package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
import com.github.zgraund.mythicritual.util.AnimatedSpriteRenderer;
import com.github.zgraund.mythicritual.util.OffsetHelper;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

public final class RitualRecipeIngredientRenderer implements IIngredientRenderer<RitualRecipeIngredient> {
    private final AnimatedSpriteRenderer mobSoul = new AnimatedSpriteRenderer(
            MythicRitual.ID("mob_soul_jei"),
            15, 7,
            16, 128,
            16, 16
    );

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull RitualRecipeIngredient ingredient) {
        switch (ingredient) {
            case ItemRitualRecipeIngredient item -> {
                guiGraphics.renderFakeItem(item.asItemStack(), 0, 0);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, item.asItemStack(), 0, 0);
            }
            case MobRitualRecipeIngredient ignored -> {
                mobSoul.draw(guiGraphics);
            }
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
                OffsetHelper.asComponent(ingredient.offset()).withStyle(ChatFormatting.DARK_GRAY)
        );
    }

    public record EntityPreviewTooltip(EntityType<?> type, int size) implements ClientTooltipComponent, TooltipComponent {
        @Override
        public int getHeight() {return size + 5;}

        @Override
        public int getWidth(@NotNull Font font) {return size + 5;}

        @Override
        public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity e = type.create(level);
            if (!(e instanceof LivingEntity entity)) return;

            if (entity instanceof Mob m) m.setNoAi(true);

            float width = type.getWidth();
            float height = type.getHeight();
            float maxDim = Math.max(width, height);
            float target = (float) size * (0.7f);

            float scale = target / maxDim;

            entity.setYRot(195.0F);
            entity.setXRot(0.0F);
            entity.setYBodyRot(entity.getYRot());
            entity.yBodyRotO = entity.getYRot();
            entity.setYHeadRot(entity.getYRot());
            entity.yHeadRotO = entity.getYRot();

            int cx = x + size / 2;
            int cy = (int) (y + (size + (height * scale)) / 2);

            guiGraphics.enableScissor(x, y, x + size, y + size);
            InventoryScreen.renderEntityInInventory(
                    guiGraphics,
                    cx, cy, scale,
                    new Vector3f(),
                    new Quaternionf().rotateLocalZ((float) Math.PI),
                    null,
                    entity
            );
            guiGraphics.disableScissor();
        }
    }
}
