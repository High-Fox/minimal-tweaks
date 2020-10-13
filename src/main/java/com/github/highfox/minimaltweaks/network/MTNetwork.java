package com.github.highfox.minimaltweaks.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.highfox.minimaltweaks.MinimalTweaks;
import com.github.highfox.minimaltweaks.network.message.IMessage;

import io.netty.channel.ChannelPipeline;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MTNetwork {
	private static final String PROTOCOL_VERSION = "1";
	private static boolean firstConnection = true;
	private static int i = 0;
	public final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(MinimalTweaks.MOD_ID, "mtmain"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public <Msg extends IMessage> void registerMessage(Class<Msg> clazz, Supplier<Msg> supplier, NetworkDirection direction) {
		BiConsumer<Msg, PacketBuffer> encoder = Msg::encode;
		Function<PacketBuffer, Msg> decoder = buffer -> {
			Msg message = supplier.get();
			message.decode(buffer);
			return message;
		};
		BiConsumer<Msg, Supplier<NetworkEvent.Context>> handler = (message, ctxsupp) -> {
			NetworkEvent.Context ctx = ctxsupp.get();
			if (ctx.getDirection() != direction) {
				return;
			}
			ctx.setPacketHandled(message.receive(ctx));
		};

		channel.registerMessage(i++, clazz, encoder, decoder, handler);
	}

	public void sendToPlayer(IMessage message, ServerPlayerEntity player) {
		channel.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	public void sendToServer(IMessage msg) {
		channel.sendToServer(msg);
	}

	@SubscribeEvent
	public void playerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
		if (firstConnection) {
			firstConnection = false;
			ChannelPipeline pipeline = event.getNetworkManager().channel().pipeline();
			pipeline.addBefore("packet_handler", "listener", new VanillaPacketListener());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void playerDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		firstConnection = true;
	}


}
