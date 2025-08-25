package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.github.zgraund.mythicritual.util.EntityUse;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record RitualRecipe(
        BlockState target,
        ItemStack trigger,
        List<RitualRecipeIngredient> ingredients,
        ItemStack result
) implements Recipe<RitualRecipeInput> {
    @Override
    public boolean matches(@NotNull RitualRecipeInput input, @NotNull Level level) {
        if (input.target() != target || !input.trigger().is(trigger.getItem()) || input.trigger().getCount() < trigger.getCount()) return false;

        HashMap<BlockPos, List<EntityUse>> inputEntities = input.entitiesByPosition(ingredients);
        if (inputEntities.isEmpty()) return false;

        Logger l = MythicRitual.LOGGER;
        l.debug("list of recipe ingredients:\n{}\nlist of ingredients entities:\n{}\npositions: {}, total entities: {}", ingredients, inputEntities, inputEntities.size(),
                inputEntities
                        .values()
                        .stream()
                        .mapToInt(List::size)
                        .sum());

        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos expected = input.origin().offset(ingredient.offset());

            List<EntityUse> entitiesAt = inputEntities.get(expected);

            Optional<EntityUse> match = entitiesAt.stream()
                                                  .peek(e -> l.debug("testing ingredient {} with input {}", ingredient, e.entity()))
                                                  .filter(ingredient::test)
                                                  .findFirst();

            if (match.isPresent()) {
                match.get().increment(ingredient.quantity());
                l.debug("test passed incrementing entity: {} by {}", match.get().entity(), ingredient.quantity());
                continue;
            }

            l.debug("inner loop finished no full match");
            return false;
        }

        l.debug("outer loop finished full match");

        input.commit(inputEntities);
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Nonnull
    public ItemStack consume(@NotNull RitualRecipeInput input) {
        input.toConsume().values().stream().flatMap(List::stream).forEach(EntityUse::consume);
        return assemble(input, input.level().registryAccess());
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
