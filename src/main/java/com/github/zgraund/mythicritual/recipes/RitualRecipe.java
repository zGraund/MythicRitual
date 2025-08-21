package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public record RitualRecipe(
        BlockState target,
        ItemStack trigger,
        List<RitualRecipeIngredient> ingredients,
        ItemStack result
) implements Recipe<RitualRecipeInput> {
    @Override
    public boolean matches(@NotNull RitualRecipeInput input, @NotNull Level level) {
        ItemStack inputTrigger = input.trigger();
        if (input.target() != target || !inputTrigger.is(trigger.getItem()) || inputTrigger.getCount() < trigger.getCount()) return false;
        // TODO: check rest of entity ingredient
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return this.result;
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RITUAL_RECIPE_SERIALIZER.get();
    }

    @Nonnull
    public RecipeType<?> getType() {
        return ModRecipes.RITUAL_RECIPE_TYPE.get();
    }
}
