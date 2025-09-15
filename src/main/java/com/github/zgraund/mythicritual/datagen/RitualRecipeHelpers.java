package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.EffectHelper;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Builder for custom {@link RitualRecipe} JSONs during datagen.
 * <p>
 * Use this inside your mod’s {@link RecipeProvider#buildRecipes(RecipeOutput, HolderLookup.Provider)}.
 * The {@link HolderLookup.Provider} passed to that method must be passed into {@link #builder(HolderLookup.Provider)}.
 * <p>
 *
 * <p>
 * The {@link RitualRecipeHelpers#fromTag} helper methods are only meant to be used in data generation.
 * </p>
 *
 * <h3>Example Usage</h3>
 *
 * <pre>{@code
 * RitualRecipeHelpers.builder(provider)
 *     .altar(Blocks.NETHERITE_BLOCK) // required
 *     .result(RitualIngredient.of(EntityType.WITHER_SKELETON)) // required
 *
 *     // optional fields
 *     .catalyst(RitualRecipeHelpers.fromTag(ItemTags.SWORDS))
 *     .addOfferings(
 *         RitualIngredient.of(5, Items.DIAMOND, Items.EMERALD),
 *         RitualIngredient.of(10, Items.GOLD_INGOT)
 *     )
 *     .addOfferings(new Vec3i(1, 0, 0), RitualIngredient.of(EntityType.ZOMBIE))
 *     .dimensions(Level.OVERWORLD)
 *     .biomes(Biomes.TAIGA)
 *     .effect(EffectHelper.LIGHTNING)
 *     .onTransmute(ActionOnTransmute.DESTROY)
 *     .needSky(true)
 *
 *     // finalize and write JSON
 *     .save(recipeOutput, ResourceLocation.parse("MOD_ID", "recipe_id"));
 * }</pre>
 */
@SuppressWarnings("unused")
public class RitualRecipeHelpers {
    @Nonnull
    @Contract("_ -> new")
    public static RequireAltar builder(HolderLookup.Provider provider) {return new Builder(provider);}

    @Nonnull
    public static RitualIngredient fromTag(TagKey<Item> tag) {return fromTag(tag, 1);}

    @Nonnull
    public static RitualIngredient fromTag(TagKey<Item> tag, int quantity) {return fromTag(tag, DataComponentPredicate.EMPTY, quantity);}

    @SuppressWarnings("deprecation")
    @Nonnull
    public static RitualIngredient fromTag(TagKey<Item> tag, DataComponentPredicate components, int quantity) {
        return new RitualIngredient(HolderSet.emptyNamed(BuiltInRegistries.ITEM.holderOwner(), tag), components, quantity);
    }

    public interface RequireAltar {
        RequireResult altar(Block altar);
    }

    public interface RequireResult {
        Builder result(RitualIngredient result);
    }

    public static class Builder implements RecipeBuilder, RequireAltar, RequireResult {
        private static final Vec3i DEFAULT_OFFSET = new Vec3i(0, 1, 0);
        private final HolderLookup.Provider provider;
        private final Map<Vec3i, List<RitualIngredient>> locations = new HashMap<>();
        private BlockState altar;
        private RitualIngredient result;
        private RitualIngredient catalyst = RitualIngredient.EMPTY;
        private List<ResourceKey<Level>> dimensions = List.of();
        private HolderSet<Biome> biomes = HolderSet.empty();
        private EffectHelper effect = EffectHelper.NONE;
        private ActionOnTransmute onTransmute = ActionOnTransmute.NONE;
        private boolean needSky = false;

        private Builder(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        @Nonnull
        @Override
        public RecipeBuilder unlockedBy(@Nonnull String name, @Nonnull Criterion<?> criterion) {return this;} // no-op

        @Nonnull
        @Override
        public RecipeBuilder group(@Nullable String groupName) {return this;} // no-op

        @Nonnull
        @Override
        public Item getResult() {
            return result.asItemStack().getItem();
        }

        @Override
        public void save(@Nonnull RecipeOutput recipeOutput, @Nonnull ResourceLocation id) {
            RitualRecipe recipe = new RitualRecipe(altar, catalyst, locations, result, dimensions, biomes, effect, onTransmute, needSky);
            recipeOutput.accept(id, recipe, null);
        }

        @Override
        public RequireResult altar(@Nonnull Block altar) {
            this.altar = altar.defaultBlockState();
            return this;
        }

        @Override
        public Builder result(RitualIngredient result) {
            this.result = result;
            return this;
        }

        public Builder catalyst(RitualIngredient catalyst) {
            this.catalyst = catalyst;
            return this;
        }

        public Builder addOfferings(RitualIngredient... locations) {
            return addOfferings(DEFAULT_OFFSET, locations);
        }

        public Builder addOfferings(Vec3i offset, RitualIngredient... locations) {
            this.locations.computeIfAbsent(offset, vec3i -> new ArrayList<>()).addAll(List.of(locations));
            return this;
        }

        @SafeVarargs
        public final Builder dimensions(ResourceKey<Level>... dimensions) {
            this.dimensions = List.of(dimensions);
            return this;
        }

        @SafeVarargs
        public final Builder biomes(ResourceKey<Biome>... biomes) {
            HolderLookup.RegistryLookup<Biome> lookup = provider.lookupOrThrow(Registries.BIOME);
            this.biomes = HolderSet.direct(Arrays.stream(biomes).map(lookup::getOrThrow).toList());
            return this;
        }

        public Builder biomes(TagKey<Biome> tag) {
            this.biomes = provider.lookupOrThrow(Registries.BIOME).getOrThrow(tag);
            return this;
        }

        public Builder effect(EffectHelper effect) {
            this.effect = effect;
            return this;
        }

        public Builder onTransmute(ActionOnTransmute onTransmute) {
            this.onTransmute = onTransmute;
            return this;
        }

        public Builder needSky(boolean needSky) {
            this.needSky = needSky;
            return this;
        }
    }

}
