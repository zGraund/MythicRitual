package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

@JeiPlugin
public class JEIMythicRitualPlugin implements IModPlugin {
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RitualRecipeJEICategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(
                RitualRecipeJEIIngredient.RITUAL_RECIPE_INGREDIENT_JEI_TYPE,
                List.of(),
                new RitualRecipeIngredientHelper(),
                new RitualRecipeIngredientRenderer(),
                RitualRecipeIngredient.CODEC
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ModRecipes.RITUAL_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RitualRecipeJEICategory.RITUAL_RECIPE_JEI_TYPE, ritualRecipes);
    }
}
