package com.github.zgraund.mythicritual.particle;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MythicRitual.MOD_ID);

    public static final Supplier<SimpleParticleType> RITUAL_PARTICLES = TYPES.register("ritual_particles", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
