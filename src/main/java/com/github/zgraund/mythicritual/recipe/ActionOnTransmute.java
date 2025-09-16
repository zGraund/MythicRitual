package com.github.zgraund.mythicritual.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ActionOnTransmute implements StringRepresentable {
    NONE("none"),
    CONSUME("consume"),
    DESTROY("destroy");

    public static final Codec<ActionOnTransmute> CODEC = StringRepresentable.fromEnum(ActionOnTransmute::values);
    private final String action;

    ActionOnTransmute(@Nonnull String action) {this.action = action.toLowerCase(Locale.ROOT);}

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.action;
    }
}
