package com.github.zgraund.mythicritual.api.postaction;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface RitualPostAction {
    void apply(RitualRecipeContext ctx);
}
