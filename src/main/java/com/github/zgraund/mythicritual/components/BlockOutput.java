package com.github.zgraund.mythicritual.components;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockOutput implements RitualOutput {
    public static final MapCodec<BlockOutput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BlockState.CODEC.fieldOf("block").forGetter(output -> output.state),
            TagParser.LENIENT_CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(output -> output.nbt)
    ).apply(inst, BlockOutput::new));

    private final BlockState state;
    private final CompoundTag nbt;

    public BlockOutput(BlockState state, CompoundTag nbt) {
        this.state = state;
        this.nbt = nbt;
    }

    @Override
    public void apply(RitualRecipeContext ctx, BlockPos pos) {
        Level level = ctx.level;
        if (!level.isClientSide()) {
            boolean placed = level.setBlockAndUpdate(pos, state);
            if (placed && !nbt.isEmpty()) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity != null)
                    entity.loadWithComponents(nbt, level.registryAccess());
            }
        }
    }

    @Nonnull
    @Override
    public MapCodec<? extends RitualOutput> type() {
        return CODEC;
    }
}
