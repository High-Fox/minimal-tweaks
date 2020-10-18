package com.github.highfox.minimaltweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.highfox.minimaltweaks.event.FarmlandTrampleCallback;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class MinimalTweaks implements ModInitializer {
	public static final String MOD_ID = "minimaltweaks";
	public static final Logger LOG = LogManager.getLogger("Minimal Tweaks");

	@Override
	public void onInitialize() {
		MTConfig.register();
		UseBlockCallback.EVENT.register(MTEvents::onUseBlock);
		FarmlandTrampleCallback.EVENT.register(MTEvents::onTrampleCrops);
		UseEntityCallback.EVENT.register(MTEvents::onEntityInteract);
	}

}
