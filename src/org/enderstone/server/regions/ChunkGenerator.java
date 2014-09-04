package org.enderstone.server.regions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @Author Bukkit - https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/generator/ChunkGenerator.java
 * 
 * A chunk generator is responsible for the initial shaping of an entire chunk.
 * For example, the nether chunk generator should shape netherrack and soulsand
 */
public abstract class ChunkGenerator {

	/**
	 * Interface to biome data for chunk to be generated: initialized with
	 * default values for world type and seed.
	 * <p>
	 * Custom generator is free to access and tailor values during
	 * generateBlockSections() or generateExtBlockSections().
	 */
	public interface BiomeGrid {

		/**
		 * Get biome at x, z within chunk being generated
		 * 
		 * @param x
		 *            - 0-15
		 * @param z
		 *            - 0-15
		 * @return Biome value
		 */
		EnderBiome getBiome(int x, int z);

		/**
		 * Set biome at x, z within chunk being generated
		 * 
		 * @param x
		 *            - 0-15
		 * @param z
		 *            - 0-15
		 * @param bio
		 *            - Biome value
		 */
		void setBiome(int x, int z, EnderBiome bio);
	}

	/**
	 * Shapes the chunk for the given coordinates, with extended block IDs
	 * supported (0-4095).
	 * <p>
	 * As of 1.2, chunks are represented by a vertical array of chunk sections,
	 * each of which is 16 x 16 x 16 blocks. If a section is empty (all zero),
	 * the section does not need to be supplied, reducing memory usage.
	 * <p>
	 * This method must return a short[][] array in the following format:
	 * 
	 * <pre>
	 * short[][] result = new short[world - height / 16][];
	 * </pre>
	 * 
	 * Each section (sectionID = (Y>>4)) that has blocks needs to be allocated
	 * space for the 4096 blocks in that section:
	 * 
	 * <pre>
	 * result[sectionID] = new short[4096];
	 * </pre>
	 * 
	 * while sections that are not populated can be left null.
	 * <p>
	 * Setting a block at X, Y, Z within the chunk can be done with the
	 * following mapping function:
	 * 
	 * <pre>
	 * void setBlock(short[][] result, int x, int y, int z, short blkid) {
	 * 	if (result[y &gt;&gt; 4] == null) {
	 * 		result[y &gt;&gt; 4] = new short[4096];
	 * 	}
	 * 	result[y &gt;&gt; 4][((y &amp; 0xF) &lt;&lt; 8) | (z &lt;&lt; 4) | x] = blkid;
	 * }
	 * </pre>
	 * 
	 * while reading a block ID can be done with the following mapping function:
	 * 
	 * <pre>
	 * short getBlock(short[][] result, int x, int y, int z) {
	 * 	if (result[y &gt;&gt; 4] == null) {
	 * 		return (short) 0;
	 * 	}
	 * 	return result[y &gt;&gt; 4][((y &amp; 0xF) &lt;&lt; 8) | (z &lt;&lt; 4) | x];
	 * }
	 * </pre>
	 * 
	 * while sections that are not populated can be left null.
	 * <p>
	 * Setting a block at X, Y, Z within the chunk can be done with the
	 * following mapping function:
	 * 
	 * <pre>
	 *    void setBlock(short[][] result, int x, int y, int z, short blkid) {
	 *        if (result[y >> 4) == null) {
	 *            result[y >> 4] = new short[4096];
	 *        }
	 *        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	 *    }
	 * </pre>
	 * 
	 * while reading a block ID can be done with the following mapping function:
	 * 
	 * <pre>
	 *    short getBlock(short[][] result, int x, int y, int z) {
	 *        if (result[y >> 4) == null) {
	 *            return (short)0;
	 *        }
	 *        return result[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
	 *    }
	 * </pre>
	 * <p>
	 * Note that this method should <b>never</b> attempt to get the Chunk at the
	 * passed coordinates, as doing so may cause an infinite loop
	 * <p>
	 * Note generators that do not return block IDs above 255 should not
	 * implement this method, or should have it return null (which will result
	 * in the generateBlockSections() method being called).
	 * 
	 * @param world
	 *            The world this chunk will be used for
	 * @param random
	 *            The random generator to use
	 * @param x
	 *            The X-coordinate of the chunk
	 * @param z
	 *            The Z-coordinate of the chunk
	 * @param biomes
	 *            Proposed biome values for chunk - can be updated by generator
	 * @return short[][] containing the types for each block created by this
	 *         generator
	 */
	public abstract BlockId[][] generateExtBlockSections(EnderWorld world,
			Random random, int x, int z, BiomeGrid biomes);

	/**
	 * Tests if the specified location is valid for a natural spawn position
	 * 
	 * @param world
	 *            The world we're testing on
	 * @param x
	 *            X-coordinate of the block to test
	 * @param z
	 *            Z-coordinate of the block to test
	 * @return true if the location is valid, otherwise false
	 */
	public boolean canSpawn(EnderWorld world, int x, int z) {
		return true;
	}

	/**
	 * Gets a list of default {@link BlockPopulator}s to apply to a given world
	 * 
	 * @param world
	 *            World to apply to
	 * @return List containing any amount of BlockPopulators
	 */
	public List<BlockPopulator> getDefaultPopulators(EnderWorld world) {
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param x
	 *            X co-ordinate of the block to be set in the array
	 * @param y
	 *            Y co-ordinate of the block to be set in the array
	 * @param z
	 *            Z co-ordinate of the block to be set in the array
	 * @param chunk
	 *            An array containing the Block id's of all the blocks in the
	 *            chunk. The first offset is the block section number. There are
	 *            16 block sections, stacked vertically, each of which 16 by 16
	 *            by 16 blocks.
	 * @param material
	 *            The material to set the block to.
	 */
	public void setBlock(int x, int y, int z, BlockId[][] chunk,
			BlockId material) {
		// if the Block section the block is in hasn't been used yet, allocate
		// it
		if (chunk[y >> 4] == null) {
			chunk[y >> 4] = new BlockId[16 * 16 * 16];
		}
		if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0)) {
			return;
		}
		try {
			chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = material;
		} catch (Exception e) {
			// do nothing
		}
	}

	public BlockId getBlock(int x, int y, int z, BlockId[][] chunk) {
		if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0)) {
			return BlockId.AIR;
		}
		// if the Block section the block is in hasn't been used yet, allocate
		// it
		if (chunk[y >> 4] == null) {
			return BlockId.AIR; // block is air as it hasnt been allocated
		}
		return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];

	}

}
