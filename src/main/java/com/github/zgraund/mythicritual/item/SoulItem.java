package com.github.zgraund.mythicritual.item;

import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.render.EntityPreviewTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class SoulItem extends Item {
    public SoulItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return hasEntity(stack);
    }

    @Override
    @Nonnull
    public Component getName(@Nonnull ItemStack stack) {
        if (hasEntity(stack)) return Component.translatable(getEntity(stack).getDescriptionId());
        return super.getName(stack);
    }

    @Override
    @Nonnull
    public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
        EntityType<?> type = stack.get(ModDataComponents.SOUL_ENTITY_TYPE);
        if (type != null && Screen.hasShiftDown()) {
            return Optional.of(new EntityPreviewTooltip(type, 90));
        }
        return super.getTooltipImage(stack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        if (hasEntity(stack) && !tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("item.mythicritual.soul.preview_text").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            if (tooltipFlag.isAdvanced()) {
                tooltipComponents.add(Component.literal(EntityType.getKey(getEntity(stack)).toString()).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Nonnull
    private EntityType<?> getEntity(@Nonnull ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SOUL_ENTITY_TYPE, EntityType.PIG);
    }

    private boolean hasEntity(@Nonnull ItemStack stack) {
        return stack.has(ModDataComponents.SOUL_ENTITY_TYPE);
    }
}
