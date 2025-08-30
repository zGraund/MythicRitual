package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RitualRecipeIngredientRenderer() implements IIngredientRenderer<RitualRecipeIngredient> {
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull RitualRecipeIngredient ingredient) {
        switch (ingredient) {
            case ItemRitualRecipeIngredient item -> guiGraphics.renderItem(item.asItemStack(), 0, 0);
            case MobRitualRecipeIngredient entity -> {
                guiGraphics.renderItem(new ItemStack(Items.DEBUG_STICK), 0, 0);
//                if (Minecraft.getInstance().level != null) {
//                    LivingEntity toRender = (LivingEntity) entity.asEntityType().create(Minecraft.getInstance().level);
//                    if (toRender instanceof Mob m) m.setNoAi(true);
//                    InventoryScreen.renderEntityInInventory(
//                            guiGraphics,
//                            8, 15, 18,
////                            new Vector3f(0.45f, 0.8f, 0),
//                            new Vector3f(0.0f, 0.0f, 0.0f),
//                            new Quaternionf().rotationXYZ((float) Math.PI, 0, 0.0F),
//                            null,
//                            toRender
//                    );
//                }
            }
        }
    }

    @Override
    public List<Component> getTooltip(@NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        return List.of(Component.literal(ingredient.offset().toString()));
    }
}
