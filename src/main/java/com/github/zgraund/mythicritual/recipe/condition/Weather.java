package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

public record Weather(Optional<Conditions> condition, Optional<String> description) implements RitualCondition {
    public static final MapCodec<Weather> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            StringRepresentable.fromEnum(Conditions::values).optionalFieldOf("value").forGetter(Weather::condition),
            DESCRIPTION_CODEC.forGetter(Weather::description)
    ).apply(inst, Weather::new));

    @Override
    public boolean test(RitualRecipeContext context) {
        return condition.isEmpty() || condition.get().test(context.level());
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        List<Component> lines = new ArrayList<>();
        condition.ifPresent(conditions -> lines.add(
                Component.translatable(
                        "info.hover.ritual.condition.weather",
                        Component.literal(conditions.name).withStyle(ChatFormatting.GREEN)
                ).withStyle(ChatFormatting.GRAY)
        ));
        description.ifPresent(s -> lines.add(Component.literal(description().get()).withStyle(ChatFormatting.GRAY)));
        return lines;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RitualConditionKeys.WEATHER;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }

    public enum Conditions implements StringRepresentable, Predicate<Level> {
        CLEAR("clear", level -> !level.isRaining() && !level.isThundering()),
        RAIN("rain", Level::isRaining),
        THUNDER("thunder", Level::isThundering);

        private final String name;
        private final Predicate<Level> predicate;

        Conditions(@Nonnull String name, Predicate<Level> predicate) {
            this.name = name.toLowerCase(Locale.ROOT);
            this.predicate = predicate;
        }

        @Override
        public boolean test(@Nonnull Level level) {
            return this.predicate.test(level);
        }

        @Nonnull
        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
