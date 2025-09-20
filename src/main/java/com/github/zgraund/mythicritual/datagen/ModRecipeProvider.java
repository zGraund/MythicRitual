package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.EffectHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput recipeOutput, @Nonnull HolderLookup.Provider provider) {
        // These are just some debug recipes used in development; they can be useful
        // as a reference for how to use the recipe builder
        HolderLookup.RegistryLookup<Enchantment> enchantments = provider.lookupOrThrow(Registries.ENCHANTMENT);

        // No catalyst and player as ingredient
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.GRASS_BLOCK)
                           .result(RitualIngredient.of(Items.BONE))
                           .addOfferings(RitualIngredient.of(EntityType.PLAYER))
                           .effect(EffectHelper.LIGHTNING)
                           .save(recipeOutput, MythicRitual.ID("sacrifice_player"));

        // Tool as catalyst with enchants, action: destroy catalyst
        ItemStack axe = new ItemStack(Items.DIAMOND_AXE);
        axe.enchant(enchantments.getOrThrow(Enchantments.UNBREAKING), 3);
        axe.enchant(enchantments.getOrThrow(Enchantments.SHARPNESS), 5);
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.NETHER_BRICKS)
                           .result(RitualIngredient.of(Items.BLAZE_ROD))
                           .catalyst(RitualIngredient.of(axe))
                           .addOfferings(RitualIngredient.of(Items.NETHER_STAR))
                           .onTransmute(ActionOnTransmute.DESTROY_AND_DROP)
                           .save(recipeOutput, MythicRitual.ID("axe_unbreakable"));

        // Tool without components, action: use 1 durability
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.RED_NETHER_BRICKS)
                           .result(RitualIngredient.of(Items.BLAZE_POWDER))
                           .catalyst(RitualIngredient.of(Items.DIAMOND_AXE))
                           .addOfferings(RitualIngredient.of(Items.NETHER_STAR))
                           .onTransmute(ActionOnTransmute.CONSUME_AND_DROP)
                           .save(recipeOutput, MythicRitual.ID("axe_normal"));

        // Craft and place an anvil on the altar
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.IRON_BLOCK)
                           .result(RitualIngredient.of(Items.ANVIL))
                           .addOfferings(RitualIngredient.of(2, Items.IRON_BLOCK), RitualIngredient.of(4, Items.IRON_INGOT))
                           .effect(EffectHelper.PARTICLES)
                           .catalyst(RitualIngredient.of(Items.STICK))
                           .onTransmute(ActionOnTransmute.CONSUME_AND_PLACE)
                           .save(recipeOutput, MythicRitual.ID("anvil_in_place"));

        // The famous recipe dirt -> diamond
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.DIRT)
                           .result(RitualIngredient.of(Items.DIAMOND_BLOCK))
                           .effect(EffectHelper.LIGHTNING)
                           .onTransmute(ActionOnTransmute.KEEP_AND_PLACE)
                           .save(recipeOutput, MythicRitual.ID("best_recipe"));

        // Destroy the catalyst and place the result
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.ANCIENT_DEBRIS)
                           .result(RitualIngredient.of(Items.RED_BED))
                           .catalyst(RitualIngredient.of(Items.GOLD_INGOT))
                           .dimensions(Level.NETHER)
                           .onTransmute(ActionOnTransmute.DESTROY_AND_PLACE)
                           .save(recipeOutput, MythicRitual.ID("whats_this"));

        // Test placing of a tile-entity
        RitualRecipeHelpers.builder(provider)
                           .altar(Blocks.OAK_LOG)
                           .result(RitualIngredient.of(Items.CHEST))
                           .onTransmute(ActionOnTransmute.DESTROY_AND_PLACE)
                           .save(recipeOutput, MythicRitual.ID("chest"));
    }
}
