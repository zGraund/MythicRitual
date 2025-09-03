package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.render.AnimatedSpriteRenderer;
import com.github.zgraund.mythicritual.render.EntityPreviewTooltip;
import com.github.zgraund.mythicritual.util.OffsetHelpers;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.List;

public final class RitualRecipeIngredientRenderer implements IIngredientRenderer<RitualRecipeIngredient> {
    private final AnimatedSpriteRenderer mobSoul = new AnimatedSpriteRenderer(
            MythicRitual.ID("mob_soul_jei"),
            3, 7,
            16, 128,
            16, 16
    );

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull RitualRecipeIngredient ingredient) {
        switch (ingredient) {
            case ItemRitualRecipeIngredient i -> {
                ItemStack item = i.asItemStack();
                guiGraphics.renderFakeItem(item, 0, 0);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, item, 0, 0);
            }
            case MobRitualRecipeIngredient ignored -> mobSoul.draw(guiGraphics);
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        List<Component> components = getTooltip(ingredient, tooltipFlag);
        tooltip.add(components.getFirst());
        tooltip.add(components.get(1));
        if (ingredient instanceof MobRitualRecipeIngredient entity) {
            if (tooltipFlag.hasShiftDown()) {
                tooltip.clear();
                tooltip.add(new EntityPreviewTooltip(entity.asEntityType(), 90));
            } else {
                tooltip.add(Component.literal("Shift: view.").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    @Override
    @Nonnull
    public @Unmodifiable List<Component> getTooltip(@NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        return List.of(
                ingredient.getDisplayName(),
                OffsetHelpers.asComponent(ingredient.offset()).withStyle(ChatFormatting.DARK_GRAY)
        );
    }
}
