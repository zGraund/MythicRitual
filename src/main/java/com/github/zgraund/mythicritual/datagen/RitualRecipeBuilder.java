package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.EffectHelper;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RitualRecipeBuilder implements RecipeBuilder {
    private final BlockState altar;
    private final RitualIngredient result;
    private final Map<Vec3i, List<RitualIngredient>> locations = new HashMap<>();
    private RitualIngredient catalyst = RitualIngredient.EMPTY;
    private List<ResourceKey<Level>> dimensions = List.of();
    private HolderSet<Biome> biomes = HolderSet.empty();
    private EffectHelper effect = EffectHelper.NONE;
    private ActionOnTransmute onTransmute = ActionOnTransmute.NONE;
    private boolean needSky = false;

    public RitualRecipeBuilder(BlockState altar, RitualIngredient result) {
        this.altar = altar;
        this.result = result;
    }

    @Nonnull
    @Override
    public RecipeBuilder unlockedBy(@Nonnull String name, @Nonnull Criterion<?> criterion) {
        return this;
    }

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

    public RitualRecipeBuilder catalyst(RitualIngredient catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    public RitualRecipeBuilder addLocations(Vec3i offset, List<RitualIngredient> locations) {
        this.locations.computeIfAbsent(offset, vec3i -> new ArrayList<>()).addAll(locations);
        return this;
    }

    public RitualRecipeBuilder addLocations(List<RitualIngredient> locations) {
        return addLocations(new Vec3i(0, 1, 0), locations);
    }

    public RitualRecipeBuilder dimensions(List<ResourceKey<Level>> dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public RitualRecipeBuilder biomes(HolderSet<Biome> biomes) {
        this.biomes = biomes;
        return this;
    }

    public RitualRecipeBuilder effect(EffectHelper effect) {
        this.effect = effect;
        return this;
    }

    public RitualRecipeBuilder onTransmute(ActionOnTransmute onTransmute) {
        this.onTransmute = onTransmute;
        return this;
    }

    public RitualRecipeBuilder needSky(boolean needSky) {
        this.needSky = needSky;
        return this;
    }
}
