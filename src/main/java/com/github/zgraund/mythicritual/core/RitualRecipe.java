package com.github.zgraund.mythicritual.core;

import com.github.zgraund.mythicritual.registry.MRRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualRecipe implements Recipe<RitualRecipeContext> {
    protected final List<RitualPosition> positions;

    public RitualRecipe(List<RitualPosition> positions) {
        this.positions = positions;
    }

    @Override
    public boolean matches(RitualRecipeContext input, Level level) {
        for (RitualPosition position : positions) {

        }
        return false;
    }

    @Override
    public ItemStack assemble(RitualRecipeContext input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MRRecipes.RITUAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MRRecipes.RITUAL_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                RitualPosition.CODEC.codec().listOf().fieldOf("positions").forGetter(recipe -> recipe.positions)
        ).apply(inst, RitualRecipe::new));
        // TODO: make a proper stream codec
        public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

        @Override
        public MapCodec<RitualRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
