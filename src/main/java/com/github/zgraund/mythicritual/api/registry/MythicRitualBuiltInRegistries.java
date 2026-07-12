package com.github.zgraund.mythicritual.api.registry;

import com.github.zgraund.mythicritual.components.RitualInput;
import com.github.zgraund.mythicritual.components.RitualOutput;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

public class MythicRitualBuiltInRegistries {
    public static final Registry<MapCodec<? extends RitualInput>> INPUT = createRegistry(MythicRitualRegistry.INPUT_KEY);
    public static final Registry<MapCodec<? extends RitualOutput>> OUTPUT = createRegistry(MythicRitualRegistry.OUTPUT_KEY);

    @Nonnull
    private static <T> Registry<T> createRegistry(ResourceKey<? extends Registry<T>> key) {
        return new RegistryBuilder<>(key).create();
    }

    @SubscribeEvent
    public static void register(@Nonnull NewRegistryEvent event) {
        event.register(INPUT);
        event.register(OUTPUT);
    }
}
