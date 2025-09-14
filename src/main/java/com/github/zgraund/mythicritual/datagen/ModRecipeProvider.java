package com.github.zgraund.mythicritual.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final Map<ResourceLocation, RecipeBuilder> ritualRecipes = new LinkedHashMap<>();

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput recipeOutput) {
        ritualRecipes.forEach((id, builder) -> builder.save(recipeOutput, id));
    }

    public void addRecipe(RecipeBuilder builder, ResourceLocation id) {ritualRecipes.put(id, builder);}
}
