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
        if (x != 0) out.add(x < 0 ? "\uD83E\uDC70 " + Math.abs(x) : "\uD83E\uDC72 " + x); // Left    - Right arrows
        if (y != 0) out.add(y < 0 ? "\uD83E\uDC73 " + Math.abs(y) : "\uD83E\uDC71 " + y); // Up      - Down  arrows
        if (z != 0) out.add(z < 0 ? "\uD83E\uDC75 " + Math.abs(z) : "\uD83E\uDC77 " + z); // Forward - Back  arrows
        return Component.literal("Position: " + String.join(", ", out));
    }

    public static boolean isDefault(@Nonnull Vec3i offset) {
        return offset.equals(new Vec3i(0, 1, 0));
    }
}
