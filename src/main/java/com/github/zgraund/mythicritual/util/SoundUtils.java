package com.github.zgraund.mythicritual.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoundUtils {
    public static void playBreakSound(Level level, @Nonnull BlockState blockState, BlockPos blockPos, @Nullable Player player) {
        Block block = blockState.getBlock();
        SoundEvent sound = block.getSoundType(blockState, level, blockPos, player).getBreakSound();
        playSound(level, blockPos, sound, SoundSource.BLOCKS);
    }

    public static void playPlaceSound(Level level, @Nonnull BlockState blockState, BlockPos blockPos, @Nullable Player player) {
        Block block = blockState.getBlock();
        SoundEvent sound = block.getSoundType(blockState, level, blockPos, player).getPlaceSound();
        playSound(level, blockPos, sound, SoundSource.BLOCKS);
    }

    public static void playSound(@Nonnull Level level, BlockPos blockPos, SoundEvent event, SoundSource source) {
        level.playSound(null, blockPos, event, source, 1, 1);
    }
}
