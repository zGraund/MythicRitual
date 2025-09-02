package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public record RitualRecipeIngredientHelper() implements IIngredientHelper<RitualRecipeIngredient> {

    @Override
    @Nonnull
    public IIngredientType<RitualRecipeIngredient> getIngredientType() {
        return RitualRecipeJEIIngredient.TYPE;
    }

    @Nonnull
    @Override
    public String getDisplayName(@NotNull RitualRecipeIngredient ingredient) {
        return ingredient.getDisplayName().getString();
    }

    @Nonnull
    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(@NotNull RitualRecipeIngredient ingredient, @NotNull UidContext context) {
        return ingredient.type().toString();
    }

    @Nonnull
    @Override
    public ResourceLocation getResourceLocation(@NotNull RitualRecipeIngredient ingredient) {
        return ingredient.type();
    }

    @Nonnull
    @Override
    public RitualRecipeIngredient copyIngredient(@NotNull RitualRecipeIngredient ingredient) {
        return ingredient;
    }

    @Nonnull
    @Override
    public String getErrorInfo(@Nullable RitualRecipeIngredient ingredient) {
        if (ingredient == null) {
            return "error ingredient: null";
        }
        return "[name: " + getDisplayName(ingredient) + ", quantity: " + ingredient.quantity() + ", offset: " + ingredient.offset() + "]";
    }
}
