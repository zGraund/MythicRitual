package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.core.RitualRecipeContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EntityOutput implements RitualOutput {
    public static final MapCodec<EntityOutput> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(output -> output.entityType),
            TagParser.LENIENT_CODEC.fieldOf("nbt").forGetter(output -> output.nbt)
    ).apply(inst, EntityOutput::new));

    private final EntityType<?> entityType;
    private final CompoundTag nbt;

    public EntityOutput(EntityType<?> entityType, CompoundTag nbt) {
        this.entityType = entityType;
        this.nbt = nbt;
    }

    @Override
    public void apply(ServerLevel level, BlockPos pos, RitualRecipeContext ctx) {
        Entity entity = entityType.create(level);
        if (entity == null)
            return;
        if (entity instanceof Mob mob)
            EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null);
        entity.load(entity.saveWithoutId(new CompoundTag()).merge(nbt));
        entity.setPos(pos.getBottomCenter());
        level.addFreshEntityWithPassengers(entity);
    }

    @Nonnull
    @Override
    public MapCodec<? extends RitualOutput> type() {
        return CODEC;
    }
}
