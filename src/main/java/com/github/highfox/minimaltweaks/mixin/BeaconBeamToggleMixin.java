package com.github.highfox.minimaltweaks.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.highfox.minimaltweaks.util.BeaconBeamInternals;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.nbt.CompoundTag;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBeamToggleMixin implements BeaconBeamInternals {
	@Unique
	private boolean beamEnabled = true;

	@Override
	public boolean beaconBeamEnabled() {
		return beamEnabled;
	}

	@Override
	public void setBeaconBeamEnabled(boolean enabled) {
		this.beamEnabled = enabled;
	}

	@Inject(method = "getBeamSegments()Ljava/util/List;", at = @At("TAIL"), cancellable = true)
	private void modifyBeamSegments(CallbackInfoReturnable<List<BeaconBlockEntity.BeamSegment>> cir) {
		cir.setReturnValue(this.beamEnabled ? cir.getReturnValue() : ImmutableList.of());
	}

	@Inject(method = "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
	private void readBeaconBeam(BlockState state, CompoundTag tag, CallbackInfo ci) {
		this.beamEnabled = tag.getBoolean("beaconBeamEnabled");
	}

	@Inject(method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("TAIL"), cancellable = true)
	private void writeBeaconBeam(CallbackInfoReturnable<CompoundTag> cir) {
		CompoundTag tag = cir.getReturnValue();
		tag.putBoolean("beaconBeamEnabled", this.beamEnabled);
		cir.setReturnValue(tag);
	}
}
