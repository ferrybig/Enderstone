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

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.enderstone.server.chat.Message;
import org.enderstone.server.inventory.Inventory.InventoryType;
import org.enderstone.server.packet.Packet;

/**
 *
 * @author Fernando
 */
public class PacketOutOpenWindow extends Packet {
	private byte windowId;
	private InventoryType inventoryType;
	private Message windowMessage;
	private transient String windowMessageString;
	private byte maxSlots;
	private int entityId;

	public PacketOutOpenWindow(byte windowId, InventoryType inventoryType, Message windowMessage, byte maxSlots, int entityId) {
		this.windowId = windowId;
		this.inventoryType = inventoryType;
		this.windowMessage = windowMessage;
		this.maxSlots = maxSlots;
		this.entityId = entityId;
	}
	
	@Override
	public void read(ByteBuf buf) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void write(ByteBuf buf) throws IOException {
		if(windowMessageString == null) windowMessageString = windowMessage.toMessageJson();
		buf.writeByte(windowId);
		writeString(inventoryType.getInventoryType(), buf);
		writeString(windowMessageString, buf);
		buf.writeByte(maxSlots);
		if(inventoryType == InventoryType.ENTITY_HORSE) writeVarInt(entityId, buf);
	}

	@Override
	public int getSize() throws IOException {
		if(windowMessageString == null) windowMessageString = windowMessage.toMessageJson();
		return 1 + getStringSize(inventoryType.getInventoryType()) + getStringSize(windowMessageString) + 1 + (inventoryType == InventoryType.ENTITY_HORSE ? getIntSize() : 0) + getVarIntSize(getId());
	}

	@Override
	public byte getId() {
		return 0x2D;
	}
	
}
