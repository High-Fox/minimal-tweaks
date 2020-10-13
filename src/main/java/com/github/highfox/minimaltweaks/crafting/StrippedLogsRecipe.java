package com.github.highfox.minimaltweaks.crafting;

import java.util.Random;

import com.github.highfox.minimaltweaks.MTConfig;
import com.github.highfox.minimaltweaks.registry.MTRegistry;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class StrippedLogsRecipe extends SpecialRecipe {
	private static final Random random = new Random();

	public StrippedLogsRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int axes = 0;
		int logs = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof AxeItem) {
					axes++;
				} else if (AxeItem.BLOCK_STRIPPING_MAP.containsKey(Block.getBlockFromItem(stack.getItem()))) {
					logs++;
				}
			}
		}

		return MTConfig.strippedLogsCrafting.get() && axes == 1 && logs == 1;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (AxeItem.BLOCK_STRIPPING_MAP.containsKey(Block.getBlockFromItem(stack.getItem()))) {
					resultStack = new ItemStack(AxeItem.BLOCK_STRIPPING_MAP.get(Block.getBlockFromItem(stack.getItem())));
				}
			}
		}

		return resultStack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.hasContainerItem()) {
					nonnulllist.set(i, stack.getContainerItem());
				} else if (stack.getItem() instanceof AxeItem) {
					ItemStack axeStack = stack.copy();
					if (!axeStack.attemptDamageItem(1, random, null)) {
						nonnulllist.set(i, axeStack);
					}
				}
			}
		}

		return nonnulllist;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return MTRegistry.CRAFTING_SPECIAL_LOGSTRIPPING;
	}

}
