package com.github.zgraund.mythicritual.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class RitualRecipeContext implements RecipeInput {
    public final Player player;
    public final Level level;
    public final BlockPos origin;
    public final HitResult hitResult;
    private final Map<Object, Runnable> inputCallbacks = new HashMap<>();

    public RitualRecipeContext(Player player, BlockPos origin) {
        this.player = player;
        this.level = player.level();
        this.origin = origin;
        this.hitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::isAlive, player.blockInteractionRange());
    }

    public boolean canUseInput(Object key) {
        return inputCallbacks.containsKey(key);
    }

    public void acceptInput(BlockPos pos, Runnable callback) {
        inputCallbacks.put(pos, callback);
    }

    public void acceptInput(Entity entity, Runnable callback) {
        inputCallbacks.put(entity, callback);
    }

    public void processInputs() {
        inputCallbacks.values().forEach(Runnable::run);
    }

    @Nonnull
    @Override
    public ItemStack getItem(int index) {
        return player.getItemInHand(InteractionHand.MAIN_HAND);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return hitResult.getType() == HitResult.Type.MISS;
    }
}
