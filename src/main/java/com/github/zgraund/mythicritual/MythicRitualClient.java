package com.github.zgraund.mythicritual;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import javax.annotation.Nonnull;

@Mod(value = MythicRitual.MOD_ID, dist = Dist.CLIENT)
public class MythicRitualClient {
    public MythicRitualClient(@Nonnull ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
