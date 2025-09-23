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
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

@Mod(MythicRitual.MOD_ID)
public class MythicRitual {
    public static final String MOD_ID = "mythicritual";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythicRitual(@Nonnull IEventBus modEventBus) {
        ModRecipes.register(modEventBus);
        ModIngredients.register(modEventBus);
        ModItems.register(modEventBus);

        ModDataComponents.register(modEventBus);
        ModDataAttachments.register(modEventBus);

        ModParticles.register(modEventBus);
    }

    @Nonnull
    public static ResourceLocation id(String path) {return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);}
}
