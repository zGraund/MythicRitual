package com.github.zgraund.mythicritual.client;

import com.github.zgraund.mythicritual.MythicRitual;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import javax.annotation.Nonnull;

@EventBusSubscriber(Dist.CLIENT)
public class MRKeybinds {
    public static final Lazy<KeyMapping> RITUAL_MODIFIER = create("ritual_modifier", InputConstants.KEY_LALT);

    @Nonnull
    private static Lazy<KeyMapping> create(String name, int key) {
        return Lazy.of(() ->
                new KeyMapping(name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, key, MythicRitual.MOD_NAME)
        );
    }

    @SubscribeEvent
    public static void register(@Nonnull RegisterKeyMappingsEvent event) {
        event.register(RITUAL_MODIFIER.get());
    }
}
