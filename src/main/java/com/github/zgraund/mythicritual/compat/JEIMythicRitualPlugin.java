package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.item.ModItems;
import com.github.zgraund.mythicritual.recipe.ModRecipes;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@JeiPlugin
public class JEIMythicRitualPlugin implements IModPlugin {
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return MythicRitual.ID("jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RitualRecipeJEICategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerExtraIngredients(@NotNull IExtraIngredientRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();
        List<ItemStack> souls = recipeManager
                .getAllRecipesFor(ModRecipes.RITUAL_RECIPE_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .flatMap(recipe -> Stream.concat(recipe.getCustomIngredients(), Stream.of(recipe.result())))
                .flatMap(RitualIngredient::getItems)
                .filter(item -> item.is(ModItems.SOUL))
                .toList();
        registration.addExtraItemStacks(souls);
        registration.addExtraItemStacks(List.of(new ItemStack(ModItems.SOUL.get())));
    }

    @Override
    public void registerIngredientAliases(@NotNull IIngredientAliasRegistration registration) {
        registration.addAlias(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.SOUL.get()), "Soul");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.SOUL.get(), new SoulSubtypeInterpreter());
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();
        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ModRecipes.RITUAL_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RitualRecipeJEICategory.TYPE, ritualRecipes);
    }

    public static class SoulSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
        @Override
        @Nullable
        public Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
            return ingredient.get(ModDataComponents.SOUL_ENTITY_TYPE);
        }

        @Nonnull
        @Override
        public String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
            return "";
        }
    }
}
