package com.github.highfox.minimaltweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.highfox.minimaltweaks.capability.CapabilityHandler;
import com.github.highfox.minimaltweaks.client.renderer.ToggleableBeaconTileEntityRenderer;
import com.github.highfox.minimaltweaks.network.MTNetwork;
import com.github.highfox.minimaltweaks.registry.MTRegistry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinimalTweaks.MOD_ID)
public class MinimalTweaks {
	public static final String MOD_ID = "minimaltweaks";
	public static final Logger LOG = LogManager.getLogger("Minimal Tweaks");
	public static MTNetwork network;

	public MinimalTweaks() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MTConfig.CONFIG);
	}

	public void commonSetup(final FMLCommonSetupEvent event) {
		CapabilityHandler.register();
		network = new MTNetwork();
		MinecraftForge.EVENT_BUS.register(network);
	}

	public void clientSetup(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_wheat, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_carrots, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_potatoes, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_beetroots, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_melon_stem, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MTRegistry.potted_pumpkin_stem, RenderType.getCutout());

		ClientRegistry.bindTileEntityRenderer(TileEntityType.BEACON, ToggleableBeaconTileEntityRenderer::new);
	}

	@SubscribeEvent
	public void blockColorsInit(ColorHandlerEvent.Block event) {
		BlockColors colors = event.getBlockColors();
		colors.register((state, world, pos, tintIndex) -> {
			int i = 7;
			int j = i * 32;
			int k = 255 - i * 8;
			int l = i * 4;
			return j << 16 | k << 8 | l;
		}, MTRegistry.potted_melon_stem, MTRegistry.potted_pumpkin_stem);
	}
}
