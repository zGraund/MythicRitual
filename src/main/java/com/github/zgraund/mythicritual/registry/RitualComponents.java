package com.github.zgraund.mythicritual.registry;

import com.github.zgraund.mythicritual.api.component.RitualInput;
import com.github.zgraund.mythicritual.api.component.RitualOutput;
import com.github.zgraund.mythicritual.api.registry.MythicRitualRegistries;
import com.github.zgraund.mythicritual.component.*;
import com.github.zgraund.mythicritual.util.RegistryUtils;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RitualComponents {
    private static final DeferredRegister<MapCodec<? extends RitualInput<?>>> INPUT =
            RegistryUtils.create(MythicRitualRegistries.RITUAL_INPUT);
    private static final DeferredRegister<MapCodec<? extends RitualOutput>> OUTPUT =
            RegistryUtils.create(MythicRitualRegistries.RITUAL_OUTPUT);

    static {
        INPUT.register("block", () -> BlockInput.CODEC);
        INPUT.register("entity", () -> EntityInput.CODEC);
        INPUT.register("item", () -> ItemInput.CODEC);

        OUTPUT.register("block", () -> BlockOutput.CODEC);
        OUTPUT.register("entity", () -> EntityOutput.CODEC);
        OUTPUT.register("item", () -> ItemOutput.CODEC);
    }

    public static void register(IEventBus bus) {
        INPUT.register(bus);
        OUTPUT.register(bus);
    }
}
