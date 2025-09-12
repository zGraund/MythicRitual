package com.github.zgraund.mythicritual.recipes_old;

import com.github.zgraund.mythicritual.recipes_old.ingredients.RitualRecipeOffering;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class RitualRecipeSerializer implements RecipeSerializer<RitualRecipe> {
    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    BlockState.CODEC.fieldOf("altar").forGetter(RitualRecipe::altar),
                    ItemStack.CODEC.optionalFieldOf("catalyst", ItemStack.EMPTY).forGetter(RitualRecipe::catalyst),
                    RitualRecipeOffering.CODEC.listOf().fieldOf("locations").forGetter(RitualRecipe::offerings),
//                    ItemStack.CODEC.fieldOf("result").forGetter(RitualRecipe::result),
                    RitualRecipeOffering.CODEC.fieldOf("result").forGetter(RitualRecipe::result),
                    Level.RESOURCE_KEY_CODEC.listOf().optionalFieldOf("dimensions", List.of()).forGetter(RitualRecipe::dimensions),
                    ResourceKey.codec(Registries.BIOME).listOf().optionalFieldOf("biomes", List.of()).forGetter(RitualRecipe::biomes),
                    EffectHelper.CODEC.optionalFieldOf("effect", EffectHelper.NONE).forGetter(RitualRecipe::effect),
                    Codec.BOOL.optionalFieldOf("needSky", false).forGetter(RitualRecipe::needSky),
                    ActionOnTransmute.CODEC.optionalFieldOf("onTransmute", ActionOnTransmute.NONE).forGetter(RitualRecipe::onTransmute)
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
