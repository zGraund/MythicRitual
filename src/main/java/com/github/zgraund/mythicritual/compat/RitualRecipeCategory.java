package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class RitualRecipeCategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "ritual_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "textures/gui/ritual_recipe/image.png");
    public static final RecipeType<RitualRecipe> RITUAL_RECIPE_JEI_RECIPE_TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RitualRecipeCategory(@NotNull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 165);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
    }

    @Override
    @Nonnull
    public RecipeType<RitualRecipe> getRecipeType() {
        return RITUAL_RECIPE_JEI_RECIPE_TYPE;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return Component.literal("Ritual");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 50).addItemStack(recipe.trigger());
        builder.addSlot(RecipeIngredientRole.INPUT, 100, 50).addItemLike(recipe.target().getBlock().asItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 100).addItemStack(recipe.result());
    }

    @Override
    public void draw(@NotNull RitualRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        background.draw(guiGraphics);
    }
}
