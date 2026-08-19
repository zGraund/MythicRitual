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
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface RitualInput<T> {
    Codec<RitualInput<?>> CODEC = CodecUtils.dispatch(MythicRitualBuiltInRegistries.RITUAL_INPUT, RitualInput::type);
    Codec<List<RitualInput<?>>> LIST_CODEC = CodecUtils.listOf(CODEC);

    /**
     * Test a specific position on the recipe context.
     */
    Optional<T> test(BlockPos pos, RitualRecipeContext ctx);

    /**
     * Callback that gets run when the recipe is executed.
     */
    void onApply(T key, ServerLevel level);

    @Nonnull
    MapCodec<? extends RitualInput<?>> type();
}
