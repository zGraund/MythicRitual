package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.particle.RitualParticle;
import com.github.zgraund.mythicritual.particle.ModParticles;
import com.github.zgraund.mythicritual.render.EntityPreviewTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MythicRitual.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MythicRitual.MOD_ID, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class MythicRitualClient {
    public MythicRitualClient(ModContainer container) {}

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}

    @SubscribeEvent
    static void registerClientTooltipFactories(@NotNull RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(EntityPreviewTooltip.class, Function.identity());
    }

    @SubscribeEvent
    static void registerParticleFactory(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RITUAL_PARTICLES.get(), RitualParticle.Provider::new);
    }
}
