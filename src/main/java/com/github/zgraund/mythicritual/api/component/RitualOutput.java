package com.github.zgraund.mythicritual.api.component;

import com.github.zgraund.mythicritual.api.registry.MythicRitualBuiltInRegistries;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.github.zgraund.mythicritual.util.CodecUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface RitualOutput {
    Codec<RitualOutput> CODEC = CodecUtils.dispatch(MythicRitualBuiltInRegistries.RITUAL_OUTPUT, RitualOutput::type);
    Codec<List<RitualOutput>> LIST_CODEC = CodecUtils.listOf(CODEC);

    default void apply(BlockPos pos, RitualRecipeContext ctx) {
        if (ctx.getLevel() instanceof ServerLevel serverLevel)
            this.apply(serverLevel, pos, ctx);
    }

    /**
     * Spawn the crafting result in the world at the specified position.
     * This method will only be called on the server.
     */
    void apply(ServerLevel level, BlockPos pos, RitualRecipeContext ctx);

    @Nonnull
    MapCodec<? extends RitualOutput> type();
}
