package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
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
        registration.addRecipeCategories(new RitualRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();

        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ModRecipes.RITUAL_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RitualRecipeCategory.RITUAL_RECIPE_JEI_RECIPE_TYPE, ritualRecipes);
    }
}
