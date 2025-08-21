package com.github.zgraund.mythicritual;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MythicRitual.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MythicRitual.MOD_ID, value = Dist.CLIENT)
public class MythicRitualClient {
    public MythicRitualClient(ModContainer container) {}

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}
}
