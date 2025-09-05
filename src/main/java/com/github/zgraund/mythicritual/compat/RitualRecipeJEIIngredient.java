package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeOffering;
import mezz.jei.api.ingredients.IIngredientType;

public record RitualRecipeJEIIngredient() {
    public static final IIngredientType<RitualRecipeOffering> TYPE = () -> RitualRecipeOffering.class;
}
