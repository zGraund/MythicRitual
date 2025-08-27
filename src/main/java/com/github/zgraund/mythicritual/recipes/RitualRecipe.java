package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.github.zgraund.mythicritual.util.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
        ItemStack result,
        List<ResourceKey<Level>> dimensions,
        EffectHelper effect,
        boolean needSky,
        boolean consumeTrigger
) implements Recipe<RitualRecipeContext> {
    @Override
    public boolean matches(@NotNull RitualRecipeContext context, @NotNull Level level) {
        Logger l = MythicRitual.LOGGER;
        if (!context.accept(this)) return false;
        HashMap<BlockPos, List<EntityConsumer>> inputEntities = context.entitiesByPosition(ingredients);
        if (inputEntities.isEmpty()) return false;

        l.debug("list of recipe ingredients:\n{}\nlist of ingredients entities:\n{}\npositions: {}, total entities: {}", ingredients, inputEntities, inputEntities.size(),
                inputEntities
                        .values()
                        .stream()
                        .mapToInt(List::size)
                        .sum());

        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos expected = RotationUtils.relativeTo(context.origin(), ingredient.offset(), context.player().getDirection());

            List<EntityConsumer> entitiesAt = inputEntities.get(expected);
            if (entitiesAt == null || entitiesAt.isEmpty()) {
                l.debug("position {} missing or empty", expected);
                return false;
            }

            Optional<EntityConsumer> match = entitiesAt.stream()
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

        context.commit(inputEntities);
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeContext context, HolderLookup.@NotNull Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Nonnull
    public ItemStack execute(@NotNull RitualRecipeContext context) {
        context.consume();
        if (consumeTrigger) context.shrink(trigger.getCount());
        context.player().swing(context.hand(), true);
        effect.apply((ServerLevel) context.level(), context.origin().above());
        return assemble(context, context.level().registryAccess());
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
