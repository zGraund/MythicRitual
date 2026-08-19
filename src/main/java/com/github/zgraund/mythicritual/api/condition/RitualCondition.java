package com.github.zgraund.mythicritual.api.condition;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface RitualCondition {
    boolean test(RitualRecipeContext ctx);

    default void onApply(RitualRecipeContext ctx) {
    }
}
