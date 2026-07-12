package com.github.zgraund.mythicritual.components;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface RitualInput {
    /**
     * Test if the clicked target provided by the recipe context is valid.
     * The target can be either a {@linkplain BlockState} or an {@linkplain Entity}
     */
    boolean testTarget(RitualRecipeContext ctx);

    /**
     * Test a specific position on the recipe context.
     */
    boolean testPos(RitualRecipeContext ctx, BlockPos pos);

    void apply(RitualRecipeContext ctx, BlockPos pos);

    @Nonnull
    MapCodec<? extends RitualInput> type();
}
