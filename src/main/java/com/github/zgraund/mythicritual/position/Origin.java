package com.github.zgraund.mythicritual.position;

import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Origin implements PositionResolver {
    public static final Origin INSTANCE = new Origin();
    public static final MapCodec<Origin> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Stream<BlockPos> resolve(RitualRecipeContext ctx) {
        return Stream.of(ctx.getOrigin());
    }

    @Override
    public MapCodec<? extends PositionResolver> type() {
        return CODEC;
    }
}
