package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.github.zgraund.mythicritual.util.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class RitualRecipeContext implements RecipeInput {
    private final HashMap<BlockPos, List<EntityConsumer>> validatedInput = new HashMap<>();
    private final BlockState target;
    private final BlockPos origin;
    private final ItemStack trigger;
    private final Level level;
    private final Player player;
    private final InteractionHand hand;

    public RitualRecipeContext(BlockState target, BlockPos origin, ItemStack trigger, Level level, Player player, InteractionHand hand) {
        this.target = target;
        this.origin = origin;
        this.trigger = trigger;
        this.level = level;
        this.player = player;
        this.hand = hand;
    }

    @NonNull
    public HashMap<BlockPos, List<EntityConsumer>> entitiesByPosition(@NotNull List<RitualRecipeIngredient> ingredients) {
        HashMap<BlockPos, List<EntityConsumer>> out = new HashMap<>();
        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos target = RotationUtils.relativeTo(origin, ingredient.offset(), player.getDirection());
            if (out.containsKey(target)) continue;
            out.put(target, level.getEntities(null, new AABB(target)).stream().map(e -> new EntityConsumer(e, 0)).collect(Collectors.toList()));
        }
        return out;
    }

    public boolean accept(@NotNull RitualRecipe recipe) {
        return target.getBlock().defaultBlockState() == recipe.target()
               && trigger.is(recipe.trigger().getItem())
               && (recipe.consumeOnUse() != ActionOnCraft.DESTROY || trigger.getDamageValue() == 0)
               && trigger.getCount() >= recipe.trigger().getCount()
               && (!recipe.needSky() || level.canSeeSky(origin.above()))
               && (recipe.dimensions().isEmpty() || recipe.dimensions().stream().anyMatch(level.dimension()::equals));
    }

    public void consume() {
        validatedInput.values().stream().flatMap(List::stream).forEach(EntityConsumer::consume);
    }

    public void shrink(ActionOnCraft action, int quantity) {
        if (player.isCreative()) return;
        switch (action) {
            case DESTROY -> {
                trigger.setDamageValue(trigger.getMaxDamage());
                damageOrShrink(trigger.getMaxDamage());
            }
            case CONSUME -> damageOrShrink(quantity);
        }
    }

    private void damageOrShrink(int quantity) {
        if (trigger.isDamageableItem()) trigger.hurtAndBreak(quantity, player, LivingEntity.getSlotForHand(hand));
        else trigger.shrink(quantity);
    }

    public void commit(HashMap<BlockPos, List<EntityConsumer>> entities) {
        validatedInput.putAll(entities);
    }

    @Override
    @Nonnull
    public ItemStack getItem(int index) {
        if (index != 0) throw new IllegalArgumentException("No item for index " + index);
        return this.trigger();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public BlockState target() {return target;}

    public BlockPos origin() {return origin;}

    public ItemStack trigger() {return trigger;}

    public Level level() {return level;}

    public InteractionHand hand() {return hand;}

    public Player player() {return player;}

    public HashMap<BlockPos, List<EntityConsumer>> validatedInput() {return validatedInput;}

    @Override
    public String toString() {
        return "RitualRecipeContext{" +
               "validatedInput=" + validatedInput +
               ", target=" + target +
               ", origin=" + origin +
               ", trigger=" + trigger +
               ", level=" + level +
               ", player=" + player +
               '}';
    }
}
