package com.github.zgraund.mythicritual.api.position;

import com.github.zgraund.mythicritual.api.registry.MythicRitualBuiltInRegistries;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.CodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface PositionResolver {
    Codec<PositionResolver> CODEC =
            CodecUtils.dispatch(MythicRitualBuiltInRegistries.POSITION_RESOLVER, PositionResolver::type);

    Stream<BlockPos> resolve(RitualRecipeContext ctx);

    MapCodec<? extends PositionResolver> type();
}
