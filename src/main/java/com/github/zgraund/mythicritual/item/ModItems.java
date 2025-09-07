package com.github.zgraund.mythicritual.item;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MythicRitual.MOD_ID);

    public static final DeferredItem<Item> SOUL =
            ITEMS.registerItem("soul", SoulItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
