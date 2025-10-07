package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.FormatUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Time(Optional<MinMaxBounds.Ints> time, Optional<String> description) implements RitualCondition {
    public static final MapCodec<Time> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            MinMaxBounds.Ints.CODEC.optionalFieldOf("time").forGetter(Time::time),
            DESCRIPTION_CODEC.forGetter(Time::description)
    ).apply(inst, Time::new));

    @Override
    public boolean test(RitualRecipeContext context) {
        return time.isEmpty() || time.get().matches((int) (context.level().dayTime() % 24000));
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        time.ifPresent(bounds -> lines.add(Component.translatable(
                        "info.hover.ritual.condition.time",
                        FormatUtils.minMaxBoundToComponent(bounds)
                ).withStyle(ChatFormatting.GRAY)
        ));
        description.ifPresent(s -> lines.add(Component.literal(s).withStyle(ChatFormatting.GRAY)));
        return lines;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RitualConditionKey.TIME;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }
}
