package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipe.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.render.AnimatedSpriteRenderer;
import com.mojang.datafixers.util.Either;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class RitualRecipeJEICategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = MythicRitual.ID("ritual_recipe");
    public static final ResourceLocation TEXTURE = MythicRitual.ID("textures/gui/gui_test_2.png");
    public static final ResourceLocation ICON = MythicRitual.ID("textures/gui/ritual_recipe/ritual_icon.png");
    public static final RecipeType<RitualRecipe> TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final int hammerX = 38;
    private final int hammerY = 4;
    private final AnimatedSpriteRenderer consume = new AnimatedSpriteRenderer(
            MythicRitual.ID("smashing_hammer"), 3, 16, 32, 544, 32, 32);
    private final AnimatedSpriteRenderer save = new AnimatedSpriteRenderer(
            MythicRitual.ID("smashing_hammer"), 3, 10, 32, 544, 32, 32);

    private final int infoIconX = 145;
    private final int infoIconY = 57;
    private final IDrawable infoIcon;

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;
    private final int width = 162;
    private final int height = 144;
    private final int ingListCol = 8;

    public RitualRecipeJEICategory(@Nonnull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, width, height);
        this.icon = helper.drawableBuilder(ICON, 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.guiHelper = helper;
        this.infoIcon = helper.drawableBuilder(MythicRitual.ID("textures/gui/sprites/info_icon_small.png"), 0, 0, 16, 16)
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
        builder.addScrollGridWidget(inputSlots.subList(2, inputSlots.size()), ingListCol, 4).setPosition(0, 72);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RitualRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addInputSlot(19, 19)
               .setStandardSlotBackground()
               .addItemStack(recipe.catalyst().asItemStack())
               .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                   Optional<Component> tt = recipe.catalystDescription();
                   tt.ifPresent(component -> tooltip.getLines().addAll(2, List.of(Either.left(component), Either.left(Component.empty()))));
               });
        builder.addInputSlot(73, 19).setStandardSlotBackground().addItemLike(recipe.altar().getBlock().asItem());
        builder.addOutputSlot(127, 19).setOutputSlotBackground().addItemStack(recipe.result().asItemStack());

        recipe.locations().values().forEach(ingredients -> ingredients.forEach(
                ingredient -> builder.addInputSlot().addItemStack(ingredient.asItemStack())
        ));
    }

    @Override
    public void draw(@Nonnull RitualRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        background.draw(guiGraphics);
        IDrawableStatic arrow = guiHelper.getRecipeArrow();
//        arrow.draw(guiGraphics, 43, 18);
        arrow.draw(guiGraphics, 95, 18);
        infoIcon.draw(guiGraphics, infoIconX, infoIconY);
        if (recipe.onTransmute() != ActionOnTransmute.NONE) {
            consume.draw(guiGraphics, hammerX, hammerY);
        } else {
            save.draw(guiGraphics, hammerX, hammerY);
        }
    }

    @Override
    public void getTooltip(@Nonnull ITooltipBuilder tooltip, @Nonnull RitualRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (infoIconX <= mouseX && infoIconX + infoIcon.getWidth() >= mouseX && infoIconY <= mouseY && infoIconY + infoIcon.getHeight() >= mouseY) {
            tooltip.addAll(List.of(recipe.skyAccessDescription(), recipe.dimensionsDescription(), recipe.biomeDescription()));
        }
        if (consume.hover(hammerX, hammerY, mouseX, mouseY)) {
            if (recipe.onTransmute() != ActionOnTransmute.NONE) {
                tooltip.add(Component.literal("The catalyst will be consumed or damaged!").withStyle(ChatFormatting.RED));
            } else {
                tooltip.add(Component.literal("The catalyst is safe!").withStyle(ChatFormatting.GREEN));
            }
        }
    }

    @Override
    public int getHeight() {return height;}

    @Override
    public int getWidth() {return width;}

    @Override
    @Nonnull
    public RecipeType<RitualRecipe> getRecipeType() {return TYPE;}

    @Override
    @Nonnull
    public Component getTitle() {return Component.literal("Ritual");}

    @Override
    public @Nullable IDrawable getIcon() {return icon;}
}
