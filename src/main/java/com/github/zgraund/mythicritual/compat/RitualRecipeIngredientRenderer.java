package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.util.OffsetHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class RitualRecipeIngredientRenderer implements IIngredientRenderer<RitualRecipeIngredient> {
    private final ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "mob_soul2");
    private final long startTime = System.currentTimeMillis();

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull RitualRecipeIngredient ingredient) {
        switch (ingredient) {
            case ItemRitualRecipeIngredient item -> {
                guiGraphics.renderFakeItem(item.asItemStack(), 0, 0);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, item.asItemStack(), 0, 0);
            }
            case MobRitualRecipeIngredient ignored -> {
                long now = System.currentTimeMillis();
                int msPerCycle = 15 * 50;
                int frameCount = 7;
                long msPassed = (now - startTime) % msPerCycle;
                int frame = (int) Math.floorDiv(msPassed * (frameCount + 1), msPerCycle);

                int width = 16;
                int height = 16;
                guiGraphics.blitSprite(sprite, 16, 128, 0, frame * height, 0, 0, 0, width, height);
            }
        }
    }

    @Contract("_, _ -> new")
    @Override
    @Nonnull
    public @Unmodifiable List<Component> getTooltip(@NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>(2);
        tooltip.add(ingredient.getDisplayName());
        if (!OffsetHelper.isDefault(ingredient.offset())) tooltip.add(OffsetHelper.asComponent(ingredient.offset()).withStyle(ChatFormatting.DARK_GRAY));
        return tooltip;
    }
}
