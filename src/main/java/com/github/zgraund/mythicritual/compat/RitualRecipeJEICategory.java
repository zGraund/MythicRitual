package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.util.AnimatedSpriteRenderer;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RitualRecipeJEICategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = MythicRitual.ID("ritual_recipe");
    public static final ResourceLocation TEXTURE = MythicRitual.ID("textures/gui/gui_test_2.png");
    public static final RecipeType<RitualRecipe> TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final int hammerX = 73;
    private final int hammerY = 1;
    private final AnimatedSpriteRenderer consume = new AnimatedSpriteRenderer(
            MythicRitual.ID("consume_trigger"), 50, 2, 16, 48, 16, 16);
    private final AnimatedSpriteRenderer save = new AnimatedSpriteRenderer(
            MythicRitual.ID("consume_trigger"), 50, 1, 16, 48, 16, 16);

    private final int infoIconX = 145;
    private final int infoIconY = 57;
    private final IDrawable infoIcon;

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;
    private final int width = 162;
    private final int height = 144;
    private final int ingListCol = 8;

    public RitualRecipeJEICategory(@NotNull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, width, height);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
        this.guiHelper = helper;
        this.infoIcon = helper.drawableBuilder(MythicRitual.ID("textures/gui/sprites/info_icon_small.png"), 0, 0, 16, 16)
                              .setTextureSize(16, 16)
                              .build();
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        Component text = Component.literal("Ritual Ingredients: ");
        builder.addText(text, 108, 20)
               .setPosition(1, 54)
               .setTextAlignment(VerticalAlignment.CENTER);

        List<IRecipeSlotDrawable> inputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
        builder.addScrollGridWidget(inputSlots.subList(1, inputSlots.size()), ingListCol, 4).setPosition(0, 72);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(19, 19).setStandardSlotBackground().addItemStack(recipe.trigger());
        builder.addSlot(RecipeIngredientRole.CATALYST, 73, 19).setStandardSlotBackground().addItemLike(recipe.target().getBlock().asItem());
        builder.addOutputSlot(127, 19).setOutputSlotBackground().addItemStack(recipe.result());

        // FIXME: ???
        List<ItemRitualRecipeIngredient> i = new ArrayList<>();
        List<MobRitualRecipeIngredient> m = new ArrayList<>();
        recipe.ingredients().forEach(a -> {
            if (a instanceof MobRitualRecipeIngredient x) {
                m.add(x);
            } else if (a instanceof ItemRitualRecipeIngredient y) {
                i.add(y);
            }
        });
        for (ItemRitualRecipeIngredient ing : i) {
            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.TYPE, ing);
        }
        for (int x = 0; x < (ingListCol - (i.size() % ingListCol)) % ingListCol; x++) {
            builder.addInputSlot().addItemStack(ItemStack.EMPTY);
        }
        for (MobRitualRecipeIngredient ing : m) {
            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.TYPE, ing);
        }
    }

    @Override
    public void draw(@NotNull RitualRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        background.draw(guiGraphics);
        IDrawableStatic arrow = guiHelper.getRecipeArrow();
        arrow.draw(guiGraphics, 43, 18);
        arrow.draw(guiGraphics, 95, 18);
        infoIcon.draw(guiGraphics, infoIconX, infoIconY);
        if (recipe.consumeTrigger()) {
            consume.draw(guiGraphics, hammerX, hammerY);
        } else {
            save.draw(guiGraphics, hammerX, hammerY);
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (infoIconX <= mouseX && infoIconX + 16 >= mouseX && infoIconY <= mouseY && infoIconY + 16 >= mouseY) {
            tooltip.addAll(List.of(
                    Component
                            .literal("Ritual need sky access: ")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(Boolean.toString(recipe.needSky())).withStyle(recipe.needSky() ? ChatFormatting.GREEN : ChatFormatting.RED)),
                    Component
                            .literal("Dimensions: ")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component
                                    .literal((recipe.dimensions().isEmpty() ? "All" : recipe.dimensions().stream().map(ResourceKey::location).toList().toString()))
                                    .withStyle(ChatFormatting.GREEN))
            ));
        }
        if (consume.isHover(hammerX, hammerY, mouseX, mouseY)) {
            if (recipe.consumeTrigger()) {
                tooltip.add(Component.literal("The trigger item will be consumed or take damage!").withStyle(ChatFormatting.RED));
            } else {
                tooltip.add(Component.literal("The trigger item is safe!").withStyle(ChatFormatting.GREEN));
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
