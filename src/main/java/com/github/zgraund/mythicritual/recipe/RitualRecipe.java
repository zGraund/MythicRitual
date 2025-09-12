package com.github.zgraund.mythicritual.recipe;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.attachment.ModDataAttachments;
import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record RitualRecipe(
        BlockState altar,
        RitualIngredient catalyst,
        Map<Vec3i, List<RitualIngredient>> locations,
        RitualIngredient result,
        List<ResourceKey<Level>> dimensions,
        HolderSet<Biome> biomes,
        EffectHelper effect,
        ActionOnTransmute onTransmute,
        boolean needSky
) implements Recipe<RitualRecipeContext> {
    @Override
    public boolean matches(@NotNull RitualRecipeContext context, @NotNull Level level) {
        if (!context.accept(this)) return false;

        HashMap<Vec3i, List<OfferingHolder>> inputEntities = context.itemsByOffset(locations.keySet());

        if (inputEntities.isEmpty()) return false;
        if (inputEntities.size() != locations.size()) return false;

        for (Map.Entry<Vec3i, List<RitualIngredient>> location : locations.entrySet()) {
            List<OfferingHolder> entitiesAt = inputEntities.get(location.getKey());
            if (entitiesAt == null || entitiesAt.isEmpty()) return false;

            ingredient:
            for (RitualIngredient ingredient : location.getValue()) {
                for (OfferingHolder input : entitiesAt) {
                    if (ingredient.test(input.normalized())) {
                        input.shrink(ingredient.count());
                        continue ingredient;
                    }
                }
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull RitualRecipeContext context, HolderLookup.@NotNull Provider registries) {
        context.consume();
        context.shrink(onTransmute, catalyst.count());
        context.player().swing(context.hand(), true);
        effect.apply((ServerLevel) context.level(), context.origin().above());
        return result.asItemStack().copy();
    }

    @Nullable
    public Entity getResultEntity(RitualRecipeContext context, HolderLookup.Provider registries) {
        ItemStack result = assemble(context, registries);
        Level level = context.level();
        if (result.is(ModItems.SOUL)) {
            EntityType<?> type = result.get(ModDataComponents.SOUL_ENTITY_TYPE);
            if (type == null) {
                MythicRitual.LOGGER.error("No EntityType present in Soul item");
                return null;
            }
            return type.create(level);
        }
        return new ItemEntity(level, 0, 0, 0, result);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {return true;}

    @Override
    @Nonnull
    public ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return this.result.asItemStack();
    }

    @Nonnull
    public Component dimensionsDescription() {
        MutableComponent prefix = Component.literal("Dimensions: ").withStyle(ChatFormatting.GRAY);
        String dim = dimensions.isEmpty() ? "All" : dimensions.stream().map(ResourceKey::location).toList().toString();
        return prefix.append(Component.literal(dim).withStyle(ChatFormatting.GREEN));
    }

    @Nonnull
    public Component biomeDescription() {
        MutableComponent prefix = Component.literal("Biomes: ").withStyle(ChatFormatting.GRAY);
        String biome = biomes.size() == 0 ? "All" : biomes.stream().map(Holder::getRegisteredName).toList().toString();
        return prefix.append(Component.literal(biome).withStyle(ChatFormatting.GREEN));
    }

    @Nonnull
    public Component skyAccessDescription() {
        MutableComponent prefix = Component.literal("Require access to sky: ").withStyle(ChatFormatting.GRAY);
        return prefix.append(Component.literal(Boolean.toString(needSky)).withStyle(needSky ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    @Nonnull
    public Optional<Component> catalystDescription() {
        if (!catalyst.asItemStack().isDamageableItem()) return Optional.empty();
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

    public record OfferingHolder(Entity original, ItemStack normalized) {
        public void shrink(int amount) {this.normalized.shrink(amount);}

        public void consume() {
            switch (original) {
                case ItemEntity i -> i.getItem().shrink(Math.abs(normalized.getCount() - i.getItem().getCount()));
                case LivingEntity l -> {
                    if (!normalized.isEmpty() || normalized.getCount() > 0) return;
                    l.setData(ModDataAttachments.IS_SACRIFICED, true);
                    l.hurt(new DamageSources(original.level().registryAccess()).magic(), Float.MAX_VALUE);
                }
                default -> original.kill();
            }
        }
    }
}

