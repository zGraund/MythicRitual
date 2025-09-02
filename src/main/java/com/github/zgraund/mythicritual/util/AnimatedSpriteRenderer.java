package com.github.zgraund.mythicritual.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Create a drawable sprite from a png file, the texture is assumed to be vertical.
 *
 * @param sprite        The ResourceLocation of the sprite to render, relative to textures/gui/sprites.
 * @param msPerFrame    The time that each frame takes in ms.
 * @param maxFrames     The maximum number of frames the animation has.
 * @param textureWidth  The width of the full sprite file.
 * @param textureHeight The height of the full sprite file.
 * @param frameWidth    The width of the frame to render.
 * @param frameHeight   the height of the frame to render.
 */
public record AnimatedSpriteRenderer(ResourceLocation sprite, int msPerFrame, int maxFrames, int textureWidth, int textureHeight, int frameWidth, int frameHeight) {
    public AnimatedSpriteRenderer(ResourceLocation sprite, int msPerFrame, int maxFrames, int textureWidth, int textureHeight, int frameWidth, int frameHeight) {
        this.sprite = sprite;
        this.msPerFrame = msPerFrame * 50;
        this.maxFrames = maxFrames;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public int getFrame() {
        long msPassed = System.currentTimeMillis() % msPerFrame;
        return (int) Math.floorDiv(msPassed * (maxFrames + 1), msPerFrame);
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

    public boolean isHover(int sourceX, int sourceY, double mouseX, double mouseY) {
        return sourceX <= mouseX &&
               sourceX + frameWidth >= mouseX &&
               sourceY <= mouseY &&
               sourceY + frameHeight >= mouseY;
    }
}
