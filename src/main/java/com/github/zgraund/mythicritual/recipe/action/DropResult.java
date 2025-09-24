package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class DropResult implements ActionOnTransmute {
    @Override
    public void apply(RitualRecipeContext context, @Nonnull RitualRecipe recipe) {
        Entity entity = recipe.getResultEntity(context);
        if (entity == null) {
            MythicRitual.LOGGER.warn("Cannot get entity to drop in recipe: {}.", recipe);
            return;
        }

        entity.setPos(context.origin().above().getCenter());
        entity.setDeltaMovement(0, 0.20, 0);
        if (entity instanceof LivingEntity living) {
            float entityDirection = context.player().getDirection().getOpposite().toYRot();
            living.setYRot(entityDirection);
            living.setYBodyRot(entityDirection);
            living.setYHeadRot(entityDirection);
            living.setDeltaMovement(0, 0, 0);
        }
        context.level().addFreshEntity(entity);
    }

    @Override
    public Component getDescription() {
        return Component.translatable(
                "info.hover.ritual.result.general",
                Component.translatable("info.hover.ritual.action.drop").withStyle(ChatFormatting.GREEN)
        ).withStyle(ChatFormatting.GRAY);
    }
}
