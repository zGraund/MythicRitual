package com.github.zgraund.mythicritual.recipes;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ActionOnCraft implements StringRepresentable {
    NONE("none"),
    CONSUME("consume"),
    DESTROY("destroy");

    public static final Codec<ActionOnCraft> CODEC = StringRepresentable.fromEnum(ActionOnCraft::values);
    private final String action;

    ActionOnCraft(@NotNull String action) {this.action = action.toLowerCase(Locale.ROOT);}

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.action;
    }
}
