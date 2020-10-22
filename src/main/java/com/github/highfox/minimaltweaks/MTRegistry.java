package com.github.highfox.minimaltweaks;

import com.github.highfox.minimaltweaks.crafting.SolidifyConcreteRecipe;
import com.github.highfox.minimaltweaks.crafting.StrippedLogsRecipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MTRegistry {
	private static AbstractBlock.Settings POTTED_CROP = AbstractBlock.Settings.of(Material.SUPPORTED).breakInstantly().nonOpaque();

	public static SpecialRecipeSerializer<StrippedLogsRecipe> CRAFTING_SPECIAL_LOGSTRIPPING;
	public static RecipeSerializer<SolidifyConcreteRecipe> CRAFTING_SPECIAL_CONCRETESOLIDIFYING;

	public static Block potted_wheat;
	public static Block potted_carrots;
	public static Block potted_potatoes;
	public static Block potted_beetroots;
	public static Block potted_melon_stem;
	public static Block potted_pumpkin_stem;

	public static void registerBlocks() {
		potted_wheat = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_wheat"), new FlowerPotBlock(Blocks.WHEAT, POTTED_CROP));
		potted_carrots = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_carrots"), new FlowerPotBlock(Blocks.CARROTS, POTTED_CROP));
		potted_potatoes = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_potatoes"), new FlowerPotBlock(Blocks.POTATOES, POTTED_CROP));
		potted_beetroots = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_beetroots"), new FlowerPotBlock(Blocks.BEETROOTS, POTTED_CROP));
		potted_melon_stem = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_melon_stem"), new FlowerPotBlock(Blocks.MELON_STEM, POTTED_CROP));
		potted_pumpkin_stem = Registry.register(Registry.BLOCK, new Identifier(MinimalTweaks.MOD_ID, "potted_pumpkin_stem"), new FlowerPotBlock(Blocks.PUMPKIN_STEM, POTTED_CROP));
	}

	public static void registerRecipeSerializers() {
		CRAFTING_SPECIAL_CONCRETESOLIDIFYING = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MinimalTweaks.MOD_ID, "crafting_special_concretesolidifying"), new SpecialRecipeSerializer<>(SolidifyConcreteRecipe::new));
		CRAFTING_SPECIAL_LOGSTRIPPING = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MinimalTweaks.MOD_ID, "crafting_special_logstripping"), new SpecialRecipeSerializer<>(StrippedLogsRecipe::new));
	}

}
