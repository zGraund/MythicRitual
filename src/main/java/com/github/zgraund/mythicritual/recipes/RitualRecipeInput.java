package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.util.EntityUse;
import net.minecraft.core.BlockPos;
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

public final class RitualRecipeInput implements RecipeInput {
    private final HashMap<BlockPos, List<EntityUse>> toConsume = new HashMap<>();
    private final BlockState target;
    private final BlockPos origin;
    private final ItemStack trigger;
    private final Level level;

    public RitualRecipeInput(BlockState target, BlockPos origin, ItemStack trigger, Level level) {
        this.target = target;
        this.origin = origin;
        this.trigger = trigger;
        this.level = level;
    }

    @NonNull
    public HashMap<BlockPos, List<EntityUse>> entitiesByPosition(@NotNull List<RitualRecipeIngredient> ingredients) {
        HashMap<BlockPos, List<EntityUse>> out = new HashMap<>();
        for (RitualRecipeIngredient ingredient : ingredients) {
            BlockPos target = origin.offset(ingredient.offset());
            if (out.containsKey(target)) continue;
            out.put(target, level.getEntities(null, new AABB(target)).stream().map(e -> new EntityUse(e, 0)).collect(Collectors.toList()));
        }
        return out;
    }

    public void commit(HashMap<BlockPos, List<EntityUse>> entities) {
        toConsume.putAll(entities);
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

    public HashMap<BlockPos, List<EntityUse>> toConsume() {return toConsume;}

    public Level level() {return level;}

    @Override
    public String toString() {
        return "RitualRecipeInput[" +
                "target=" + target + ", " +
                "origin=" + origin + ", " +
                "trigger=" + trigger + ", " +
                "level=" + level + ", " +
                "matched entities=" + toConsume + ']';
    }
}
