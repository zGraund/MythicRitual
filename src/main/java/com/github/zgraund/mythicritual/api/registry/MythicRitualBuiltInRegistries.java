package com.github.zgraund.mythicritual.api.registry;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.api.condition.RitualCondition;
import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.api.postaction.RitualPostAction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@EventBusSubscriber
public class MythicRitualBuiltInRegistries {
    public static final Registry<MapCodec<? extends RitualInput<?>>> RITUAL_INPUT = create(MythicRitualRegistries.RITUAL_INPUT);
    public static final Registry<MapCodec<? extends RitualOutput>> RITUAL_OUTPUT = create(MythicRitualRegistries.RITUAL_OUTPUT);
    public static final Registry<MapCodec<? extends RitualCondition>> RITUAL_CONDITION = create(MythicRitualRegistries.RITUAL_CONDITION);
    public static final Registry<MapCodec<? extends RitualPostAction>> RITUAL_POST_ACTION = create(MythicRitualRegistries.RITUAL_POST_ACTION);

    public static final Registry<MapCodec<? extends PositionResolver>> POSITION_RESOLVER = create(MythicRitualRegistries.POSITION_RESOLVER);

    @Nonnull
    private static <T> Registry<T> create(ResourceKey<? extends Registry<T>> key) {
        return new RegistryBuilder<>(key).create();
    }

    @SubscribeEvent
    public static void register(@Nonnull NewRegistryEvent event) {
        event.register(RITUAL_INPUT);
        event.register(RITUAL_OUTPUT);
        event.register(RITUAL_CONDITION);
        event.register(RITUAL_POST_ACTION);

        event.register(POSITION_RESOLVER);
    }
}
