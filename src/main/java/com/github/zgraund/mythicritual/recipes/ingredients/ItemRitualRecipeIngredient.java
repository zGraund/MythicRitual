package com.github.zgraund.mythicritual.recipes.ingredients;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public record ItemRitualRecipeIngredient(
        ResourceLocation type,
        int quantity,
        Vec3i offset
) implements RitualRecipeIngredient {
    public static final MapCodec<ItemRitualRecipeIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(ItemRitualRecipeIngredient::type),
                    Codec.INT.optionalFieldOf("quantity", 1).forGetter(ItemRitualRecipeIngredient::quantity),
                    Vec3i.CODEC.optionalFieldOf("pos", new Vec3i(0, 1, 0)).forGetter(ItemRitualRecipeIngredient::offset)
            ).apply(inst, ItemRitualRecipeIngredient::new)
    );

    @Override
    public Boolean test(Entity entity) {
        if (!(entity instanceof ItemEntity itemEntity)) return false;
        ItemStack input = itemEntity.getItem();
        return input.is(BuiltInRegistries.ITEM.get(type)) && input.getCount() >= quantity;
    }
}
