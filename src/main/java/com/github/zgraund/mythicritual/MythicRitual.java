package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.registry.MRAttachmentTypes;
import com.github.zgraund.mythicritual.registry.MRRecipes;
import com.github.zgraund.mythicritual.registry.PositionResolvers;
import com.github.zgraund.mythicritual.registry.RitualComponents;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

@Mod(MythicRitual.MOD_ID)
public class MythicRitual {
    public static final String MOD_ID = "mythicritual";
    public static final String MOD_NAME = "Mythic Ritual";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythicRitual(IEventBus modEventBus, @Nonnull ModContainer modContainer) {
        LOGGER.info("{} starting up!", MOD_ID);

        RitualComponents.register(modEventBus);
        PositionResolvers.register(modEventBus);

        MRRecipes.register(modEventBus);

        MRAttachmentTypes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @Nonnull
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
