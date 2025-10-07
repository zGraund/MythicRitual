package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.resources.ResourceLocation;

public class RitualConditionKey {
    // TODO: make a proper key with a type to cast in the get method and an int to sort the tooltips
    public static final ResourceLocation CATALYST = MythicRitual.id("catalyst");
    public static final ResourceLocation ALTAR = MythicRitual.id("altar");
    public static final ResourceLocation LOCATION = MythicRitual.id("location");
    public static final ResourceLocation TIME = MythicRitual.id("time");
    public static final ResourceLocation WEATHER = MythicRitual.id("weather");
}
