package com.github.zgraund.mythicritual.position;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum RelativeDirection implements StringRepresentable {
    LEFT(1),
    RIGHT(0),
    UP(3),
    DOWN(2),
    FORWARD(5),
    BACK(4);

    public static final Codec<RelativeDirection> CODEC = StringRepresentable.fromEnum(RelativeDirection::values);
    public static final RelativeDirection[] VALUES = RelativeDirection.values();
    private final String name;
    private final int oppositeIndex;

    RelativeDirection(int oppositeIndex) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.oppositeIndex = oppositeIndex;
    }

    public RelativeDirection getOpposite() {
        return VALUES[oppositeIndex];
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return name;
    }
}
