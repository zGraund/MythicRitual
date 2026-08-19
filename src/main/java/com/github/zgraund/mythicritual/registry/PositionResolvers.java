package com.github.zgraund.mythicritual.registry;

import com.github.zgraund.mythicritual.api.position.PositionResolver;
import com.github.zgraund.mythicritual.api.registry.MythicRitualRegistries;
import com.github.zgraund.mythicritual.position.Cardinal;
import com.github.zgraund.mythicritual.position.Origin;
import com.github.zgraund.mythicritual.position.Relative;
import com.github.zgraund.mythicritual.position.WorldAbsolute;
import com.github.zgraund.mythicritual.util.RegistryUtils;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PositionResolvers {
    private static final DeferredRegister<MapCodec<? extends PositionResolver>> RESOLVERS =
            RegistryUtils.create(MythicRitualRegistries.POSITION_RESOLVER);

    static {
        RESOLVERS.register("origin", () -> Origin.CODEC);
        RESOLVERS.register("world_absolute", () -> WorldAbsolute.CODEC);
        RESOLVERS.register("cardinal", () -> Cardinal.CODEC);
        RESOLVERS.register("relative", () -> Relative.CODEC);
        // TODO:
        //  dimensionrelative, dimensionabsolute
        //
    }

    public static void register(IEventBus bus) {
//        RESOLVERS.register(bus);
    }
}
