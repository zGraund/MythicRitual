package com.github.zgraund.mythicritual.util;

import net.minecraft.advancements.critereon.MinMaxBounds;

import javax.annotation.Nonnull;

public class FormatUtils {
    public static String minMaxBoundToString(@Nonnull MinMaxBounds<?> bounds) {
        if (bounds.min().isPresent() && bounds.max().isPresent()) return bounds.min() + " - " + bounds.max();
        if (bounds.min().isPresent()) return bounds.min().get().toString();
        return "";
    }
}
