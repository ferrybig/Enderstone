/* 
 * Enderstone
 * Copyright (C) 2014 Sander Gielisse and Fernando van Loenhout
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.enderstone.server.packet.play;

import java.io.IOException;
import org.enderstone.server.packet.Packet;
import org.enderstone.server.packet.PacketDataWrapper;

public class PacketOutChangeGameState extends Packet {
	
	private byte reason;
	private float value;

	public PacketOutChangeGameState(byte reason, float value) {
		this.reason = reason;
		this.value = value;
	}

	@Override
	public void read(PacketDataWrapper buf) throws IOException {
		throw new RuntimeException("Packet " + this.getClass().getSimpleName() + " with ID 0x" + Integer.toHexString(getId()) + " cannot be read.");
	}

	@Override
	public void write(PacketDataWrapper buf) throws IOException {
		buf.writeByte(reason);
		buf.writeFloat(value);
	}

	@Override
	public int getSize() throws IOException {
		return 1 + getFloatSize() + getVarIntSize(getId());
	}

	@Override
	public byte getId() {
		return 0x2B;
	}
}
