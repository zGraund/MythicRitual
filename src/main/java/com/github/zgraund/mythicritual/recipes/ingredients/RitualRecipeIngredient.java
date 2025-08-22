package com.github.zgraund.mythicritual.recipes.ingredients;

import com.mojang.serialization.Codec;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public sealed interface RitualRecipeIngredient permits ItemRitualRecipeIngredient, MobRitualRecipeIngredient {

    Codec<RitualRecipeIngredient> CODEC = ResourceLocation.CODEC.dispatch(
            RitualRecipeIngredient::type,
            type -> {
                if (BuiltInRegistries.ITEM.containsKey(type)) return ItemRitualRecipeIngredient.CODEC;
                if (!type.equals(ResourceLocation.parse("minecraft:item")) && BuiltInRegistries.ENTITY_TYPE.containsKey(type))
                    return MobRitualRecipeIngredient.CODEC;
                throw new IllegalArgumentException("Unknown ritual ingredient type: " + type);
            }
    );

    ResourceLocation type();

    Vec3i offset();

    Boolean test(Entity entity);

    // TODO: BlockState as ingredient
    // Boolean test(BlockState block);
}
