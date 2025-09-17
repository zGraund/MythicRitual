package com.github.zgraund.mythicritual.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Optional;

public enum ActionOnTransmute implements StringRepresentable {
    //    NONE("none") {
//        @Override
//        public void apply(RitualRecipeContext context, RitualRecipe recipe) {}
//    },
    CONSUME_CATALYST("consume_catalyst") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (context.player().isCreative()) return;
            this.damageOrShrink(context.catalyst(), recipe.catalyst().count(), context.player(), context.hand());
        }

        @Override
        public Optional<Component> description() {
            return Optional.of(Component.translatable("info.hover.ritual.consume.catalyst").withStyle(ChatFormatting.YELLOW));
        }
    },
    DESTROY_CATALYST("destroy_catalyst") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (context.player().isCreative()) return;
            context.catalyst().setDamageValue(context.catalyst().getMaxDamage());
            this.damageOrShrink(context.catalyst(), context.catalyst().getMaxDamage(), context.player(), context.hand());
        }

        @Override
        public Optional<Component> description() {
            return Optional.of(Component.translatable("info.hover.ritual.destroy.catalyst").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }
    },
    DESTROY_ALTAR("destroy_altar") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            context.level().removeBlock(context.origin(), false);
        }

        @Override
        public Optional<Component> description() {
            return Optional.of(Component.translatable("info.hover.ritual.destroy.altar"));
        }
    },
    PLACE_RESULT("place_result") {
        @Override
        public void apply(RitualRecipeContext context, RitualRecipe recipe) {
            if (recipe.result().asItemStack().getItem() instanceof BlockItem blockItem) {
                // TODO: this is not safe check if the facing exist before setting
                DESTROY_ALTAR.apply(context, recipe);
                BlockState blockState = blockItem.getBlock().defaultBlockState()
                                                 .setValue(BlockStateProperties.HORIZONTAL_FACING, context.player().getDirection().getOpposite());
                context.level().setBlockAndUpdate(context.origin(), blockState);
            }
        }
    };

    public static final Codec<ActionOnTransmute> CODEC = StringRepresentable.fromEnum(ActionOnTransmute::values);
    private final String action;

    ActionOnTransmute(@Nonnull String action) {this.action = action.toLowerCase(Locale.ROOT);}

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.action;
    }

    public final void damageOrShrink(@Nonnull ItemStack item, int quantity, Player player, InteractionHand hand) {
        if (item.isDamageableItem()) item.hurtAndBreak(quantity, player, LivingEntity.getSlotForHand(hand));
        else item.shrink(quantity);
    }

    public abstract void apply(RitualRecipeContext context, RitualRecipe recipe);

    public Optional<Component> description() {return Optional.empty();}
}
