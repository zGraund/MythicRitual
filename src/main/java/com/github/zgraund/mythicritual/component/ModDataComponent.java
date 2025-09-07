package com.github.zgraund.mythicritual.component;

import com.github.zgraund.mythicritual.MythicRitual;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponent {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MythicRitual.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EntityType<?>>> SOUL_ENTITY_TYPE = REGISTRAR.registerComponentType(
            "entity_type",
            entityTypeBuilder -> entityTypeBuilder.persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
    );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}
