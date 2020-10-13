package com.github.highfox.minimaltweaks.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IMessage {
	void encode(PacketBuffer buffer);
	void decode(PacketBuffer buffer);
	boolean receive(NetworkEvent.Context ctx);
}
