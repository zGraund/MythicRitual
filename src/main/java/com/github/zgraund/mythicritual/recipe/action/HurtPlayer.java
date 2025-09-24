package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.damage.ModDamageTypes;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSources;

import javax.annotation.Nonnull;

public class HurtPlayer implements ActionOnTransmute {
    @Override
    public void apply(@Nonnull RitualRecipeContext context, RitualRecipe recipe) {
        if (context.player().isCreative()) return;
        context.player().hurt(new DamageSources(context.level().registryAccess()).source(ModDamageTypes.RITUAL_DAMAGE, context.player()), 2);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("info.hover.ritual.action.hurt").withStyle(ChatFormatting.RED);
    }
}
