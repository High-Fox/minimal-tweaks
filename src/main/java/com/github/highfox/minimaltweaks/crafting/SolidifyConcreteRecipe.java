package com.github.highfox.minimaltweaks.crafting;

import com.github.highfox.minimaltweaks.MTConfig;
import com.github.highfox.minimaltweaks.MTRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class SolidifyConcreteRecipe extends SpecialRecipe {
	public SolidifyConcreteRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		int powders = 0;
		int waterBuckets = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY).getFluid().isIn(FluidTags.WATER)) {
					waterBuckets++;
				} else if (Block.getBlockFromItem(stack.getItem()) instanceof ConcretePowderBlock) {
					powders++;
				}
			}
		}

		return MTConfig.solidifiedConcreteCrafting.get() && powders == 1 && waterBuckets == 1;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (Block.getBlockFromItem(stack.getItem()) instanceof ConcretePowderBlock) {
					ConcretePowderBlock powderBlock = (ConcretePowderBlock)Block.getBlockFromItem(stack.getItem());
					resultStack = new ItemStack(powderBlock.solidifiedState.getBlock());
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
			if (FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY).getFluid().isIn(FluidTags.WATER)) {
				nonnulllist.set(i, stack.copy());
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
		return MTRegistry.CRAFTING_SPECIAL_CONCRETESOLIDIFYING;
	}

}
