package com.github.highfox.minimaltweaks.client.renderer;

import com.github.highfox.minimaltweaks.capability.CapabilityHandler;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;

public class ToggleableBeaconTileEntityRenderer extends BeaconTileEntityRenderer {

	public ToggleableBeaconTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	public void render(BeaconTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		tileEntityIn.getCapability(CapabilityHandler.BEACON_BEAM).ifPresent(instance -> {
			if (instance.hasBeamEnabled()) {
				super.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			}
		});

	}

}
