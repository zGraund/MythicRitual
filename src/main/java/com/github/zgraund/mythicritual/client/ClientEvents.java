package com.github.zgraund.mythicritual.client;

import com.github.zgraund.mythicritual.network.RitualModifierKeyPacket;
import com.github.zgraund.mythicritual.registry.MRAttachmentTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void checkRitualModifierKey(@Nonnull PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        // noinspection resource
        if (!player.level().isClientSide())
            return;

        boolean ritualModDown = MRKeybinds.RITUAL_MODIFIER.get().isDown();
        if (player.getData(MRAttachmentTypes.IS_TRYING_RITUAL) != ritualModDown)
            PacketDistributor.sendToServer(new RitualModifierKeyPacket(ritualModDown));
    }
}
