package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ItemOutput implements RitualOutput {
    public static final MapCodec<ItemOutput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(output -> output.item)
    ).apply(inst, ItemOutput::new));

    private final ItemStack item;

    public ItemOutput(ItemStack item) {
        this.item = item;
    }

    @Override
    public void apply(ServerLevel level, BlockPos pos, RitualRecipeContext ctx) {
        ItemEntity itemEntity = EntityType.ITEM.create(level);
        if (itemEntity == null)
            return;
        itemEntity.setPos(pos.getBottomCenter());
        itemEntity.setDeltaMovement(0, 0.3, 0);
        level.addFreshEntity(itemEntity);
    }

    @Nonnull
    @Override
    public MapCodec<? extends RitualOutput> type() {
        return CODEC;
    }
}
