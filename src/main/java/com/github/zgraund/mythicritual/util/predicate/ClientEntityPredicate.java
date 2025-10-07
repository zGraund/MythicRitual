package com.github.zgraund.mythicritual.util.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.Optional;

public record ClientEntityPredicate(
        Optional<EntityTypePredicate> entityType,
        Optional<MobEffectsPredicate> effects,
        Optional<NbtPredicate> nbt,
        Optional<EntityFlagsPredicate> flags,
        Optional<EntityEquipmentPredicate> equipment
) {
    public static final MapCodec<ClientEntityPredicate> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            EntityTypePredicate.CODEC.optionalFieldOf("type").forGetter(ClientEntityPredicate::entityType),
            MobEffectsPredicate.CODEC.optionalFieldOf("effects").forGetter(ClientEntityPredicate::effects),
            NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(ClientEntityPredicate::nbt),
            EntityFlagsPredicate.CODEC.optionalFieldOf("flags").forGetter(ClientEntityPredicate::flags),
            EntityEquipmentPredicate.CODEC.optionalFieldOf("equipment").forGetter(ClientEntityPredicate::equipment)
    ).apply(inst, ClientEntityPredicate::new));

    public boolean matches(Entity entity) {
        return (entityType.isEmpty() || entityType.get().matches(entity.getType())) &&
               (effects.isEmpty() || effects.get().matches(entity)) &&
               (nbt.isEmpty() || nbt.get().matches(entity)) &&
               (flags.isEmpty() || flags.get().matches(entity)) &&
               (equipment.isEmpty() || equipment.get().matches(entity));
    }

    @Nonnull
    @Contract(value = " -> new")
    public static Builder builder() {return new Builder();}

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
    public static class Builder {
        private Optional<EntityTypePredicate> entityType;
        private Optional<MobEffectsPredicate> effects;
        private Optional<NbtPredicate> nbt;
        private Optional<EntityFlagsPredicate> flags;
        private Optional<EntityEquipmentPredicate> equipment;

        public void setEntityType(EntityTypePredicate entityType) {
            this.entityType = Optional.of(entityType);
        }

        public void setEffects(MobEffectsPredicate effects) {
            this.effects = Optional.of(effects);
        }

        public void setNbt(NbtPredicate nbt) {
            this.nbt = Optional.of(nbt);
        }

        public void setFlags(EntityFlagsPredicate flags) {
            this.flags = Optional.of(flags);
        }

        public void setEquipment(EntityEquipmentPredicate equipment) {
            this.equipment = Optional.of(equipment);
        }

        public ClientEntityPredicate build() {return new ClientEntityPredicate(entityType, effects, nbt, flags, equipment);}
    }
}
