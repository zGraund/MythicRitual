package com.github.zgraund.mythicritual.compat.ritual_recipe_old;

import com.github.zgraund.mythicritual.recipes_old.ingredients.RitualRecipeOffering;
import mezz.jei.api.ingredients.IIngredientType;

public record RitualRecipeJEIIngredient() {
    public static final IIngredientType<RitualRecipeOffering> TYPE = () -> RitualRecipeOffering.class;
}
