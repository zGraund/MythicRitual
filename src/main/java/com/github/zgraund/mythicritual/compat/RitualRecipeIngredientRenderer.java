package com.github.zgraund.mythicritual.compat;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.recipes.ingredients.ItemRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.MobRitualRecipeIngredient;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeIngredient;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class RitualRecipeIngredientRenderer implements IIngredientRenderer<RitualRecipeIngredient> {
    private final ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "mob_soul_jei");
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

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        List<Component> components = getTooltip(ingredient, tooltipFlag);
        tooltip.add(components.getFirst());
        tooltip.add(components.get(1));
        if (ingredient instanceof MobRitualRecipeIngredient entity) {
            if (tooltipFlag.hasShiftDown()) {
                tooltip.clear();
                tooltip.add(new EntityPreviewTooltip(entity.asEntityType(), 72));
            } else {
                tooltip.add(Component.literal("Shift: view.").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    @Contract("_, _ -> new")
    @Override
    @Nonnull
    public @Unmodifiable List<Component> getTooltip(@NotNull RitualRecipeIngredient ingredient, @NotNull TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>(2);
        tooltip.add(ingredient.getDisplayName());
        tooltip.add(OffsetHelper.asComponent(ingredient.offset()).withStyle(ChatFormatting.DARK_GRAY));
        return tooltip;
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
            float target = (float) size * (0.6f);

            float scale = target / maxDim;

            entity.setYRot(210.0F);
            entity.setXRot(0.0F);
            entity.yBodyRot = entity.getYRot();
            entity.yHeadRot = entity.getYRot();

//            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(MythicRitual.MOD_ID, "textures/gui/gui_test_2.png"), x, y, 0, 0, size, size);
//            guiGraphics.fillGradient(RenderType.LIGHTNING, x, y, x + size, y + size, 0xffffffff, 0xAFD8F0ff, 0);

            int cx = x + size / 2;
            int cy = (int) (y + (size + (height * scale)) / 2);

            InventoryScreen.renderEntityInInventory(
                    guiGraphics,
                    cx, cy, scale,
                    new Vector3f(),
                    new Quaternionf().rotateLocalZ((float) Math.PI),
                    null,
                    entity
            );
        }
    }
}
