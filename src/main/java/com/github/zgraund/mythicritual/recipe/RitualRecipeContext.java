package com.github.zgraund.mythicritual.recipe;

import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.item.ModItems;
import com.github.zgraund.mythicritual.util.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RitualRecipeContext implements RecipeInput {
    private final HashMap<Vec3i, List<RitualRecipe.OfferingHolder>> entitiesFound = new HashMap<>();
    private final BlockState altar;
    private final BlockPos origin;
    private final ItemStack catalyst;
    private final Level level;
    private final Player player;
    private final InteractionHand hand;

    public RitualRecipeContext(BlockState target, BlockPos origin, ItemStack trigger, Level level, Player player, InteractionHand hand) {
        this.altar = target;
        this.origin = origin;
        this.catalyst = trigger;
        this.level = level;
        this.player = player;
        this.hand = hand;
    }

    public HashMap<Vec3i, List<RitualRecipe.OfferingHolder>> itemsByOffset(@Nonnull Set<Vec3i> offsets) {
        for (Vec3i offset : offsets) {
            BlockPos target = RotationUtils.relativeTo(origin, offset, player.getDirection());
            entitiesFound.put(offset,
                    level.getEntities(null, new AABB(target))
                         .stream()
                         .map(entity -> {
                                     if (entity instanceof ItemEntity item) return new RitualRecipe.OfferingHolder(entity, item.getItem().copy());
                                     ItemStack item = new ItemStack(ModItems.SOUL.asItem(), 1);
                                     item.set(ModDataComponents.SOUL_ENTITY_TYPE.get(), entity.getType());
                                     return new RitualRecipe.OfferingHolder(entity, item);
                                 }
                         ).toList()
            );
        }
        return entitiesFound;
    }

    public boolean accept(@Nonnull RitualRecipe recipe) {
        return altar.getBlock().defaultBlockState() == recipe.altar()
               && recipe.catalyst().test(catalyst)
               && (!recipe.onTransmute().contains(ActionOnTransmute.DESTROY_CATALYST) || catalyst.getDamageValue() == 0)
               && (!recipe.needSky() || level.canSeeSky(origin.above()))
               && (recipe.dimensions().isEmpty() || recipe.dimensions().stream().anyMatch(level.dimension()::equals))

               && (recipe.biomes().size() == 0 || recipe.biomes().contains(level.getBiome(origin)));
    }

    public void consume() {
        entitiesFound.values().stream().flatMap(List::stream).forEach(holder -> holder.consume(player));
    }

    public void shrink(ActionOnTransmute action, int quantity) {
        if (player.isCreative()) return;
        switch (action) {
            case DESTROY_CATALYST -> {
                catalyst.setDamageValue(catalyst.getMaxDamage());
                damageOrShrink(catalyst.getMaxDamage());
            }
            case CONSUME_CATALYST -> damageOrShrink(quantity);
        }
    }

    private void damageOrShrink(int quantity) {
        if (catalyst.isDamageableItem()) catalyst.hurtAndBreak(quantity, player, LivingEntity.getSlotForHand(hand));
        else catalyst.shrink(quantity);
    }

    @Override
    @Nonnull
    public ItemStack getItem(int index) {
        if (index != 0) throw new IllegalArgumentException("No item for index " + index);
        return this.catalyst();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public BlockState altar() {return altar;}

    public BlockPos origin() {return origin;}

    public ItemStack catalyst() {return catalyst;}

    public Level level() {return level;}

    public InteractionHand hand() {return hand;}

    public Player player() {return player;}

    @Override
    public String toString() {
        return "RitualRecipeContext{" +
               "validatedInput=" + entitiesFound +
               ", altar=" + altar +
               ", origin=" + origin +
               ", catalyst=" + catalyst +
               ", level=" + level +
               ", player=" + player +
               '}';
    }
}
