package com.github.zgraund.mythicritual.recipes.ingredients;

import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

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
    @Nonnull
    public Boolean test(@NotNull EntityConsumer entityConsumer) {
        Entity entity = entityConsumer.entity();
        int used = entityConsumer.used();
        if (!(entity instanceof LivingEntity) || used >= 1) return false;
        return entity.isAlive() && BuiltInRegistries.ENTITY_TYPE.get(this.type) == entity.getType();
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return asEntityType().getDescription();
    }

    @Nonnull
    public EntityType<?> asEntityType() {
        return BuiltInRegistries.ENTITY_TYPE.get(type);
    }
}
