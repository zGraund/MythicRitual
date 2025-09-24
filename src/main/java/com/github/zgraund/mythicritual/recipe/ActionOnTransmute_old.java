package com.github.zgraund.mythicritual.recipe;

import com.github.zgraund.mythicritual.util.SoundUtils;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public enum ActionOnTransmute_old implements StringRepresentable {
    // Catalyst actions
    KEEP_CATALYST("keep_catalyst") {
        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.catalyst.general",
                    Component.translatable("info.hover.ritual.action.preserve").withStyle(ChatFormatting.GREEN)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    DAMAGE_CATALYST("damage_catalyst") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (context.player().isCreative()) return;
            this.damageOrShrink(context.catalyst(), recipe.catalyst().count(), context.player(), context.hand());
        }

        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.catalyst.general",
                    Component.translatable("info.hover.ritual.action.damage").withStyle(ChatFormatting.YELLOW)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    DESTROY_CATALYST("destroy_catalyst") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (context.player().isCreative()) return;
            context.catalyst().setDamageValue(context.catalyst().getMaxDamage());
            this.damageOrShrink(context.catalyst(), 0, context.player(), context.hand());
        }

        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.catalyst.general",
                    Component.translatable("info.hover.ritual.action.destroy").withStyle(ChatFormatting.RED)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    // Altar actions
    KEEP_ALTAR("keep_altar") {
        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.altar.general",
                    Component.translatable("info.hover.ritual.action.preserve").withStyle(ChatFormatting.GREEN)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    DESTROY_ALTAR("destroy_altar") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            context.level().destroyBlock(context.origin(), false, context.player());
        }

        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.altar.general",
                    Component.translatable("info.hover.ritual.action.destroy").withStyle(ChatFormatting.RED)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    // Result actions
    DROP_RESULT("drop_result") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            Entity entity = recipe.getResultEntity(context);
            if (entity == null) return;

            entity.setPos(context.origin().above().getCenter());
            entity.setDeltaMovement(0, 0.20, 0);
            if (entity instanceof LivingEntity living) {
                Direction entityDirection = context.player().getDirection().getOpposite();
                living.setYRot(entityDirection.toYRot());
                living.setYBodyRot(entityDirection.toYRot());
                living.setYHeadRot(entityDirection.toYRot());
                living.setDeltaMovement(0, 0, 0);
            }
            context.level().addFreshEntity(entity);
        }

        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.result.general",
                    Component.translatable("info.hover.ritual.action.drop").withStyle(ChatFormatting.GREEN)
            ).withStyle(ChatFormatting.GRAY);
        }
    },
    PLACE_RESULT("place_result") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (recipe.result().asItemStack().getItem() instanceof BlockItem blockItem) {
                DESTROY_ALTAR.apply(context, recipe);
                SoundUtils.playPlaceSound(context.level(), context.altar(), context.origin(), context.player());
                BlockState blockState = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(context.useOn()));
                if (blockState == null) blockState = blockItem.getBlock().defaultBlockState();
                context.level().setBlockAndUpdate(context.origin(), blockState);
            } else {
                DROP_RESULT.apply(context, recipe);
            }
        }

        @Override
        public Component description() {
            return Component.translatable(
                    "info.hover.ritual.result.general",
                    Component.translatable("info.hover.ritual.action.place").withStyle(ChatFormatting.YELLOW)
            ).withStyle(ChatFormatting.GRAY);
        }
    };

    public static final Codec<ActionOnTransmute_old> CODEC = StringRepresentable.fromEnum(ActionOnTransmute_old::values);

    public static final List<ActionOnTransmute_old> DEFAULT = List.of(KEEP_CATALYST, KEEP_ALTAR, DROP_RESULT);
    public static final List<ActionOnTransmute_old> CONSUME_AND_DROP = List.of(DAMAGE_CATALYST, KEEP_ALTAR, DROP_RESULT);
    public static final List<ActionOnTransmute_old> DESTROY_AND_DROP = List.of(DESTROY_CATALYST, KEEP_ALTAR, DROP_RESULT);
    public static final List<ActionOnTransmute_old> KEEP_AND_PLACE = List.of(KEEP_CATALYST, DESTROY_ALTAR, PLACE_RESULT);
    public static final List<ActionOnTransmute_old> CONSUME_AND_PLACE = List.of(DAMAGE_CATALYST, DESTROY_ALTAR, PLACE_RESULT);
    public static final List<ActionOnTransmute_old> DESTROY_AND_PLACE = List.of(DESTROY_CATALYST, DESTROY_ALTAR, PLACE_RESULT);

    @Nonnull
    private final String action;

    ActionOnTransmute_old(@Nonnull String action) {this.action = action.toLowerCase(Locale.ROOT);}

    @Override
    @Nonnull
    public final String getSerializedName() {return this.action;}

    public void apply(RitualRecipeContext context, RitualRecipe recipe) {} // no-op

    public abstract Component description();

    public final void damageOrShrink(@Nonnull ItemStack item, int quantity, Player player, @Nullable InteractionHand hand) {
        if (item.isDamageableItem()) item.hurtAndBreak(quantity, player, LivingEntity.getSlotForHand(hand));
        else item.shrink(quantity);
    }
}
