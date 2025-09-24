package com.github.zgraund.mythicritual.recipe.action;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.List;

public class Actions {
    public static final DeferredRegister<ActionOnTransmute> ACTIONS =
            DeferredRegister.create(ModRegistries.ACTION_REGISTRY, MythicRitual.MOD_ID);

    public static final DeferredHolder<ActionOnTransmute, DamageCatalyst> DAMAGE_CATALYST = register("damage_catalyst", new DamageCatalyst());
    public static final DeferredHolder<ActionOnTransmute, DestroyCatalyst> DESTROY_CATALYST = register("destroy_catalyst", new DestroyCatalyst());
    public static final DeferredHolder<ActionOnTransmute, DestroyAltar> DESTROY_ALTAR = register("destroy_altar", new DestroyAltar());
    public static final DeferredHolder<ActionOnTransmute, DropResult> DROP_RESULT = register("drop_result", new DropResult());
    public static final DeferredHolder<ActionOnTransmute, PlaceResult> PLACE_RESULT = register("place_result", new PlaceResult());
    public static final DeferredHolder<ActionOnTransmute, HurtPlayer> HURT_PLAYER = register("hurt_player", new HurtPlayer());

    public static final List<Holder<ActionOnTransmute>> DEFAULT = List.of(DROP_RESULT);

    @Nonnull
    public static <I extends ActionOnTransmute> DeferredHolder<ActionOnTransmute, I> register(String name, I action) {
        return ACTIONS.register(name, () -> action);
    }

    public static void register(IEventBus eventBus) {
        ACTIONS.register(eventBus);
    }
}
