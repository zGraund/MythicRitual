package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface RitualCondition {
    Codec<RitualCondition> CODEC = ModRegistries.CONDITION_REGISTRY.byNameCodec().dispatch(RitualCondition::type, Function.identity());

    boolean test(RitualRecipeContext context);

    List<Component> getDescription();

    ResourceLocation getResourceLocation();

    MapCodec<? extends RitualCondition> type();

    MapCodec<Optional<String>> DESCRIPTION_CODEC = Codec.STRING.optionalFieldOf("description");
}
