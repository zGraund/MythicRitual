package com.github.zgraund.mythicritual.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record EntityPreviewTooltip(EntityType<?> type, int size) implements ClientTooltipComponent, TooltipComponent {
    @Override
    public int getHeight() {return size + 2;}

    @Override
    public int getWidth(@NotNull Font font) {return size + 2;}

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
        float target = (float) size * (0.65f);

        float scale = target / maxDim;

        entity.setYRot(195.0F);
        entity.setXRot(0.0F);
        entity.setYBodyRot(entity.getYRot());
        entity.yBodyRotO = entity.getYRot();
        entity.setYHeadRot(entity.getYRot());
        entity.yHeadRotO = entity.getYRot();

        int cx = x + size / 2;
        int cy = (int) (y + (size + (height * scale)) / 2);

        int lineColor = 0xFF8C00BF;
        guiGraphics.vLine(x, y, y + size, lineColor);           // Vertical left
        guiGraphics.vLine(x + size, y, y + size, lineColor); // Vertical right
        guiGraphics.hLine(x, x + size, y, lineColor);           // Horizontal top
        guiGraphics.hLine(x, x + size, y + size, lineColor); // Horizontal bottom

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
