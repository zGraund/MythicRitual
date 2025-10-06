package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.registries.ModRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RitualConditions {
    public static final DeferredRegister<MapCodec<? extends RitualCondition>> CONDITIONS =
            DeferredRegister.create(ModRegistries.CONDITION_REGISTRY, MythicRitual.MOD_ID);

    static {
        register(RitualConditionKeys.ALTAR, Altar.CODEC);
        register(RitualConditionKeys.LOCATION, Location.CODEC);
        register(RitualConditionKeys.WEATHER, Weather.CODEC);
        register(RitualConditionKeys.TIME, Time.CODEC);
        register(RitualConditionKeys.CATALYST, Catalyst.CODEC);
    }

    @Nonnull
    public static <I extends RitualCondition> Supplier<MapCodec<I>> register(@Nonnull ResourceLocation id, MapCodec<I> condition) {
        return CONDITIONS.register(id.getPath(), () -> condition);
    }

    public static void register(@Nonnull IEventBus eventBus) {
        CONDITIONS.register(eventBus);
    }
}
