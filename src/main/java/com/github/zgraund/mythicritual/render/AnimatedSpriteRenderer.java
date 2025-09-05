package com.github.zgraund.mythicritual.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Create a drawable sprite from a png file, the texture is expected to be a vertical spritesheet.
 *
 * @param sprite Relative to textures/gui/sprites/[name].png.
 */
public record AnimatedSpriteRenderer(ResourceLocation sprite, int ticksPerFrame, int maxFrames, int textureWidth, int textureHeight, int frameWidth, int frameHeight) {
    public AnimatedSpriteRenderer(ResourceLocation sprite, int ticksPerFrame, int maxFrames, int textureWidth, int textureHeight, int frameWidth, int frameHeight) {
        this.sprite = sprite;
        this.ticksPerFrame = ticksPerFrame * 50;
        this.maxFrames = maxFrames;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public int getFrame() {
        return (int) ((System.currentTimeMillis() / ticksPerFrame) % maxFrames);
    }

    public void draw(@NotNull GuiGraphics guiGraphics, int screenX, int screenY, int screenZ) {
        guiGraphics.blitSprite(
                sprite, textureWidth, textureHeight,
                0, frameHeight * getFrame(),
                screenX, screenY, screenZ,
                frameWidth, frameHeight);
    }

    public void draw(@NotNull GuiGraphics guiGraphics, int screenX, int screenY) {
        draw(guiGraphics, screenX, screenY, 0);
    }

    public void draw(@NotNull GuiGraphics guiGraphics) {
        draw(guiGraphics, 0, 0, 0);
    }

    public boolean hover(int sourceX, int sourceY, double mouseX, double mouseY) {
        return sourceX <= mouseX &&
               sourceX + frameWidth >= mouseX &&
               sourceY <= mouseY &&
               sourceY + frameHeight >= mouseY;
    }
}
