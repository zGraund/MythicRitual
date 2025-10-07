package com.github.zgraund.mythicritual.recipe.condition;

import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public record Altar(Optional<BlockPredicate> block, Optional<String> description) implements RitualCondition {
    public static final MapCodec<Altar> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BlockPredicate.CODEC.optionalFieldOf("value").forGetter(Altar::block),
            DESCRIPTION_CODEC.forGetter(Altar::description)
    ).apply(inst, Altar::new));

    @Override
    public boolean test(@Nonnull RitualRecipeContext context) {
        return block.isEmpty() || block.get().matches(new BlockInWorld(context.level(), context.origin(), false));
    }

    @Nonnull
    @Override
    public List<Component> getDescription() {
        return description.<List<Component>>map(s -> List.of(Component.literal(s).withStyle(ChatFormatting.GRAY))).orElseGet(List::of);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RitualConditionKey.ALTAR;
    }

    @Override
    public MapCodec<? extends RitualCondition> type() {
        return CODEC;
    }
}
