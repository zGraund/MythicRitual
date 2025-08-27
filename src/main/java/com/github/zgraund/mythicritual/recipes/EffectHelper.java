package com.github.zgraund.mythicritual.recipes;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum EffectHelper implements StringRepresentable {
    NONE("none"),
    PARTICLES("particles") {
        @Override
        public void apply(@NotNull ServerLevel level, BlockPos pos) {
            RandomSource random = level.getRandom();

            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS);

            for (int i = 0; i < 10; i++) {
                // Random angle around the circle
                double angle = random.nextDouble() * 2 * Math.PI;

                // Radial velocity outward
                double speed = 0.15 + random.nextDouble() * 0.2;
                double vx = Math.cos(angle) * speed;
                double vz = Math.sin(angle) * speed;

                // Upward velocity
                double vy = 0.3 + random.nextDouble() * 0.2;

                level.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 2, vx, vy, vz, 0.5);
            }
        }
    },
    LIGHTNING("lightning") {
        @Override
        public void apply(@NotNull ServerLevel level, BlockPos pos) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt == null) return;
            lightningBolt.moveTo(pos.getBottomCenter());
            lightningBolt.setVisualOnly(true);
            level.addFreshEntity(lightningBolt);
        }
    };

    public static final Codec<EffectHelper> CODEC = StringRepresentable.fromEnum(EffectHelper::values);

    private final String type;

    EffectHelper(@NotNull String type) {
        this.type = type.toLowerCase(Locale.ROOT);
    }

    public void apply(@NotNull ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
    }

    @Override
    public @NotNull String getSerializedName() {return this.type;}
}
