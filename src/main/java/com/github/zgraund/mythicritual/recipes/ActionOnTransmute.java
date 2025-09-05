package com.github.zgraund.mythicritual.recipes;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ActionOnTransmute implements StringRepresentable {
    NONE("none"),
    CONSUME("consume"),
    DESTROY("destroy");

    public static final Codec<ActionOnTransmute> CODEC = StringRepresentable.fromEnum(ActionOnTransmute::values);
    private final String action;

    ActionOnTransmute(@NotNull String action) {this.action = action.toLowerCase(Locale.ROOT);}

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.action;
    }
}
