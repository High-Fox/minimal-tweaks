package com.github.highfox.minimaltweaks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public class MinimalTweaksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			int i = 6;
			int j = i * 32;
			int k = 255 - i * 8;
			int l = i * 4;
			return j << 16 | k << 8 | l;
		}, MTRegistry.potted_melon_stem, MTRegistry.potted_pumpkin_stem);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MTRegistry.potted_wheat, MTRegistry.potted_carrots, MTRegistry.potted_potatoes, MTRegistry.potted_beetroots, MTRegistry.potted_melon_stem, MTRegistry.potted_pumpkin_stem);
	}

}
