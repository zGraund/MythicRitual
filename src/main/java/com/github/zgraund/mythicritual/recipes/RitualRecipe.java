package com.github.zgraund.mythicritual.recipes;

import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeOffering;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.github.zgraund.mythicritual.util.RotationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record RitualRecipe(
        BlockState altar,
        ItemStack catalyst,
        List<RitualRecipeOffering> offerings,
        ItemStack result,
        List<ResourceKey<Level>> dimensions,
        List<ResourceKey<Biome>> biomes,
        EffectHelper effect,
        boolean needSky,
        ActionOnTransmute onTransmute
) implements Recipe<RitualRecipeContext> {
    @Override
    public boolean matches(@NotNull RitualRecipeContext context, @NotNull Level level) {
        if (!context.accept(this)) return false;
        HashMap<BlockPos, List<EntityConsumer>> inputEntities = context.entitiesByPosition(offerings);
        if (inputEntities.isEmpty()) return false;
        for (RitualRecipeOffering offering : offerings) {
            BlockPos expected = RotationUtils.relativeTo(context.origin(), offering.offset(), context.player().getDirection());
            List<EntityConsumer> entitiesAt = inputEntities.get(expected);
            if (entitiesAt == null || entitiesAt.isEmpty()) return false;
            Optional<EntityConsumer> match = entitiesAt.stream().filter(offering::test).findFirst();
            if (match.isPresent()) {
                match.get().increment(offering.quantity());
                continue;
            }
            return false;
        }
        context.commit(inputEntities);
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeContext context, HolderLookup.@NotNull Provider registries) {return this.result.copy();}

    @Nonnull
    public ItemStack execute(@NotNull RitualRecipeContext context) {
        context.consume();
        context.shrink(onTransmute, catalyst.getCount());
        context.player().swing(context.hand(), true);
        effect.apply((ServerLevel) context.level(), context.origin().above());
        return assemble(context, context.level().registryAccess());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {return true;}

    @Override
    @Nonnull
    public ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {return this.result;}

    @Nonnull
    public Component dimensionsDescription() {
        MutableComponent prefix = Component.literal("Dimensions: ").withStyle(ChatFormatting.GRAY);
        String dim = dimensions.isEmpty() ? "All" : dimensions.stream().map(ResourceKey::location).toList().toString();
        return prefix.append(Component.literal(dim).withStyle(ChatFormatting.GREEN));
    }

    @Nonnull
    public Component biomeDescription() {
        MutableComponent prefix = Component.literal("Biomes: ").withStyle(ChatFormatting.GRAY);
        String biome = biomes.isEmpty() ? "All" : biomes.stream().map(ResourceKey::location).toList().toString();
        return prefix.append(Component.literal(biome).withStyle(ChatFormatting.GREEN));
    }

    @Nonnull
    public Component skyAccessDescription() {
        MutableComponent prefix = Component.literal("Require access to sky: ").withStyle(ChatFormatting.GRAY);
        return prefix.append(Component.literal(Boolean.toString(needSky)).withStyle(needSky ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    @Nonnull
    public Optional<Component> catalystDescription() {
        if (!catalyst.isDamageableItem()) return Optional.empty();
        return switch (onTransmute) {
            case NONE -> Optional.empty();
            case CONSUME -> Optional.of(Component.literal("The durability will be reduced!").withStyle(ChatFormatting.YELLOW));
            case DESTROY -> Optional.of(Component.literal("The item will be destroyed!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        };
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {return ModRecipes.RITUAL_RECIPE_SERIALIZER.get();}

    @Nonnull
    public RecipeType<?> getType() {return ModRecipes.RITUAL_RECIPE_TYPE.get();}
}
