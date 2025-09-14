package com.github.zgraund.mythicritual.util;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OffsetHelpers {
    @Nonnull
    public static MutableComponent asComponent(@Nonnull Vec3i offset) {
        List<String> out = new ArrayList<>();
        int x = offset.getX();
        int y = offset.getY();
        int z = offset.getZ();
        if (x != 0) out.add(x < 0 ? "⬅ " + Math.abs(x) : "➡ " + x);
        if (y != 0) out.add(y < 0 ? "⬇ " + Math.abs(y) : "⬆ " + y);
        if (z != 0) out.add(z < 0 ? "⬈ " + Math.abs(z) : "⬋ " + z);
        return Component.literal(String.join(", ", out));
    }

    public static boolean isDefault(@Nonnull Vec3i offset) {
        return offset.equals(new Vec3i(0, 1, 0));
    }
}
