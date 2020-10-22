package com.github.highfox.minimaltweaks.crafting;

import java.util.Random;

import com.github.highfox.minimaltweaks.MTEvents;
import com.github.highfox.minimaltweaks.MTRegistry;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class StrippedLogsRecipe extends SpecialCraftingRecipe {
	private static final Random random = new Random();

	public StrippedLogsRecipe(Identifier idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int axes = 0;
		int logs = 0;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof AxeItem) {
					axes++;
				} else if (AxeItem.BLOCK_ITEMS.containsKey(Block.getBlockFromItem(stack.getItem()))) {
					logs++;
				}
			}
		}

		return MTEvents.CONFIG.utilityRecipes.strippedLogsCrafting && axes == 1 && logs == 1;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				if (AxeItem.STRIPPED_BLOCKS.containsKey(Block.getBlockFromItem(stack.getItem()))) {
					resultStack = new ItemStack(AxeItem.STRIPPED_BLOCKS.get(Block.getBlockFromItem(stack.getItem())));
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
			if (!stack.isEmpty()) {
				if (stack.getItem().hasRecipeRemainder()) {
					nonnulllist.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
				} else if (stack.getItem() instanceof AxeItem) {
					ItemStack axeStack = stack.copy();
					if (!axeStack.damage(1, random, null)) {
						nonnulllist.set(i, axeStack);
					}
				}
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
		return MTRegistry.CRAFTING_SPECIAL_LOGSTRIPPING;
	}

}
