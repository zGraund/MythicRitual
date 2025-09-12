package com.github.zgraund.mythicritual.event;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.attachment.ModDataAttachments;
import com.github.zgraund.mythicritual.recipe.ModRecipes;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = MythicRitual.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void useItemOnBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (event.getHand() == InteractionHand.OFF_HAND) return;

        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos origin = event.getPos();
        BlockState altar = level.getBlockState(origin);
        ItemStack catalyst = event.getItemStack();
        RecipeManager recipes = level.getRecipeManager();

        RecipeType<RitualRecipe> type = ModRecipes.RITUAL_RECIPE_TYPE.get();
        RitualRecipeContext context = new RitualRecipeContext(altar, origin, catalyst, level, event.getEntity(), event.getHand());
        Optional<RecipeHolder<RitualRecipe>> recipe = recipes.getRecipeFor(type, context, level);

        if (recipe.isEmpty()) return;
        if (!level.isClientSide) {
            Entity entity = recipe.get().value().getResultEntity(context, level.registryAccess());
            if (entity == null) return;
            entity.setPos(origin.getX() + 0.5, origin.getY() + 1, origin.getZ() + 0.5);
            entity.setDeltaMovement(0, 0.20, 0);
            if (entity instanceof LivingEntity) {
                Direction entityDirection = player.getDirection().getOpposite();
                entity.setYRot(entityDirection.toYRot());
                entity.setYBodyRot(entityDirection.toYRot());
                entity.setYHeadRot(entityDirection.toYRot());
                entity.setDeltaMovement(0, 0, 0);
            }
            level.addFreshEntity(entity);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntitySacrifice(@NotNull LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasData(ModDataAttachments.IS_SACRIFICED)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntitySacrifice(@NotNull LivingExperienceDropEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasData(ModDataAttachments.IS_SACRIFICED)) event.setCanceled(true);
    }
}
