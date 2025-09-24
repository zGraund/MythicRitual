package com.github.zgraund.mythicritual.recipe;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.attachment.ModDataAttachments;
import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.damage.ModDamageTypes;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.item.ModItems;
import com.github.zgraund.mythicritual.recipe.action.ActionOnTransmute;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public record RitualRecipe(
        BlockState altar,
        RitualIngredient catalyst,
        Map<Vec3i, List<RitualIngredient>> locations,
        RitualIngredient result,
        List<ResourceKey<Level>> dimensions,
        HolderSet<Biome> biomes,
        EffectHelper effect,
        List<Holder<ActionOnTransmute>> onTransmute,
        boolean needSky
) implements Recipe<RitualRecipeContext> {
    @Override
    public boolean matches(@Nonnull RitualRecipeContext context, @Nonnull Level level) {
        if (!context.accept(this)) return false;
        if (locations.isEmpty()) return true;
        HashMap<Vec3i, List<OfferingHolder>> inputEntities = context.getEntitiesByOffsets(locations.keySet());
        for (Map.Entry<Vec3i, List<RitualIngredient>> location : locations.entrySet()) {
            List<OfferingHolder> entitiesAt = inputEntities.get(location.getKey());
            if (entitiesAt == null || entitiesAt.isEmpty()) return false;
            for (RitualIngredient ingredient : location.getValue()) {
                boolean found = false;
                for (OfferingHolder input : entitiesAt) {
                    if (ingredient.test(input.normalized())) {
                        input.shrink(ingredient.count());
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
        }

        return true;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull RitualRecipeContext context, @Nonnull HolderLookup.Provider registries) {
        context.consume();
        onTransmute.forEach(action -> action.value().apply(context, this));
        effect.apply((ServerLevel) context.level(), context.origin().above());
        return result.asItemStack().copy();
    }

    @Nullable
    public Entity getResultEntity(@Nonnull RitualRecipeContext context) {
        ItemStack result = this.result.asItemStack().copy();
        Level level = context.level();
        if (result.is(ModItems.SOUL)) {
            EntityType<?> type = result.get(ModDataComponents.SOUL_ENTITY_TYPE);
            if (type == null) {
                MythicRitual.LOGGER.error("No EntityType present in Soul item for recipe {}", this);
                return null;
            }
            Entity entity = type.create(level);
            if (entity instanceof Mob mob) {
                EventHooks.finalizeMobSpawn(mob, (ServerLevel) level, level.getCurrentDifficultyAt(context.origin()), MobSpawnType.MOB_SUMMONED, null);
            }
            return entity;
        }
        return new ItemEntity(level, 0, 0, 0, result);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {return true;}

    @Override
    @Nonnull
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider registries) {
        return this.result.asItemStack();
    }

    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, locations.values().stream().flatMap(List::stream).map(RitualIngredient::toVanilla).toArray(Ingredient[]::new));
    }

    public Stream<RitualIngredient> getCustomIngredients() {
        return locations.values().stream().flatMap(List::stream);
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
    public List<Component> actionDescriptions() {
        List<Component> actions = onTransmute.stream().map(Holder::value).map(ActionOnTransmute::getDescription).toList();
        if (actions.isEmpty()) return List.of(Component.translatable("info.hover.ritual.action.empty").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        return actions;
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {return ModRecipes.RITUAL_RECIPE_SERIALIZER.get();}

    @Nonnull
    public RecipeType<?> getType() {return ModRecipes.RITUAL_RECIPE_TYPE.get();}

    public record OfferingHolder(Entity original, ItemStack normalized) {
        public void shrink(int amount) {this.normalized.shrink(amount);}

        public void consume(@Nullable Player player) {
            switch (original) {
                case ItemEntity item -> item.getItem().shrink(item.getItem().getCount() - normalized.getCount());
                case LivingEntity living -> {
                    if (normalized.getCount() > 0) return;
                    living.setData(ModDataAttachments.IS_SACRIFICED, true);
                    living.hurt(new DamageSources(original.level().registryAccess()).source(ModDamageTypes.RITUAL_DAMAGE, player), Float.MAX_VALUE);
                }
                default -> {
                    if (normalized.getCount() > 0) return;
                    original.kill();
                }
            }
        }
    }
}

