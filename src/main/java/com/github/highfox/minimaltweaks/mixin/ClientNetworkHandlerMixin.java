package com.github.highfox.minimaltweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.highfox.minimaltweaks.MinimalTweaks;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntityAttach(Lnet/minecraft/network/packet/s2c/play/EntityAttachS2CPacket;)V", at = @At("HEAD"))
	private void sendLeadBreakSound(EntityAttachS2CPacket packet, CallbackInfo ci) {
		Entity entity = world.getEntityById(packet.getAttachedEntityId());
		if (entity instanceof MobEntity && packet.getHoldingEntityId() == 0) {
			PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
			data.writeBlockPos(entity.getBlockPos());

			ClientSidePacketRegistry.INSTANCE.sendToServer(MinimalTweaks.LEAD_BREAK_PACKET_ID, data);
		}
	}

}
