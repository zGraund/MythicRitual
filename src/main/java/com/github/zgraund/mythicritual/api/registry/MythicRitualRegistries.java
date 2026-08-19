package com.github.zgraund.mythicritual.api.registry;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.api.condition.RitualCondition;
import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.api.postaction.RitualPostAction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nonnull;

public class MythicRitualRegistries {
    public static final ResourceKey<Registry<MapCodec<? extends RitualInput<?>>>> RITUAL_INPUT = key("ritual_input");
    public static final ResourceKey<Registry<MapCodec<? extends RitualOutput>>> RITUAL_OUTPUT = key("ritual_output");
    public static final ResourceKey<Registry<MapCodec<? extends RitualCondition>>> RITUAL_CONDITION = key("ritual_condition");
    public static final ResourceKey<Registry<MapCodec<? extends RitualPostAction>>> RITUAL_POST_ACTION = key("ritual_post_action");

    public static final ResourceKey<Registry<MapCodec<? extends PositionResolver>>> POSITION_RESOLVER = key("position_resolver");


    @Nonnull
    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(MythicRitual.asResource(name));
    }
}
