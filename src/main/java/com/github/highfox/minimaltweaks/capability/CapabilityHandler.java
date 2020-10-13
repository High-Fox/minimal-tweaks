package com.github.highfox.minimaltweaks.capability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHandler {

	@CapabilityInject(IBeaconBeam.class)
	public static Capability<IBeaconBeam> BEACON_BEAM = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IBeaconBeam.class, new IStorage<IBeaconBeam>() {
			@Override
			public INBT writeNBT(Capability<IBeaconBeam> capability, IBeaconBeam instance, Direction side) {
				return ByteNBT.valueOf(instance.hasBeamEnabled());
			}

			@Override
			public void readNBT(Capability<IBeaconBeam> capability, IBeaconBeam instance, Direction side, INBT nbt) {
				ByteNBT bytenbt = (ByteNBT)nbt;
				instance.setBeamEnabled(bytenbt.getByte() != 0);
			}

		}, BeaconBeamWrapper::new);
	}

}
