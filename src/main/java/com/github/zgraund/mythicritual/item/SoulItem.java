package com.github.zgraund.mythicritual.item;

import com.github.zgraund.mythicritual.component.ModDataComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class SoulItem extends Item {
    public SoulItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget,
                                                  @NotNull InteractionHand usedHand) {
        player.getItemInHand(usedHand).set(ModDataComponent.SOUL_ENTITY_TYPE, interactionTarget.getType());
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return hasEntity(stack);
    }

    @Override
    @Nonnull
    public Component getName(@NotNull ItemStack stack) {
        if (hasEntity(stack)) return Component.translatable(getEntity(stack).getDescriptionId());
        return super.getName(stack);
    }

//    @Override
//    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
//        EntityType<?> type = stack.get(ModDataComponent.SOUL_ENTITY_TYPE);
//        if (type != null) {
//            return Optional.of(new EntityPreviewTooltip(type, 50));
//        }
//        return super.getTooltipImage(stack);
//    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        if (hasEntity(stack)) {
            tooltipComponents.add(Component.translatable("item.mythicritual.soul.preview_text").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Nonnull
    private EntityType<?> getEntity(@NotNull ItemStack stack) {
        return stack.getOrDefault(ModDataComponent.SOUL_ENTITY_TYPE, EntityType.PIG);
    }

    private boolean hasEntity(@NotNull ItemStack stack) {
        return stack.has(ModDataComponent.SOUL_ENTITY_TYPE);
    }
}
