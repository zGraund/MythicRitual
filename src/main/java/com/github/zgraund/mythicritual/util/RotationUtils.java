package com.github.zgraund.mythicritual.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class RotationUtils {

    /**
     * Rotates, relative to North, around Y axis according to a facing direction.
     * Only handles horizontal facings (NORTH, SOUTH, EAST, WEST).
     */
    public static Vec3i rotate(Vec3i offset, @NotNull Direction facing) {
        return switch (facing) {
            case SOUTH -> new Vec3i(-offset.getX(), offset.getY(), -offset.getZ());
            case WEST -> new Vec3i(offset.getZ(), offset.getY(), -offset.getX());
            case EAST -> new Vec3i(-offset.getZ(), offset.getY(), offset.getX());
            default -> offset;
        };
    }

    /**
     * Computes the rotated absolute position relative to an anchor.
     */
    @Nonnull
    public static BlockPos relativeTo(@NotNull BlockPos anchor, Vec3i offset, Direction facing) {
        return anchor.offset(rotate(offset, facing));
    }
}
