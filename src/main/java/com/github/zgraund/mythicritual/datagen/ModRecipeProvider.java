package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.condition.*;
import com.github.zgraund.mythicritual.util.predicate.ClientLocationPredicate;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput recipeOutput, @Nonnull HolderLookup.Provider provider) {
        // These are just some debug recipes used in development; they can be useful
        // as a reference for how to use the recipe builder
        HolderLookup.RegistryLookup<Enchantment> enchantmentsLookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        HolderLookup.RegistryLookup<Biome> biomesLookup = provider.lookupOrThrow(Registries.BIOME);

        RitualRecipeHelpers.builder(provider).result(RitualIngredient.of(Items.DIAMOND_BLOCK)).addConditions(
                new Altar(Optional.of(BlockPredicate.Builder.block().of(BlockTags.WOOL).build()), Optional.empty()),
                new Catalyst(Optional.of(Catalyst.EMPTY_HAND), Optional.of("spade seee")),
                new Location(Optional.of(ClientLocationPredicate
                        .builder()
                        .setBiomes(biomesLookup.getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS))
                        .setDimensions(List.of(Level.OVERWORLD, Level.NETHER))
                        .setCanSeeSk(true)
                        .setLight(MinMaxBounds.Ints.between(1, 12))
                        .setSmokey(false)
                        .build()), Optional.of("la location seee")),
                new Time(Optional.of(MinMaxBounds.Ints.between(690, 42000)), Optional.of("nice")),
                new Weather(Optional.of(Weather.Conditions.RAIN), Optional.of("piove meloni ladro"))
        ).save(recipeOutput, MythicRitual.id("test_new_system"));

        // No catalyst and player as ingredient
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.GRASS_BLOCK)
//                           .result(RitualIngredient.of(Items.BONE))
//                           .addOfferings(RitualIngredient.of(EntityType.PLAYER))
//                           .effect(EffectHelper.LIGHTNING)
//                           .save(recipeOutput, MythicRitual.id("sacrifice_player"));
//
//        // Tool as catalyst with enchants, action: destroy catalyst
//        ItemStack axe = new ItemStack(Items.DIAMOND_AXE);
//        axe.enchant(enchantmentsLookup.getOrThrow(Enchantments.UNBREAKING), 3);
//        axe.enchant(enchantmentsLookup.getOrThrow(Enchantments.SHARPNESS), 5);
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.NETHER_BRICKS)
//                           .result(RitualIngredient.of(Items.BLAZE_ROD))
//                           .catalyst(RitualIngredient.of(axe))
//                           .addOfferings(RitualIngredient.of(Items.NETHER_STAR))
//                           .onTransmute(Actions.DESTROY_CATALYST, Actions.DROP_RESULT)
//                           .save(recipeOutput, MythicRitual.id("axe_unbreakable"));
//
//        // Tool without components, action: use 1 durability
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.RED_NETHER_BRICKS)
//                           .result(RitualIngredient.of(Items.BLAZE_POWDER))
//                           .catalyst(RitualIngredient.of(Items.DIAMOND_AXE))
//                           .addOfferings(RitualIngredient.of(Items.NETHER_STAR))
//                           .onTransmute(Actions.DAMAGE_CATALYST, Actions.DROP_RESULT)
//                           .save(recipeOutput, MythicRitual.id("axe_normal"));
//
//        // Craft and place an anvil on the altar
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.IRON_BLOCK)
//                           .result(RitualIngredient.of(Items.ANVIL))
//                           .addOfferings(RitualIngredient.of(2, Items.IRON_BLOCK), RitualIngredient.of(4, Items.IRON_INGOT))
//                           .effect(EffectHelper.PARTICLES)
//                           .catalyst(RitualIngredient.of(Items.STICK))
//                           .onTransmute(Actions.DAMAGE_CATALYST, Actions.PLACE_RESULT)
//                           .save(recipeOutput, MythicRitual.id("anvil_in_place"));
//
//        // The famous recipe dirt -> diamond
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.DIRT)
//                           .result(RitualIngredient.of(Items.DIAMOND_BLOCK))
//                           .effect(EffectHelper.LIGHTNING)
//                           .onTransmute(Actions.PLACE_RESULT)
//                           .save(recipeOutput, MythicRitual.id("best_recipe"));
//
//        // Destroy the catalyst and place the result
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.ANCIENT_DEBRIS)
//                           .result(RitualIngredient.of(Items.RED_BED))
//                           .catalyst(RitualIngredient.of(Items.GOLD_INGOT))
//                           .dimensions(Level.NETHER)
//                           .onTransmute(Actions.DESTROY_CATALYST, Actions.PLACE_RESULT)
//                           .save(recipeOutput, MythicRitual.id("whats_this"));
//
//        // Test placing of a tile-entity
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.OAK_LOG)
//                           .result(RitualIngredient.of(Items.CHEST))
//                           .onTransmute(Actions.DESTROY_CATALYST, Actions.PLACE_RESULT)
//                           .save(recipeOutput, MythicRitual.id("chest"));
//
//        // Entity as ingredient
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.BEACON)
//                           .result(RitualIngredient.of(EntityType.VILLAGER))
//                           .catalyst(RitualIngredient.of(Items.GOLDEN_APPLE))
//                           .addOfferings(RitualIngredient.of(EntityType.ZOMBIE))
//                           .onTransmute(Actions.DAMAGE_CATALYST, Actions.DROP_RESULT)
//                           .save(recipeOutput, MythicRitual.id("villager_conversion"));
//
//        // Test the EventHooks#finalizeMobSpawn
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.MOSSY_COBBLESTONE)
//                           .result(RitualIngredient.of(EntityType.ZOMBIE))
//                           .catalyst(RitualIngredient.of(Items.DEBUG_STICK))
//                           .addOfferings(RitualIngredient.of(8, Items.ROTTEN_FLESH))
//                           .onTransmute(Actions.HURT_PLAYER, Actions.DROP_RESULT)
//                           .save(recipeOutput, MythicRitual.id("zombie_army"));
//
//        // Ender Dragon sword
//        ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
//        enchants.set(enchantmentsLookup.getOrThrow(Enchantments.SHARPNESS), 255);
//        enchants.set(enchantmentsLookup.getOrThrow(Enchantments.UNBREAKING), 255);
//        enchants.set(enchantmentsLookup.getOrThrow(Enchantments.LOOTING), 255);
//        enchants.set(enchantmentsLookup.getOrThrow(Enchantments.MENDING), 1);
//        DataComponentPredicate dragonSwordComponents = DataComponentPredicate.builder().expect(DataComponents.ENCHANTMENTS, enchants.toImmutable()).build();
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.BEDROCK)
//                           .result(RitualIngredient.of(1, dragonSwordComponents, Items.NETHERITE_SWORD))
//                           .catalyst(RitualIngredient.of(Items.NETHERITE_SWORD))
//                           .addOfferings(RitualIngredient.of(EntityType.ENDER_DRAGON))
//                           .onTransmute(Actions.DESTROY_CATALYST, Actions.DROP_RESULT)
//                           .save(recipeOutput, MythicRitual.id("dragon_sword"));
//
//        // Test the "ghost" placing when using a block as catalyst
//        RitualRecipeHelpers.builder(provider)
//                           .altar(Blocks.DIRT)
//                           .result(RitualIngredient.of(Items.DIAMOND))
//                           .catalyst(RitualIngredient.of(Items.DIRT))
//                           .save(recipeOutput, MythicRitual.id("ghost_block"));
    }
}
