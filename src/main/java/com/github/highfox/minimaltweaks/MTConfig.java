package com.github.highfox.minimaltweaks;

import net.minecraftforge.common.ForgeConfigSpec;

public class MTConfig {
	public static ForgeConfigSpec CONFIG;
	public static String CATEGORY_MAIN = "settings";

	public static String CATEGORY_SOUL_BLOCK_CONVERSION = "soulBlockConversion";
	public static ForgeConfigSpec.BooleanValue enableTorchConversion;
	public static ForgeConfigSpec.BooleanValue enableLanternConversion;
	public static ForgeConfigSpec.BooleanValue enableCampfireConversion;

	public static String CATEGORY_RECIPES = "utilityRecipes";
	public static ForgeConfigSpec.BooleanValue strippedLogsCrafting;
	public static ForgeConfigSpec.BooleanValue solidifiedConcreteCrafting;

	public static String CATEGORY_LEADABLE_MOBS = "leadableMobs";
	public static ForgeConfigSpec.BooleanValue leadableHoglinsAndZoglins;
	public static ForgeConfigSpec.BooleanValue leadableTurtles;
	public static ForgeConfigSpec.BooleanValue leadablePandas;

	public static ForgeConfigSpec.BooleanValue autoStackUsedPotions;
	public static ForgeConfigSpec.BooleanValue pottableCrops;
	public static ForgeConfigSpec.BooleanValue toggleableBeaconBeams;
	public static ForgeConfigSpec.BooleanValue featherFallingStopsTrampling;
	public static ForgeConfigSpec.BooleanValue repairableAnvils;
	public static ForgeConfigSpec.BooleanValue openChestsThroughItemFrames;
	public static ForgeConfigSpec.BooleanValue openChestsThroughSigns;
	public static ForgeConfigSpec.BooleanValue leadBreakSound;

	static {
		ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

		BUILDER.comment("Mod settings").push(CATEGORY_MAIN);

		BUILDER.comment("Converting of certain blocks to their soul fire variants").push(CATEGORY_SOUL_BLOCK_CONVERSION);
		enableTorchConversion = BUILDER.comment("Enable torch to soul torch converting")
				.define("enableTorchConversion", true);
		enableLanternConversion = BUILDER.comment("Enable lantern to soul lantern converting")
				.define("enableLanternConversion", true);
		enableCampfireConversion = BUILDER.comment("Enable campfire to soul campfire converting")
				.define("enableCampfireConversion", true);
		BUILDER.pop();

		BUILDER.comment("Various recipe modifications").push(CATEGORY_RECIPES);
		strippedLogsCrafting = BUILDER.comment("Make stripped logs craftable with an axe + log")
				.define("strippedLogsCrafting", true);
		solidifiedConcreteCrafting = BUILDER.comment("Make concrete powder solidifiable in a crafting grid with a water bucket")
				.define("solidifiedConcreteCrafting", true);
		BUILDER.pop();

		BUILDER.comment("Allow leads to be used on some previously unleadable mobs").push(CATEGORY_LEADABLE_MOBS);
		leadableHoglinsAndZoglins = BUILDER.define("leadableHoglinsAndZoglins", true);
		leadableTurtles = BUILDER.define("leadableTurtles", true);
		leadablePandas = BUILDER.define("leadablePandas", true);

		autoStackUsedPotions = BUILDER.comment("Add the glass bottle received after drinking a potion to an existing stack of glass bottles in the inventory, if one exists")
			.define("autoStackUsedPotions", true);
		pottableCrops = BUILDER.comment("Allow crops to placed in flower pots")
				.define("pottableCrops", true);
		toggleableBeaconBeams = BUILDER.comment("Allows shift-clicking a beacon with an empty hand to turn off it's beam. (This does not remove the sky access requirement)")
				.define("toggleableBeaconBeams", true);
		featherFallingStopsTrampling = BUILDER.comment("Feather falling boots prevent the trampling of crops - suggested by AtomicWriting28")
				.define("featherFallingStopsTrampling", true);
		repairableAnvils = BUILDER.comment("Anvils can be repaired by right clicking them with an iron block - suggested by Mr Boness YT")
				.define("repairableAnvils", true);
		openChestsThroughItemFrames = BUILDER.comment("Lets you open chests through item frames. Sneak for default behaviour")
				.define("openChestsThroughItemFrames", true);
		openChestsThroughSigns = BUILDER.comment("Lets you open chests through signs.")
				.define("openChestsThroughSigns", true);
		leadBreakSound = BUILDER.comment("Plays a sound when an attached lead breaks")
				.define("leadBreakSound", true);

		BUILDER.pop();

		CONFIG = BUILDER.build();
	}

}
