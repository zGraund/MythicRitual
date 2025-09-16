package com.github.zgraund.mythicritual.ingredient;

import com.github.zgraund.mythicritual.MythicRitual;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModIngredients {
    public static final DeferredRegister<IngredientType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, MythicRitual.MOD_ID);

    public static final Supplier<IngredientType<RitualIngredient>> RITUAL_INGREDIENT =
            TYPES.register("ritual_offering", () -> new IngredientType<>(RitualIngredient.CODEC));

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
    }
}
