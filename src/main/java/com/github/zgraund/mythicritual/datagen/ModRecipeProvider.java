package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.EffectHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput recipeOutput, @Nonnull HolderLookup.Provider provider) {
        // This recipe datagen is only here as an example and nothing should be here on release.
        RitualRecipeHelpers
                .builder(provider)
                .altar(Blocks.GRASS_BLOCK)
                .result(RitualIngredient.of(Items.BONE))
                .addOfferings(RitualIngredient.of(EntityType.PLAYER))
                .effect(EffectHelper.LIGHTNING)
                .save(recipeOutput, MythicRitual.ID("suicide"));
    }
}
