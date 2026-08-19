package com.github.zgraund.mythicritual.api.position;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class OffsetPositionResolver implements PositionResolver {
    protected final List<Vec3i> offsets;

    protected OffsetPositionResolver(List<Vec3i> offsets) {
        this.offsets = offsets;
    }

    @Override
    public Stream<BlockPos> resolve(RitualRecipeContext ctx) {
        return offsets.stream().map(offset -> apply(ctx, offset));
    }

    protected abstract BlockPos apply(RitualRecipeContext ctx, Vec3i offset);

    public List<Vec3i> getOffsets() {
        return offsets;
    }
}
