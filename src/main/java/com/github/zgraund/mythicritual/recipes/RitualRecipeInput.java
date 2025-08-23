package com.github.zgraund.mythicritual.recipes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public record RitualRecipeInput(BlockState target, BlockPos origin, ItemStack trigger, Level level) implements RecipeInput {
    @Override
    @Nonnull
    public ItemStack getItem(int index) {
        if (index != 0) throw new IllegalArgumentException("No item for index " + index);
        return this.trigger();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
