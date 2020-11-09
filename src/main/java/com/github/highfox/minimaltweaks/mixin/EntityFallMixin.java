package com.github.highfox.minimaltweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.highfox.minimaltweaks.event.FarmlandTrampleCallback;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class EntityFallMixin {

	@Redirect(method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
	private void onFall(Block block, World world, BlockPos pos, Entity entity, float distance) {
		if (entity instanceof LivingEntity) {
			ActionResult result = FarmlandTrampleCallback.EVENT.invoker().interact(world, pos, (LivingEntity)entity);

			if (result == ActionResult.PASS || result.isAccepted()) {
				block.onLandedUpon(world, pos, entity, distance);
			} else {
				entity.handleFallDamage(distance, 1.0F);
			}
		} else {
			block.onLandedUpon(world, pos, entity, distance);
		}
	}

}
