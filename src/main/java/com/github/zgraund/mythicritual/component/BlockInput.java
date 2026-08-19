package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class BlockInput implements RitualInput<BlockPos> {
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
    public Optional<BlockPos> test(BlockPos pos, RitualRecipeContext ctx) {
        BlockInWorld block = new BlockInWorld(ctx.getLevel(), pos, false);
        return ctx.canUseInput(pos) && blocks.matches(block) ? Optional.of(pos) : Optional.empty();
    }

    @Override
    public void onApply(BlockPos pos, ServerLevel level) {
        if (consume) level.destroyBlock(pos, false);
    }

    @Override
    public @Nonnull MapCodec<? extends RitualInput<?>> type() {
        return CODEC;
    }
}
