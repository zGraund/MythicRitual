package com.github.zgraund.mythicritual.compat.ritual_recipe_old;

import com.github.zgraund.mythicritual.recipes_old.ingredients.RitualRecipeOffering;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public record RitualRecipeIngredientHelper() implements IIngredientHelper<RitualRecipeOffering> {
    @Override
    @Nonnull
    public IIngredientType<RitualRecipeOffering> getIngredientType() {
        return RitualRecipeJEIIngredient.TYPE;
    }

    @Nonnull
    @Override
    public String getDisplayName(RitualRecipeOffering ingredient) {
        return ingredient.getDisplayName().getString();
    }

    @Nonnull
    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(RitualRecipeOffering ingredient, UidContext context) {
        return ingredient.type().toString();
    }

    @Nonnull
    @Override
    public ResourceLocation getResourceLocation(RitualRecipeOffering ingredient) {
        return ingredient.type();
    }

    @Nonnull
    @Override
    public RitualRecipeOffering copyIngredient(RitualRecipeOffering ingredient) {
        return ingredient.copy();
    }

    @Override
    public long getAmount(RitualRecipeOffering ingredient) {
        return ingredient.quantity();
    }

    @Nonnull
    @Override
    public String getErrorInfo(@Nullable RitualRecipeOffering ingredient) {
        if (ingredient == null) {
            return "error ingredient: null";
        }
        return "[name: " + getDisplayName(ingredient) + ", quantity: " + ingredient.quantity() + ", offset: " + ingredient.offset() + "]";
    }
}
