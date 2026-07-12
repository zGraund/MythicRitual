package com.github.zgraund.mythicritual.registry;

import com.github.zgraund.mythicritual.api.registry.MythicRitualRegistry;
import com.github.zgraund.mythicritual.components.BlockInput;
import com.github.zgraund.mythicritual.components.BlockOutput;
import com.github.zgraund.mythicritual.components.RitualInput;
import com.github.zgraund.mythicritual.components.RitualOutput;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RitualComponents {
    private static final DeferredRegister<MapCodec<? extends RitualInput>> INPUT =
            DeferredRegister.create(MythicRitualRegistry.INPUT_KEY, "input");
    private static final DeferredRegister<MapCodec<? extends RitualOutput>> OUTPUT =
            DeferredRegister.create(MythicRitualRegistry.OUTPUT_KEY, "output");

    static {
        INPUT.register("block", () -> BlockInput.CODEC);

        OUTPUT.register("block", () -> BlockOutput.CODEC);
    }

    public static void register(IEventBus bus) {
        INPUT.register(bus);
        OUTPUT.register(bus);
    }
}
