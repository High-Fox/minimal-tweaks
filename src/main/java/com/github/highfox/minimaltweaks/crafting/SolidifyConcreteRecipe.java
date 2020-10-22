package com.github.highfox.minimaltweaks.crafting;

import com.github.highfox.minimaltweaks.MTEvents;
import com.github.highfox.minimaltweaks.MTRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SolidifyConcreteRecipe extends SpecialCraftingRecipe {
	public SolidifyConcreteRecipe(Identifier idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int powders = 0;
		int waterBuckets = 0;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == Items.WATER_BUCKET) {
					waterBuckets++;
				} else if (Block.getBlockFromItem(stack.getItem()) instanceof ConcretePowderBlock) {
					powders++;
				}
			}
		}

		return MTEvents.CONFIG.utilityRecipes.solidifiedConcreteCrafting && powders == 1 && waterBuckets == 1;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (Block.getBlockFromItem(stack.getItem()) instanceof ConcretePowderBlock) {
					ConcretePowderBlock powderBlock = (ConcretePowderBlock)Block.getBlockFromItem(stack.getItem());
					resultStack = new ItemStack(powderBlock.hardenedState.getBlock());
				}
			}
		}

		return resultStack;
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
		DefaultedList<ItemStack> nonnulllist = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() == Items.WATER_BUCKET) {
				nonnulllist.set(i, stack.copy());
			}
		}

		return nonnulllist;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MTRegistry.CRAFTING_SPECIAL_CONCRETESOLIDIFYING;
	}

}
