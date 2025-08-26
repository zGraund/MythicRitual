package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class RitualRecipeSerializer implements RecipeSerializer<RitualRecipe> {
    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    BlockState.CODEC.fieldOf("target").forGetter(RitualRecipe::target),
                    ItemStack.CODEC.fieldOf("trigger").forGetter(RitualRecipe::trigger),
                    RitualRecipeIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(RitualRecipe::ingredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(RitualRecipe::result),
                    ResourceKey.codec(Registries.DIMENSION).listOf().optionalFieldOf("dimensions", List.of()).forGetter(RitualRecipe::dimensions),
                    Codec.BOOL.optionalFieldOf("needSky", false).forGetter(RitualRecipe::needSky),
                    Codec.BOOL.optionalFieldOf("consumeTrigger", false).forGetter(RitualRecipe::consumeTrigger)
            ).apply(inst, RitualRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    @Override
    @Nonnull
    public MapCodec<RitualRecipe> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
