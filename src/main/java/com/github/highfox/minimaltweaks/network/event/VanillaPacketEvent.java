package com.github.highfox.minimaltweaks.network.event;

import net.minecraft.network.IPacket;
import net.minecraftforge.eventbus.api.GenericEvent;

public class VanillaPacketEvent<T extends IPacket<?>> extends GenericEvent<T> {
	private final T packet;

	public VanillaPacketEvent(Class<T> type, T packet) {
		super(type);
		this.packet = packet;
	}

	public T getPacket() {
		return packet;
	}

	public static class Outgoing<T extends IPacket<?>> extends VanillaPacketEvent<T> {
		public Outgoing(Class<T> type, T packet) {
			super(type, packet);
		}
	}

	public static class Incoming<T extends IPacket<?>> extends VanillaPacketEvent<T> {
		public Incoming(Class<T> type, T packet) {
			super(type, packet);
		}
	}

}
