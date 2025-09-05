package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeOffering;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeOffering;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
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
                RitualRecipeJEIIngredient.TYPE,
                List.of(),
                new RitualRecipeIngredientHelper(),
                new RitualRecipeIngredientRenderer(),
                RitualRecipeOffering.CODEC
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();
        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ModRecipes.RITUAL_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(RitualRecipeJEICategory.TYPE, ritualRecipes);
        ritualRecipes.forEach(recipe -> {
            List<RitualRecipeOffering> ingredients = recipe.offerings().stream().filter(MobRitualRecipeOffering.class::isInstance).toList();
            if (!ingredients.isEmpty()) {
                registration.addIngredientInfo(
                        ingredients,
                        RitualRecipeJEIIngredient.TYPE,
                        Component.literal("This soul represent the Living Entity that must be sacrificed in the ritual.")
                );
            }
        });
    }
}
