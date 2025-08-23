package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record RitualRecipe(
        BlockState target,
        ItemStack trigger,
        List<RitualRecipeIngredient> ingredients,
        ItemStack result
) implements Recipe<RitualRecipeInput> {
    @Nonnull
    private List<Pair<Entity, Integer>> matchedIngredients(RitualRecipeInput input) {
        if (input.target() != target || !input.trigger().is(trigger.getItem()) || input.trigger().getCount() < trigger.getCount()) return List.of();

        BlockPos origin = input.origin();
        Level level = input.level();

        HashMap<BlockPos, List<Entity>> inputEntities = ingredientsByPos(origin, level);
        List<Pair<Entity, Integer>> matchedEntities = new ArrayList<>(inputEntities.size());

        Logger l = MythicRitual.LOGGER;
        l.debug("list of recipe ingredients:\n{}\nlist of ingredients entities:\n{}\npositions: {}, total entities: {}", ingredients, inputEntities, inputEntities.size(),
                inputEntities
                        .values()
                        .stream()
                        .mapToInt(List::size)
                        .sum());

        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos expected = origin.offset(ingredient.offset());

            if (!inputEntities.containsKey(expected) || inputEntities.get(expected).isEmpty()) {
                l.debug("position empty or missing: {}", expected);
                return List.of();
            }

            List<Entity> entitiesAt = inputEntities.get(expected);

            int index = -1;
            for (int i = 0; i < entitiesAt.size(); i++) {
                l.debug("testing ingredient {} with input {}", ingredient, entitiesAt.get(i));
                if (ingredient.test(entitiesAt.get(i))) {
                    l.debug("test passed removing entity {}", entitiesAt.get(i));
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                matchedEntities.add(new Pair<>(entitiesAt.get(index), ingredient instanceof ItemRitualRecipeIngredient i ? i.quantity() : 1));
                entitiesAt.remove(index);
                continue;
            }

            l.debug("inner loop finished no full match");

            return List.of();
        }

        l.debug("outer loop finished full match");

        return matchedEntities;
    }

    @Override
    public boolean matches(@NotNull RitualRecipeInput input, @NotNull Level level) {
        return !matchedIngredients(input).isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeInput input, HolderLookup.@NotNull Provider registries) {
        matchedIngredients(input).forEach(pair -> {
            Entity e = pair.getFirst();
            switch (e) {
                case ItemEntity i -> i.getItem().shrink(pair.getSecond());
                case LivingEntity l -> l.kill();
                default -> e.kill();
            }
        });
        return this.getResultItem(registries).copy();
    }

    @Contract(pure = true)
    @NotNull
    private HashMap<BlockPos, List<Entity>> ingredientsByPos(BlockPos origin, Level level) {
        HashMap<BlockPos, List<Entity>> out = new HashMap<>();
        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos target = origin.offset(ingredient.offset());
            if (out.containsKey(target)) continue;
            out.put(target, level.getEntities(null, new AABB(target)));
        }
        return out;
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
