package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.FormatUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Time(MinMaxBounds.Ints time, Optional<String> description) implements RitualCondition {
    public static final MapCodec<Time> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            MinMaxBounds.Ints.CODEC.optionalFieldOf("time", MinMaxBounds.Ints.ANY).forGetter(Time::time),
            DESCRIPTION_CODEC.forGetter(Time::description)
    ).apply(inst, Time::new));

    @Override
    public boolean test(@Nonnull RitualRecipeContext context) {
        return time.matches((int) (context.level().dayTime() % 24000));
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        if (time != MinMaxBounds.Ints.ANY) {
            lines.add(Component.translatable(
                            "info.hover.ritual.condition.time",
                            FormatUtils.minMaxBoundToComponent(time)
                    ).withStyle(ChatFormatting.GRAY)
            );
        }
        description.ifPresent(s -> lines.add(Component.literal(s).withStyle(ChatFormatting.GRAY)));
        return lines;
    }

    @Override
    public int order() {return 20;}

    @Override
    public RitualConditionKey<Time> key() {
        return RitualConditionKey.TIME;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }
}
