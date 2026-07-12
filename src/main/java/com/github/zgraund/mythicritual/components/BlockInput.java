package com.github.zgraund.mythicritual.components;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockInput implements RitualInput {
    public static final MapCodec<BlockInput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BlockPredicate.CODEC.fieldOf("block").forGetter(input -> input.blocks),
            Codec.BOOL.optionalFieldOf("consume", false).forGetter(input -> input.consume)
    ).apply(inst, BlockInput::new));

    private final BlockPredicate blocks;
    private final boolean consume;

    public BlockInput(BlockPredicate blocks, boolean consume) {
        this.blocks = blocks;
        this.consume = consume;
    }

    @Override
    public boolean testTarget(RitualRecipeContext ctx) {
        return ctx.clickedBlock != null && blocks.matches(new BlockInWorld(ctx.level, ctx.origin, false));
    }

    @Override
    public boolean testPos(RitualRecipeContext ctx, BlockPos pos) {
        return blocks.matches(new BlockInWorld(ctx.level, pos, false));
    }

    @Override
    public void apply(RitualRecipeContext ctx, BlockPos pos) {
        if (consume && !ctx.level.isClientSide())
            ctx.level.destroyBlock(pos, false);
    }

    @Nonnull
    @Override
    public MapCodec<? extends RitualInput> type() {
        return CODEC;
    }
}
