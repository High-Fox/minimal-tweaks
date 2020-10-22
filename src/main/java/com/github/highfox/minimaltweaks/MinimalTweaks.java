package com.github.highfox.minimaltweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.highfox.minimaltweaks.event.FarmlandTrampleCallback;
import com.github.highfox.minimaltweaks.event.ItemUseFinishCallback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MinimalTweaks implements ModInitializer {
	public static final String MOD_ID = "minimaltweaks";
	public static final Logger LOG = LogManager.getLogger("Minimal Tweaks");

	public static final Identifier LEAD_BREAK_PACKET_ID = new Identifier(MinimalTweaks.MOD_ID, "lead_break");

	@Override
	public void onInitialize() {
		MTConfig.register();
		MTRegistry.registerBlocks();
		MTRegistry.registerRecipeSerializers();

		UseBlockCallback.EVENT.register(MTEvents::onUseBlock);
		FarmlandTrampleCallback.EVENT.register(MTEvents::onTrampleCrops);
		UseEntityCallback.EVENT.register(MTEvents::onEntityInteract);
		ItemUseFinishCallback.EVENT.register(MTEvents::onItemUseFinish);

		ServerSidePacketRegistry.INSTANCE.register(LEAD_BREAK_PACKET_ID, (ctx, data) -> {
			BlockPos pos = data.readBlockPos();
			ctx.getTaskQueue().execute(() -> {
				ctx.getPlayer().getEntityWorld().playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.PLAYERS, 1.0F, 1.5F);
			});
		});

	}

}
