package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.compat.RitualRecipeIngredientRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
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
    static void tooltips(@NotNull RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(RitualRecipeIngredientRenderer.EntityPreviewTooltip.class, Function.identity());
    }
}
