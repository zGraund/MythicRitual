package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemInput implements RitualInput<ItemStack> {
    public static final MapCodec<ItemInput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.FLAT_CODEC.fieldOf("item").forGetter(input -> input.ingredient),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage", 0).forGetter(input -> input.damage)
    ).apply(inst, ItemInput::new));

    private final SizedIngredient ingredient;
    private final int damage;

    public ItemInput(SizedIngredient ingredients, int damage) {
        this.ingredient = ingredients;
        this.damage = damage;
    }

    @Override
    public Optional<ItemStack> test(BlockPos pos, RitualRecipeContext ctx) {
        return ctx.getItems(pos)
                  .stream()
                  .filter(ctx::canUseInput)
                  .filter(ingredient::test)
                  .findFirst();
    }

    @Override
    public void onApply(ItemStack stack, ServerLevel level) {
        if (damage > 0 && stack.isDamageableItem())
            stack.hurtAndBreak(damage, level, null, (item) -> {
                //TODO: sound and particles
            });
        else
            //TODO: sound and particles
            stack.shrink(ingredient.count());
    }

    @Override
    public MapCodec<? extends RitualInput<?>> type() {
        return CODEC;
    }
}
