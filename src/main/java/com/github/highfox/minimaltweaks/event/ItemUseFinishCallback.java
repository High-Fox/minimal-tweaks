package com.github.highfox.minimaltweaks.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ItemUseFinishCallback {
	Event<ItemUseFinishCallback> EVENT = EventFactory.createArrayBacked(ItemUseFinishCallback.class, listeners -> (user, usedStack, resultStack) -> {
		for (ItemUseFinishCallback listener : listeners) {
			ItemStack modifedResultStack = listener.interact(user, usedStack, resultStack);

			return modifedResultStack;
		}

		return resultStack;
	});

	ItemStack interact(LivingEntity user, ItemStack usedStack, ItemStack resultStack);

}
