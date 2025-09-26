package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public interface RitualCondition {
    boolean test(RitualRecipeContext context);

    Component getDescription();

    Codec<? extends RitualCondition> codec();
}
