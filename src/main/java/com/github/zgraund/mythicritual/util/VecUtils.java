package com.github.zgraund.mythicritual.util;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import javax.annotation.Nonnull;

public class VecUtils {
    /**
     * Converts a relative offset vector into a new cardinal offset vector.
     */
    public static Vec3i toCardinal(Vec3i offset, @Nonnull Direction dir) {
        return switch (dir) {
            case NORTH -> new Vec3i(offset.getX(), offset.getY(), -offset.getZ());
            case EAST -> new Vec3i(offset.getZ(), offset.getY(), offset.getX());
            case SOUTH -> new Vec3i(-offset.getX(), offset.getY(), offset.getZ());
            case WEST -> new Vec3i(-offset.getZ(), offset.getY(), -offset.getX());

            case UP, DOWN -> {
                MythicRitual.LOGGER.warn("Cannot convert offset to cardinal using vertical direction {}", dir);
                yield offset;
            }
        };
    }
}
