package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class DamageCatalyst implements ActionOnTransmute {
    @Override
    public void apply(@Nonnull RitualRecipeContext context, RitualRecipe recipe) {
        if (context.player().isCreative()) return;
//        ItemUtils.damageOrShrink(context.catalyst(), recipe.catalyst().count(), context.player(), context.hand());
        // FIXME: find a way to do this
//        ItemUtils.damageOrShrink(context.catalyst(), recipe.conditions().getCatalyst().items().get().count().min().get(), context.player(), context.hand());
        ItemUtils.damageOrShrink(context.catalyst(), 1, context.player(), context.hand());
    }

    boolean t() {return false;}

    @Override
    public Component getDescription() {
        return Component.translatable(
                "info.hover.ritual.catalyst.general",
                Component.translatable("info.hover.ritual.action.damage").withStyle(ChatFormatting.YELLOW)
        ).withStyle(ChatFormatting.GRAY);
    }
}
