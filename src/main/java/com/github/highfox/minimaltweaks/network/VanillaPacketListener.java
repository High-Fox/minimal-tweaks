package com.github.highfox.minimaltweaks.network;

import com.github.highfox.minimaltweaks.network.event.VanillaPacketEvent;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.IPacket;
import net.minecraftforge.common.MinecraftForge;

public class VanillaPacketListener extends ChannelDuplexHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		boolean get = true;

		if (msg instanceof IPacket) {
			VanillaPacketEvent.Incoming<?> inPacket = new VanillaPacketEvent.Incoming(((IPacket<?>)msg).getClass(), (IPacket<?>)msg);
			MinecraftForge.EVENT_BUS.post(inPacket);
			if(inPacket.isCanceled()) {
				get = false;
			}
			msg = inPacket.getPacket();
		}
		if (get) {
			super.channelRead(ctx, msg);
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		boolean send = true;

		if (msg instanceof IPacket) {
			VanillaPacketEvent.Outgoing<?> outPacket = new VanillaPacketEvent.Outgoing(((IPacket<?>)msg).getClass(), (IPacket<?>)msg);
			MinecraftForge.EVENT_BUS.post(outPacket);
			if(outPacket.isCanceled()) {
				send = false;
			}
			msg = outPacket.getPacket();
		}
		if (send) {
			super.write(ctx, msg, promise);
		}
	}

}
