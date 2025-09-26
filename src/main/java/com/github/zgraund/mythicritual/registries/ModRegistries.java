package com.github.zgraund.mythicritual.registries;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipe.action.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.condition.RitualCondition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class ModRegistries {
    public static final ResourceKey<Registry<ActionOnTransmute>> ACTION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(MythicRitual.id("action_on_transmute"));
    public static final ResourceKey<Registry<RitualCondition>> CONDITION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(MythicRitual.id("ritual_condition"));

    public static final Registry<ActionOnTransmute> ACTION_REGISTRY = new RegistryBuilder<>(ACTION_REGISTRY_KEY).sync(true).create();
    public static final Registry<RitualCondition> CONDITION_REGISTRY = new RegistryBuilder<>(CONDITION_REGISTRY_KEY).sync(true).create();
}
