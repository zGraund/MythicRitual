package com.github.zgraund.mythicritual.components;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface RitualOutput {
    /**
     * Spawn the crafting result in the world at the specified position.
     */
    void apply(RitualRecipeContext ctx, BlockPos pos);

    @Nonnull
    MapCodec<? extends RitualOutput> type();
}
