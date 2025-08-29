package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import mezz.jei.api.ingredients.IIngredientType;

public record RitualRecipeJEIIngredient() {
    public static final IIngredientType<RitualRecipeIngredient> RITUAL_RECIPE_INGREDIENT_JEI_TYPE = () -> RitualRecipeIngredient.class;
}
