package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class DestroyAltar implements ActionOnTransmute {
    @Override
    public void apply(@Nonnull RitualRecipeContext context, RitualRecipe recipe) {
        context.level().destroyBlock(context.origin(), false, context.player());
    }

    @Override
    public Component getDescription() {
        return Component.translatable(
                "info.hover.ritual.altar.general",
                Component.translatable("info.hover.ritual.action.destroy").withStyle(ChatFormatting.RED)
        ).withStyle(ChatFormatting.GRAY);
    }
}
