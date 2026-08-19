package com.github.zgraund.mythicritual.core;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
public class RitualRecipeContext implements RecipeInput {
    private final Map<BlockPos, Pair<List<ItemStack>, List<Entity>>> entityCache = new HashMap<>();
    private final Map<Object, Runnable> inputCallbacks = new HashMap<>();
    private final Player player;
    private final Level level;
    @Nullable
    private BlockPos origin;
    @Nullable
    private HitResult hitResult;

    public RitualRecipeContext(Player player) {
        this.player = player;
        this.level = player.level();
    }

    public BlockPos getOrigin() {
        if (origin == null) {
            HitResult hit = getHitResult();
            this.origin = switch (hit) {
                case EntityHitResult entityHit -> entityHit.getEntity().blockPosition();
                case BlockHitResult blockHit -> blockHit.getBlockPos();
                default -> BlockPos.ZERO;
            };
        }
        return origin;
    }

    public HitResult getHitResult() {
        if (hitResult == null)
            this.hitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::isAlive, player.blockInteractionRange());
        return hitResult;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public ItemStack getItemInHand() {
        return getItem(0);
    }

    @Nonnull
    public List<ItemStack> getItems(BlockPos pos) {
        if (!entityCache.containsKey(pos)) {
            fetchEntities(pos);
        }
        return entityCache.get(pos).getFirst();
    }

    @Nonnull
    public List<Entity> getEntities(BlockPos pos) {
        if (!entityCache.containsKey(pos)) {
            fetchEntities(pos);
        }
        return entityCache.get(pos).getSecond();
    }

    private void fetchEntities(BlockPos pos) {
        List<Entity> nonItems = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        level.getEntities((Entity) null, new AABB(pos), EntitySelector.ENTITY_STILL_ALIVE)
             .forEach(entity -> {
                 if (entity instanceof ItemEntity item)
                     items.add(item.getItem());
                 else
                     nonItems.add(entity);
             });
        entityCache.put(pos, Pair.of(items, nonItems));
    }


    public boolean canUseInput(Object key) {
        return inputCallbacks.containsKey(key);
    }

    public <T> void acceptInput(T key, BiConsumer<T, ServerLevel> consumer) {
        if (level.isClientSide())
            return;
        inputCallbacks.put(key, () -> consumer.accept(key, (ServerLevel) level));
    }

    public void processInputs() {
        if (level.isClientSide())
            return;
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
        // TODO: pre-check for item from lookup
        return false;
    }
}
