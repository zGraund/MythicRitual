package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import net.minecraft.network.chat.Component;

public interface ActionOnTransmute {
    void apply(RitualRecipeContext context, RitualRecipe recipe);

    Component getDescription();
}
