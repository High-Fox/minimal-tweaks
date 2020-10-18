package com.github.highfox.minimaltweaks;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

@Config(name = MinimalTweaks.MOD_ID)
public class MTConfig implements ConfigData {
	boolean autoStackUsedPotions = true;
	boolean pottableCrops = true;
	boolean toggleableBeaconBeams = true;
	boolean featherFallingStopsTrampling = true;
	boolean repairableAnvils = true;
	boolean openChestsThroughItemFrames = true;
	boolean leadBreakSound = true;

	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	SoulBlockConversion soulBlockConversion = new SoulBlockConversion();

	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	UtilityRecipes utilityRecipes = new UtilityRecipes();

	public static class SoulBlockConversion {
		boolean enableTorchConversion = true;
		boolean enableLanternConversion = true;
		boolean enableCampfireConversion = true;
	}

	public static class UtilityRecipes {
		boolean strippedLogsCrafting = true;
		boolean solidifiedConcreteCrafting = true;
	}

	public static void register() {
		AutoConfig.register(MTConfig.class, Toml4jConfigSerializer::new);
	}

}
