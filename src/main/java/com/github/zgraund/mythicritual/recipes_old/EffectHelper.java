package com.github.zgraund.mythicritual.recipes_old;

import com.github.zgraund.mythicritual.particle.ModParticles;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;

import javax.annotation.Nonnull;

import java.util.Locale;

public enum EffectHelper implements StringRepresentable {
    NONE("none"),
    PARTICLES("particles") {
        @Override
        public void apply(ServerLevel level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.3f, 0.1f);

            int[][] directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
            for (int[] dir : directions) {
                double hOffset = 0.15 / (Math.abs(dir[0]) + Math.abs(dir[1]));
                level.sendParticles(
                        ModParticles.RITUAL_PARTICLES.get(),
                        pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        0, // Need to be 0 cuz mojang
                        dir[0] * hOffset, 0.6, dir[1] * hOffset,
                        0.5
                );
            }
        }
    },
    LIGHTNING("lightning") {
        @Override
        public void apply(ServerLevel level, BlockPos pos) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt == null) return;
            lightningBolt.moveTo(pos.getBottomCenter());
            lightningBolt.setVisualOnly(true);
            level.addFreshEntity(lightningBolt);
        }
    };

    public static final Codec<EffectHelper> CODEC = StringRepresentable.fromEnum(EffectHelper::values);

    private final String type;

    EffectHelper(String type) {
        this.type = type.toLowerCase(Locale.ROOT);
    }

    public void apply(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
    }

    @Override
    public String getSerializedName() {return this.type;}
}
