package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.render.AnimatedSpriteRenderer;
import com.github.zgraund.mythicritual.util.OffsetHelpers;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RitualRecipeJEICategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = MythicRitual.ID("ritual_recipe");
    public static final RecipeType<RitualRecipe> TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final AnimatedSpriteRenderer hammer = new AnimatedSpriteRenderer(
            MythicRitual.ID("smashing_hammer_big"), 1, 28, 32, 896, 32, 32);

    private final int infoIconX = 145;
    private final int infoIconY = 57;
    private final IDrawable infoIcon;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;

    public RitualRecipeJEICategory(@Nonnull IGuiHelper helper) {
        this.guiHelper = helper;
        this.icon = helper.drawableBuilder(MythicRitual.ID("textures/gui/ritual_recipe/ritual_icon.png"), 0, 0, 16, 16)
                          .setTextureSize(16, 16)
                          .build();
        this.infoIcon = helper.drawableBuilder(MythicRitual.ID("textures/gui/ritual_recipe/info_icon_small.png"), 0, 0, 16, 16)
                              .setTextureSize(16, 16)
                              .build();
    }

    @Override
    public void createRecipeExtras(@Nonnull IRecipeExtrasBuilder builder, @Nonnull RitualRecipe recipe, @Nonnull IFocusGroup focuses) {
        Component text = Component.literal("Ritual Offerings: ");
        builder.addText(text, 108, 20)
               .setPosition(1, 54)
               .setTextAlignment(VerticalAlignment.CENTER);

        List<IRecipeSlotDrawable> inputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
        builder.addScrollGridWidget(inputSlots.subList(2, inputSlots.size()), 8, 4).setPosition(0, 72);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RitualRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addInputSlot(19, 19).setStandardSlotBackground().addItemStacks(recipe.catalyst().getItems().toList());
        builder.addInputSlot(73, 19).setStandardSlotBackground().addItemLike(recipe.altar().getBlock().asItem());
        builder.addOutputSlot(127, 19).setOutputSlotBackground().addItemStacks(recipe.result().getItems().toList());

        recipe.locations()
              .forEach((offset, ingredients) -> ingredients.forEach(ingredient ->
                      builder.addInputSlot()
                             .addItemStacks(ingredient.getItems().toList())
                             .addRichTooltipCallback((recipeSlotView, tooltip) ->
                                     tooltip.add(OffsetHelpers.asComponent(offset)))
              ));
    }

    @Override
    public void draw(@Nonnull RitualRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IDrawableStatic arrow = guiHelper.getRecipeArrow();
        arrow.draw(guiGraphics, 95, 18);
        infoIcon.draw(guiGraphics, infoIconX, infoIconY);
        hammer.draw(guiGraphics, 35, 2);
    }

    @Override
    public void getTooltip(@Nonnull ITooltipBuilder tooltip, @Nonnull RitualRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (infoIconX <= mouseX && infoIconX + infoIcon.getWidth() >= mouseX && infoIconY <= mouseY && infoIconY + infoIcon.getHeight() >= mouseY) {
            tooltip.add(Component.translatable("info.hover.ritual.info"));
            tooltip.addAll(recipe.actionDescriptions());
            tooltip.add(Component.empty());
            tooltip.addAll(List.of(recipe.skyAccessDescription(), recipe.dimensionsDescription(), recipe.biomeDescription()));
        }
    }

    @Override
    public int getHeight() {return 144;}

    @Override
    public int getWidth() {return 162;}

    @Override
    @Nonnull
    public RecipeType<RitualRecipe> getRecipeType() {return TYPE;}

    @Override
    @Nonnull
    public Component getTitle() {return Component.literal("Ritual");}

    @Override
    @Nullable
    public IDrawable getIcon() {return icon;}

    @SuppressWarnings("unused")
    private void drawDebugBackground(GuiGraphics guiGraphics) {
        // Debug background for alignment
        ResourceLocation texture = MythicRitual.ID("textures/gui/debug_background.png");
        IDrawable background = guiHelper.createDrawable(texture, 0, 0, getWidth(), getHeight());
        background.draw(guiGraphics);
    }
}
