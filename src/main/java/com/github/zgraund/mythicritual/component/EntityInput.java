package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class EntityInput implements RitualInput<Entity> {
    public static final MapCodec<EntityInput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            EntityPredicate.CODEC.fieldOf("entity").forGetter(input -> input.entityPredicate),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("hurtBy", -1).forGetter(input -> input.hurt)
    ).apply(inst, EntityInput::new));

    private final EntityPredicate entityPredicate;
    private final int hurt;
    // TODO: nbt for merge nbt

    public EntityInput(EntityPredicate entityPredicate, int hurt) {
        this.entityPredicate = entityPredicate;
        this.hurt = hurt;
    }

    @Override
    public Optional<Entity> test(BlockPos pos, RitualRecipeContext ctx) {
        return ctx.getEntities(pos)
                  .stream()
                  .filter(ctx::canUseInput)
                  .filter(this::testEntity)
                  .findFirst();
    }

    protected boolean testEntity(Entity entity) {
        // noinspection resource (getting the level from the entity is safe)
        if (entity.level().isClientSide()) {
            // reduced test on the client, only for visual sync
            // TODO: find a better way
            return entityPredicate.entityType()
                                  .map(typePredicate -> typePredicate.matches(entity.getType()))
                                  .orElse(true);
        } else {
            return entityPredicate.matches((ServerLevel) entity.level(), entity.position(), entity);
        }
    }

    @Override
    public void onApply(Entity entity, ServerLevel level) {
        if (hurt > 0)
            entity.hurt(level.damageSources().generic(), hurt);
        else
            // TODO: cancel loot
            entity.kill();
    }

    @Override
    public @Nonnull MapCodec<? extends RitualInput<?>> type() {
        return CODEC;
    }
}
