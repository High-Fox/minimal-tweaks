package com.github.highfox.minimaltweaks.registry;

import com.github.highfox.minimaltweaks.MTConfig;
import com.github.highfox.minimaltweaks.MinimalTweaks;
import com.github.highfox.minimaltweaks.crafting.SolidifyConcreteRecipe;
import com.github.highfox.minimaltweaks.crafting.StrippedLogsRecipe;
import com.github.highfox.minimaltweaks.crafting.conditions.ConfigTrueCondition;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MTRegistry {
	public static SpecialRecipeSerializer<StrippedLogsRecipe> CRAFTING_SPECIAL_LOGSTRIPPING;
	public static SpecialRecipeSerializer<SolidifyConcreteRecipe> CRAFTING_SPECIAL_CONCRETESOLIDIFYING;

	public static Block potted_wheat;
	public static Block potted_carrots;
	public static Block potted_potatoes;
	public static Block potted_beetroots;
	public static Block potted_melon_stem;
	public static Block potted_pumpkin_stem;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		if (MTConfig.pottableCrops.get()) {
			potted_wheat = registerBlock(new FlowerPotBlock(Blocks.WHEAT, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_wheat");
			potted_carrots = registerBlock(new FlowerPotBlock(Blocks.CARROTS, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_carrots");
			potted_potatoes = registerBlock(new FlowerPotBlock(Blocks.POTATOES, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_potatoes");
			potted_beetroots = registerBlock(new FlowerPotBlock(Blocks.BEETROOTS, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_beetroots");
			potted_melon_stem = registerBlock(new FlowerPotBlock(Blocks.MELON_STEM, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_melon_stem");
			potted_pumpkin_stem = registerBlock(new FlowerPotBlock(Blocks.PUMPKIN_STEM, AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().notSolid()), "potted_pumpkin_stem");
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		CraftingHelper.register(ConfigTrueCondition.Serializer.INSTANCE);

		CRAFTING_SPECIAL_LOGSTRIPPING = registerRecipe(new SpecialRecipeSerializer<>(StrippedLogsRecipe::new), "crafting_special_logstripping");
		CRAFTING_SPECIAL_CONCRETESOLIDIFYING = registerRecipe(new SpecialRecipeSerializer<>(SolidifyConcreteRecipe::new), "crafting_special_concretesolidifying");
	}

	private static Block registerBlock(Block block, String regname) {
		block.setRegistryName(getPath(regname));
		ForgeRegistries.BLOCKS.register(block);
		return block;
	}

	private static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> S registerRecipe(S serializer, String regname) {
		serializer.setRegistryName(getPath(regname));
		ForgeRegistries.RECIPE_SERIALIZERS.register(serializer);
		return serializer;
	}

	private static ResourceLocation getPath(String path) {
		return new ResourceLocation(MinimalTweaks.MOD_ID, path);
	}

}
