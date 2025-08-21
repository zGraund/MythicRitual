package com.github.zgraund.mythicritual.registries;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.RitualRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MythicRitual.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MythicRitual.MOD_ID);

    public static final Supplier<RecipeSerializer<RitualRecipe>> RITUAL_RECIPE_SERIALIZER =
            SERIALIZERS.register("ritual_recipe", RitualRecipeSerializer::new);
    public static final Supplier<RecipeType<RitualRecipe>> RITUAL_RECIPE_TYPE =
            TYPES.register("ritual_recipe", RecipeType::simple);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
