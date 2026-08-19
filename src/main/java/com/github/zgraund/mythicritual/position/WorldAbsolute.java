package com.github.zgraund.mythicritual.position;

import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.CodecUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WorldAbsolute implements PositionResolver {
    public static final MapCodec<WorldAbsolute> CODEC =
            CodecUtils.listOf(BlockPos.CODEC).fieldOf("position").xmap(WorldAbsolute::new, WorldAbsolute::getPositions);

    protected final List<BlockPos> positions;

    protected WorldAbsolute(List<BlockPos> position) {
        this.positions = position;
    }

    public static WorldAbsolute of(BlockPos... positions) {
        return new WorldAbsolute(List.of(positions));
    }

    @Override
    public Stream<BlockPos> resolve(RitualRecipeContext ctx) {
        return positions.stream();
    }

    public List<BlockPos> getPositions() {
        return positions;
    }

    @Override
    public MapCodec<? extends PositionResolver> type() {
        return CODEC;
    }
}
