package com.github.zgraund.mythicritual.compat.ritual_recipe_old;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes_old.ingredients.ItemRitualRecipeOffering;
import com.github.zgraund.mythicritual.recipes_old.ingredients.MobRitualRecipeOffering;
import com.github.zgraund.mythicritual.recipes_old.ingredients.RitualRecipeOffering;
import com.github.zgraund.mythicritual.render.AnimatedSpriteRenderer;
import com.github.zgraund.mythicritual.render.EntityPreviewTooltip;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.List;

public final class RitualRecipeIngredientRenderer implements IIngredientRenderer<RitualRecipeOffering> {
    private final AnimatedSpriteRenderer mobSoul = new AnimatedSpriteRenderer(
            MythicRitual.ID("mob_soul_jei"),
            3, 7,
            16, 128,
            16, 16
    );

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull RitualRecipeOffering ingredient) {
        switch (ingredient) {
            case ItemRitualRecipeOffering i -> {
                ItemStack item = i.asItemStack();
                guiGraphics.renderFakeItem(item, 0, 0);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, item, 0, 0);
            }
            case MobRitualRecipeOffering ignored -> mobSoul.draw(guiGraphics);
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualRecipeOffering ingredient, @NotNull TooltipFlag tooltipFlag) {
        List<Component> components = getTooltip(ingredient, tooltipFlag);
        tooltip.addAll(components);
        if (ingredient instanceof MobRitualRecipeOffering entity) {
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
    public @Unmodifiable List<Component> getTooltip(@NotNull RitualRecipeOffering ingredient, @NotNull TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
        return ingredient.getTooltipLines(tooltipContext, player, tooltipFlag);
    }
}
