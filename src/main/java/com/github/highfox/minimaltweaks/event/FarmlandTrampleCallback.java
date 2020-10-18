package com.github.highfox.minimaltweaks.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface FarmlandTrampleCallback {

	Event<FarmlandTrampleCallback> EVENT = EventFactory.createArrayBacked(FarmlandTrampleCallback.class, listeners -> (world, pos, entity) -> {
		for (FarmlandTrampleCallback listener : listeners) {
			ActionResult result = listener.interact(world, pos, entity);

			if(result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.PASS;
	});

	ActionResult interact(World world, BlockPos pos, LivingEntity entity);

}
