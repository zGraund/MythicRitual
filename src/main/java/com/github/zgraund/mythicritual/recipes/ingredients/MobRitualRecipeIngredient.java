package com.github.zgraund.mythicritual.recipes.ingredients;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record MobRitualRecipeIngredient(
        ResourceLocation type,
        Vec3i offset
) implements RitualRecipeIngredient {
    public static final MapCodec<MobRitualRecipeIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(MobRitualRecipeIngredient::type),
                    Vec3i.CODEC.optionalFieldOf("pos", new Vec3i(0, 1, 0)).forGetter(MobRitualRecipeIngredient::offset)
            ).apply(inst, MobRitualRecipeIngredient::new)
    );

    @Override
    public Boolean test(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        return entity.isAlive() && BuiltInRegistries.ENTITY_TYPE.get(this.type) == entity.getType();
    }
}
