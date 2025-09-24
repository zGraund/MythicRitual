package com.github.zgraund.mythicritual.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemUtils {
    public static void damageOrShrink(@Nonnull ItemStack item, int quantity, LivingEntity entity, @Nullable InteractionHand hand) {
        if (item.isDamageableItem()) item.hurtAndBreak(quantity, entity, LivingEntity.getSlotForHand(hand));
        else item.shrink(quantity);
    }
}
