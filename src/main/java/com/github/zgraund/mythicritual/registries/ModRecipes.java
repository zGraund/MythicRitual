package com.github.zgraund.mythicritual.registries;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes_old.RitualRecipe;
import com.github.zgraund.mythicritual.recipes_old.RitualRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES_OLD = DeferredRegister.create(Registries.RECIPE_TYPE, MythicRitual.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS_OLD = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MythicRitual.MOD_ID);

    public static final Supplier<RecipeSerializer<RitualRecipe>> RITUAL_RECIPE_SERIALIZER_OLD =
            SERIALIZERS_OLD.register("ritual_recipe_old", RitualRecipeSerializer::new);
    public static final Supplier<RecipeType<RitualRecipe>> RITUAL_RECIPE_TYPE_OLD =
            TYPES_OLD.register("ritual_recipe_old", RecipeType::simple);

    public static void register(IEventBus eventBus) {
        SERIALIZERS_OLD.register(eventBus);
        TYPES_OLD.register(eventBus);
    }
}
