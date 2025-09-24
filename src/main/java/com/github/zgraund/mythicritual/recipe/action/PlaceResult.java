package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.SoundUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class PlaceResult implements ActionOnTransmute {
    @Override
    public void apply(RitualRecipeContext context, @Nonnull RitualRecipe recipe) {
        if (recipe.result().asItemStack().getItem() instanceof BlockItem blockItem) {
            Actions.DESTROY_ALTAR.get().apply(context, recipe);
            SoundUtils.playPlaceSound(context.level(), context.altar(), context.origin(), context.player());
            BlockState blockState = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(context.useOn()));
            if (blockState == null) blockState = blockItem.getBlock().defaultBlockState();
            context.level().setBlockAndUpdate(context.origin(), blockState);
        } else {
            MythicRitual.LOGGER.warn("Cannot place result {} for recipe {}, not a placeable item.", recipe.result().asItemStack(), recipe);
            Actions.DROP_RESULT.get().apply(context, recipe);
        }
    }

    @Override
    public Component getDescription() {
        return Component.translatable(
                "info.hover.ritual.result.general",
                Component.translatable("info.hover.ritual.action.place").withStyle(ChatFormatting.YELLOW)
        ).withStyle(ChatFormatting.GRAY);
    }
}
