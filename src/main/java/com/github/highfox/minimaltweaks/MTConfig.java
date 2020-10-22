package com.github.highfox.minimaltweaks;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

@Config(name = MinimalTweaks.MOD_ID)
public class MTConfig implements ConfigData {
	public boolean autoStackUsedPotions = true;
	public boolean pottableCrops = true;
	public boolean toggleableBeaconBeams = true;
	public boolean featherFallingStopsTrampling = true;
	public boolean repairableAnvils = true;
	public boolean openChestsThroughItemFrames = true;
	public boolean leadBreakSound = true;

	@ConfigEntry.Category("soulBlockConversion")
	@ConfigEntry.Gui.TransitiveObject
	public SoulBlockConversion soulBlockConversion = new SoulBlockConversion();

	@ConfigEntry.Category("utilityRecipes")
	@ConfigEntry.Gui.TransitiveObject
	public UtilityRecipes utilityRecipes = new UtilityRecipes();

	public static class SoulBlockConversion {
		public boolean enableTorchConversion = true;
		public boolean enableLanternConversion = true;
		public boolean enableCampfireConversion = true;
	}

	public static class UtilityRecipes {
		public boolean strippedLogsCrafting = true;
		public boolean solidifiedConcreteCrafting = true;
	}

	public static void register() {
		AutoConfig.register(MTConfig.class, Toml4jConfigSerializer::new);
	}

}
