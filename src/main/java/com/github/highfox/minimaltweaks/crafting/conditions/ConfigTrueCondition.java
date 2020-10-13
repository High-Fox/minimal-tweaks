package com.github.highfox.minimaltweaks.crafting.conditions;

import com.github.highfox.minimaltweaks.MTConfig;
import com.github.highfox.minimaltweaks.MinimalTweaks;
import com.google.gson.JsonObject;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ConfigTrueCondition implements ICondition {
	private static final ResourceLocation NAME = new ResourceLocation(MinimalTweaks.MOD_ID, "config_true");
	private final String key;

	public ConfigTrueCondition(String key) {
		this.key = key;
	}

	@Override
	public ResourceLocation getID() {
		return NAME;
	}

	@Override
	public boolean test() {
		return MTConfig.CONFIG.contains(key) && MTConfig.CONFIG.getOrElse(key, false);
	}

	@Override
	public String toString() {
		return "config_true(\"" + key + "\")";
	}

	public static class Serializer implements IConditionSerializer<ConfigTrueCondition> {
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void write(JsonObject json, ConfigTrueCondition value)
		{
			json.addProperty("key", value.key.toString());
		}

		@Override
		public ConfigTrueCondition read(JsonObject json)
		{
			return new ConfigTrueCondition(JSONUtils.getString(json, "key"));
		}

		@Override
		public ResourceLocation getID()
		{
			return ConfigTrueCondition.NAME;
		}
	}

}
