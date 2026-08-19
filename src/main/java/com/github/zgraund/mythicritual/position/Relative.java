package com.github.zgraund.mythicritual.position;

import com.github.zgraund.mythicritual.api.position.OffsetPositionResolver;
import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.OffsetParser;
import com.github.zgraund.mythicritual.util.VecUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Relative extends OffsetPositionResolver {
    public static final MapCodec<Relative> CODEC =
            OffsetParser.relative().listCodec().fieldOf("offset").xmap(Relative::new, OffsetPositionResolver::getOffsets);

    protected Relative(List<Vec3i> offsets) {
        super(offsets);
    }

    public static Relative of(Vec3i... offsets) {
        return new Relative(List.of(offsets));
    }

    @SafeVarargs
    public static Relative of(Map<RelativeDirection, Integer>... offsets) {
        return new Relative(Arrays.stream(offsets).map(OffsetParser.relative()::toVec).toList());
    }

    @Override
    protected BlockPos apply(RitualRecipeContext ctx, Vec3i offset) {
        return ctx.getOrigin().offset(VecUtils.toCardinal(offset, ctx.getPlayer().getDirection()));
    }

    @Override
    public MapCodec<? extends PositionResolver> type() {
        return CODEC;
    }
}
