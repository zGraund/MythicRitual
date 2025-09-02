package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import mezz.jei.api.ingredients.IIngredientType;

public record RitualRecipeJEIIngredient() {
    public static final IIngredientType<RitualRecipeIngredient> TYPE = () -> RitualRecipeIngredient.class;
}
