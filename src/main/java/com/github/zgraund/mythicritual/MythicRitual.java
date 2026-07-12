package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.registry.RitualComponents;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

@Mod(MythicRitual.MOD_ID)
@EventBusSubscriber
public class MythicRitual {
    public static final String MOD_ID = "mythicritual";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythicRitual(IEventBus modEventBus, @Nonnull ModContainer modContainer) {
        LOGGER.info("{} starting up!", MOD_ID);

        RitualComponents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public static void click(PlayerInteractEvent.EntityInteractSpecific event) {
        LOGGER.info("entity: {}\nlevel: {}", event.getTarget().getType(), event.getLevel().isClientSide() ? "client" : "server");
    }

    @Nonnull
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
