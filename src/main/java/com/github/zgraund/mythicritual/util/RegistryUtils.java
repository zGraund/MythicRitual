package com.github.zgraund.mythicritual.util;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class RegistryUtils {
    @Nonnull
    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key) {
        return DeferredRegister.create(key, MythicRitual.MOD_ID);
    }

    @Nonnull
    public static <T> DeferredRegister<T> create(Registry<T> registry) {
        return DeferredRegister.create(registry, MythicRitual.MOD_ID);
    }
}
