package com.github.zgraund.mythicritual.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class ParticleUtils {
    public static void sendBreakParticles(@Nonnull Level level, BlockState blockState, BlockPos blockPos) {
        level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
    }
}
