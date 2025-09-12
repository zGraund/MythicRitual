package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.attachment.ModDataAttachments;
import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.ingredient.ModIngredients;
import com.github.zgraund.mythicritual.item.ModItems;
import com.github.zgraund.mythicritual.particle.ModParticles;
import com.github.zgraund.mythicritual.recipe.ModRecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

@Mod(MythicRitual.MOD_ID)
public class MythicRitual {
    public static final String MOD_ID = "mythicritual";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythicRitual(@NotNull IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        com.github.zgraund.mythicritual.registries.ModRecipes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModIngredients.register(modEventBus);
        ModItems.register(modEventBus);

        ModDataComponents.register(modEventBus);
        ModDataAttachments.register(modEventBus);

        ModParticles.register(modEventBus);
    }

    @Nonnull
    public static ResourceLocation ID(String path) {return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);}

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {LOGGER.info("HELLO from server starting");}
}
