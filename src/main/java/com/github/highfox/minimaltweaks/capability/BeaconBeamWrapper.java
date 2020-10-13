package com.github.highfox.minimaltweaks.capability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BeaconBeamWrapper implements IBeaconBeam, ICapabilitySerializable<ByteNBT> {
	private boolean beamEnabled = true;

	@Override
	public boolean hasBeamEnabled() {
		return beamEnabled;
	}

	@Override
	public void setBeamEnabled(boolean enabled) {
		beamEnabled = enabled;
	}

	@Override
	public void toggleBeamEnabled() {
		setBeamEnabled(!beamEnabled);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityHandler.BEACON_BEAM) {
			return LazyOptional.of(() -> this).cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public ByteNBT serializeNBT() {
		return ByteNBT.valueOf(beamEnabled);
	}

	@Override
	public void deserializeNBT(ByteNBT nbt) {
		setBeamEnabled(nbt.getByte() != 0);
	}

}
