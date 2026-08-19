package com.github.zgraund.mythicritual.registry;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.core.RitualRecipe;
import com.github.zgraund.mythicritual.util.RegistryUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class MRRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = RegistryUtils.create(Registries.RECIPE_TYPE);
    public static final Supplier<RecipeType<RitualRecipe>> RITUAL_TYPE = type("ritual");
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryUtils.create(Registries.RECIPE_SERIALIZER);
    public static final Supplier<RecipeSerializer<RitualRecipe>> RITUAL_SERIALIZER = serializer("ritual", RitualRecipe.Serializer::new);

    @Nonnull
    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> type(String id) {
        return RECIPE_TYPES.register(id, () -> RecipeType.simple(MythicRitual.asResource(id)));
    }

    @Nonnull
    private static <T extends RecipeSerializer<?>> DeferredHolder<RecipeSerializer<?>, T> serializer(String id, Supplier<T> factory) {
        return RECIPE_SERIALIZERS.register(id, factory);
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
