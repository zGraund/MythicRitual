package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
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
import java.util.List;

public class RitualRecipeJEICategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "ritual_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "textures/gui/gui_test_2.png");
    public static final RecipeType<RitualRecipe> RITUAL_RECIPE_JEI_TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final int width = 162;
    private final int height = 144;

    public RitualRecipeJEICategory(@NotNull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, width, height);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RitualRecipe recipe, IFocusGroup focuses) {
        Component text = Component.literal("Ritual Ingredients: ");
        builder.addText(text, 108, 20)
               .setPosition(19, 54)
               .setTextAlignment(VerticalAlignment.CENTER);

        List<IRecipeSlotDrawable> inputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
        inputSlots.subList(0, 2).clear();

        builder.addScrollGridWidget(inputSlots, 6, 3).setPosition(18, 72);
//        builder.addScrollGridWidget(inputSlots, 7, 4).setPosition(18, 72);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot().setPosition(19, 19).addItemStack(recipe.trigger());
        builder.addInputSlot().setPosition(73, 19).addItemLike(recipe.target().getBlock().asItem());
        builder.addOutputSlot().setPosition(127, 19).addItemStack(recipe.result());
        for (RitualRecipeIngredient ingredient : recipe.ingredients()) {
            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.RITUAL_RECIPE_INGREDIENT_JEI_TYPE, ingredient);
        }
    }

    @Override
    public void draw(@NotNull RitualRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        background.draw(guiGraphics);
    }

    @Override
    public int getHeight() {return height;}

    @Override
    public int getWidth() {return width;}

    @Override
    @Nonnull
    public RecipeType<RitualRecipe> getRecipeType() {return RITUAL_RECIPE_JEI_TYPE;}

    @Override
    @Nonnull
    public Component getTitle() {return Component.literal("Ritual");}

    @Override
    public @Nullable IDrawable getIcon() {return icon;}
}
