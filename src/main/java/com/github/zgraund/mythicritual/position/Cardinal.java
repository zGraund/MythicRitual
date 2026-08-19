package com.github.zgraund.mythicritual.position;

import com.github.zgraund.mythicritual.api.position.OffsetPositionResolver;
import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.OffsetParser;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Cardinal extends OffsetPositionResolver {
    public static final MapCodec<Cardinal> CODEC =
            OffsetParser.cardinal().listCodec().fieldOf("offset").xmap(Cardinal::new, OffsetPositionResolver::getOffsets);

    protected Cardinal(List<Vec3i> offsets) {
        super(offsets);
    }

    public static Cardinal of(Vec3i... offsets) {
        return new Cardinal(List.of(offsets));
    }

    @SafeVarargs
    public static Cardinal of(Map<Direction, Integer>... offsets) {
        return new Cardinal(Arrays.stream(offsets).map(OffsetParser.cardinal()::toVec).toList());
    }

    @Override
    protected BlockPos apply(RitualRecipeContext ctx, Vec3i offset) {
        return ctx.getOrigin().offset(offset);
    }

    @Override
    public MapCodec<? extends PositionResolver> type() {
        return CODEC;
    }
}
