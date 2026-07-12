package com.github.zgraund.mythicritual.api.registry;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.components.RitualInput;
import com.github.zgraund.mythicritual.components.RitualOutput;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nonnull;

public class MythicRitualRegistry {
    public static final ResourceKey<Registry<MapCodec<? extends RitualInput>>> INPUT_KEY = createKey("ritual_input");
    public static final ResourceKey<Registry<MapCodec<? extends RitualOutput>>> OUTPUT_KEY = createKey("ritual_output");

    @Nonnull
    private static <T> ResourceKey<Registry<T>> createKey(String name) {
        return ResourceKey.createRegistryKey(MythicRitual.asResource(name));
    }
}
