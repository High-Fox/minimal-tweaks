package com.github.highfox.minimaltweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.highfox.minimaltweaks.event.ItemUseFinishCallback;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public class LivingFinishItemUseMixin {

	@Redirect(method = "consumeItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack finishUsing(ItemStack usedStack, World world, LivingEntity user) {
		ItemStack resultStack = ItemUseFinishCallback.EVENT.invoker().interact(user, usedStack.copy(), usedStack.finishUsing(world, user));
		return resultStack;
	}

}
