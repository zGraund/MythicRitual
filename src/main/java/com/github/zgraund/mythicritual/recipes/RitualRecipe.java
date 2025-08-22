package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
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
import java.util.HashMap;
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
        HashMap<BlockPos, List<Entity>> inputIngredients = ingredientsByPos(input.pos(), level);

        Logger l = MythicRitual.LOGGER;
        l.debug("list of recipe ingredients:\n{}\nlist of ingredients entities:\n{}\npositions: {}, total entities: {}", ingredients, inputIngredients, inputIngredients.size(),
                inputIngredients
                        .values()
                        .stream()
                        .mapToInt(List::size)
                        .sum());

        here:
        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos expectedPos = input.pos().offset(ingredient.offset());
            if (!inputIngredients.containsKey(expectedPos) || inputIngredients.get(expectedPos).isEmpty()) {
                l.debug("position empty or missing: {}", expectedPos);
                return false;
            }
            List<Entity> entityAt = inputIngredients.get(expectedPos);
            for (int i = 0; i < entityAt.size(); i++) {
                l.debug("testing ingredient {} with input {}", ingredient, entityAt.get(i));
                if (ingredient.test(entityAt.get(i))) {
                    l.debug("test passed removing entity {}", entityAt.get(i));
                    entityAt.remove(i);
                    continue here;
                }
            }
            l.debug("inner loop finished no full match");
            return false;
        }
        l.debug("outer loop finished full match");
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Contract(pure = true)
    private @NotNull HashMap<BlockPos, List<Entity>> ingredientsByPos(BlockPos origin, Level level) {
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
