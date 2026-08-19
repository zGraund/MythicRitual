package com.github.zgraund.mythicritual.core;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualPosition {
    public static final MapCodec<RitualPosition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            PositionResolver.CODEC.fieldOf("at").forGetter(RitualPosition::getResolver),
            RitualInput.LIST_CODEC.optionalFieldOf("ingredients", List.of()).forGetter(RitualPosition::getInputs),
            RitualOutput.LIST_CODEC.optionalFieldOf("results", List.of()).forGetter(RitualPosition::getOutputs)
    ).apply(inst, RitualPosition::new));

    protected final PositionResolver resolver;
    protected final List<RitualInput<?>> inputs;
    protected final List<RitualOutput> outputs;

    public RitualPosition(PositionResolver resolver, List<RitualInput<?>> inputs, List<RitualOutput> outputs) {
        this.resolver = resolver;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public boolean test(RitualRecipeContext ctx) {
        return resolver.resolve(ctx).allMatch(pos -> testPosition(pos, ctx));
    }

    private boolean testPosition(BlockPos pos, RitualRecipeContext ctx) {
        return ctx.getLevel().isLoaded(pos) && inputs.stream().allMatch(input -> testInput(input, pos, ctx));
    }

    private <T> boolean testInput(RitualInput<T> input, BlockPos pos, RitualRecipeContext ctx) {
        Optional<T> value = input.test(pos, ctx);
        if (value.isPresent()) {
            ctx.acceptInput(value.get(), input::onApply);
            return true;
        }
        return false;
    }

    public PositionResolver getResolver() {
        return resolver;
    }

    public List<RitualInput<?>> getInputs() {
        return inputs;
    }

    public List<RitualOutput> getOutputs() {
        return outputs;
    }
}
