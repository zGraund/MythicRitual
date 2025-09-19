package com.github.zgraund.mythicritual.recipe;

import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class RitualRecipeSerializer implements RecipeSerializer<RitualRecipe> {
    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    BlockState.CODEC.fieldOf("altar").forGetter(RitualRecipe::altar),
                    RitualIngredient.CODEC.codec().optionalFieldOf("catalyst", RitualIngredient.EMPTY).forGetter(RitualRecipe::catalyst),
                    Codec.pair(
                            Vec3i.CODEC.fieldOf("position").codec(),
                            RitualIngredient.CODEC.codec().listOf().fieldOf("offerings").codec()
                    ).listOf().optionalFieldOf("locations", List.of()).xmap(
                            pairs -> pairs.stream().collect(Pair.toMap()),
                            map -> map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).toList()

                    ).forGetter(RitualRecipe::locations),
                    RitualIngredient.CODEC.fieldOf("result").forGetter(RitualRecipe::result),
                    Level.RESOURCE_KEY_CODEC.listOf().optionalFieldOf("dimensions", List.of()).forGetter(RitualRecipe::dimensions),
                    Biome.LIST_CODEC.optionalFieldOf("biomes", HolderSet.empty()).forGetter(RitualRecipe::biomes),
                    EffectHelper.CODEC.optionalFieldOf("effect", EffectHelper.NONE).forGetter(RitualRecipe::effect),
                    ActionOnTransmute.CODEC.listOf().optionalFieldOf("actions", ActionOnTransmute.DEFAULT).forGetter(RitualRecipe::onTransmute),
                    Codec.BOOL.optionalFieldOf("sky", false).forGetter(RitualRecipe::needSky)
            ).apply(inst, RitualRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

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
