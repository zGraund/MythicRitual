package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface RitualCondition {
    Codec<RitualCondition> CODEC = ModRegistries.CONDITION_REGISTRY.byNameCodec().dispatch(RitualCondition::type, Function.identity());

    boolean test(RitualRecipeContext context);

    List<Component> getDescription();

    /**
     * Used for sorting the conditions, this determines the execution and info line order
     */
    default int order() {return 1;}

    RitualConditionKey<? extends RitualCondition> key();

    MapCodec<? extends RitualCondition> type();

    MapCodec<Optional<String>> DESCRIPTION_CODEC = Codec.STRING.optionalFieldOf("description");

    default void appendDescription(@Nonnull List<Component> lines, String description) {
        lines.add(Component.literal(" ❯ " + description).withStyle(ChatFormatting.GRAY));
    }
}
