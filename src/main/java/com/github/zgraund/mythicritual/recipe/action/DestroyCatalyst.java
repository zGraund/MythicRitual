package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class DestroyCatalyst implements ActionOnTransmute {
    @Override
    public void apply(@Nonnull RitualRecipeContext context, RitualRecipe recipe) {
        if (context.player().isCreative()) return;
        context.catalyst().setDamageValue(context.catalyst().getMaxDamage());
        ItemUtils.damageOrShrink(context.catalyst(), 0, context.player(), context.hand());
    }

    @Override
    public Component getDescription() {
        return Component.translatable(
                "info.hover.ritual.catalyst.general",
                Component.translatable("info.hover.ritual.action.destroy").withStyle(ChatFormatting.RED)
        ).withStyle(ChatFormatting.GRAY);
    }
}
