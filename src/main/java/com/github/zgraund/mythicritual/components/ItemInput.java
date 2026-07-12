package com.github.zgraund.mythicritual.components;

import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ItemInput implements RitualInput {
    public static final MapCodec<ItemInput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("item").forGetter(input -> input.ingredients),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage", 0).forGetter(input -> input.damage)
    ).apply(inst, ItemInput::new));

    private final List<SizedIngredient> ingredients;
    private final int damage;

    public ItemInput(List<SizedIngredient> ingredients, int damage) {
        this.ingredients = ingredients;
        this.damage = damage;
    }

    @Override
    public boolean testTarget(RitualRecipeContext ctx) {
        return false;
    }

    @Override
    public boolean testPos(RitualRecipeContext ctx, BlockPos pos) {
        return false;
    }

    @Override
    public void apply(RitualRecipeContext ctx, BlockPos pos) {

    }

    @Nonnull
    @Override
    public MapCodec<? extends RitualInput> type() {
        return CODEC;
    }
}
