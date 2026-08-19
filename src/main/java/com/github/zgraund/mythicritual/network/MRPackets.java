package com.github.zgraund.mythicritual.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nonnull;

@EventBusSubscriber
public class MRPackets {
    @SubscribeEvent
    public static void register(@Nonnull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                RitualModifierKeyPacket.TYPE,
                RitualModifierKeyPacket.STREAM_CODEC,
                RitualModifierKeyPacket::handleOnServer
        );
    }
}
