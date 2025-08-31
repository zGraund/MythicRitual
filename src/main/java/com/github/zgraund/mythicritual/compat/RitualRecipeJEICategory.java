package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
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
import java.util.ArrayList;
import java.util.List;

public class RitualRecipeJEICategory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "ritual_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "textures/gui/gui_test_2.png");
    public static final RecipeType<RitualRecipe> RITUAL_RECIPE_JEI_TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final int width = 162;
    private final int height = 144;
    private final int ingListCol = 8;

    public RitualRecipeJEICategory(@NotNull IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, width, height);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        Component text = Component.literal("Ritual Ingredients: ");
        builder.addText(text, 108, 20)
               .setPosition(1, 54)
               .setTextAlignment(VerticalAlignment.CENTER);

        builder.addRecipeArrow().setPosition(43, 18);
        builder.addRecipeArrow().setPosition(95, 18);

        List<IRecipeSlotDrawable> inputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
        inputSlots.subList(0, 2).clear();

        builder.addScrollGridWidget(inputSlots, ingListCol, 4).setPosition(0, 72);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RitualRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(19, 19).setStandardSlotBackground().addItemStack(recipe.trigger());
        builder.addInputSlot(73, 19).setStandardSlotBackground().addItemLike(recipe.target().getBlock().asItem());
        builder.addOutputSlot(127, 19).setOutputSlotBackground().addItemStack(recipe.result());

        // FIXME: ???
        List<ItemRitualRecipeIngredient> i = new ArrayList<>();
        List<MobRitualRecipeIngredient> m = new ArrayList<>();
        recipe.ingredients().forEach(a -> {
            if (a instanceof MobRitualRecipeIngredient x) {
                m.add(x);
            } else if (a instanceof ItemRitualRecipeIngredient y) {i.add(y);}
        });
//        for (RitualRecipeIngredient ingredient : recipe.ingredients()) {
//            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.RITUAL_RECIPE_INGREDIENT_JEI_TYPE, ingredient);
//        }
        for (ItemRitualRecipeIngredient ing : i) {
            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.RITUAL_RECIPE_INGREDIENT_JEI_TYPE, ing);
        }
        for (int x = 0; x < (ingListCol - (i.size() % ingListCol)) % ingListCol; x++) {
            builder.addInputSlot().addItemStack(ItemStack.EMPTY);
        }
        for (MobRitualRecipeIngredient ing : m) {
            builder.addInputSlot().addIngredient(RitualRecipeJEIIngredient.RITUAL_RECIPE_INGREDIENT_JEI_TYPE, ing);
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
