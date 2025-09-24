package com.github.zgraund.mythicritual.event;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.attachment.ModDataAttachments;
import com.github.zgraund.mythicritual.recipe.ModRecipes;
import com.github.zgraund.mythicritual.recipe.RitualRecipe;
import com.github.zgraund.mythicritual.recipe.RitualRecipeContext;
import com.github.zgraund.mythicritual.registries.ModRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import javax.annotation.Nonnull;
import java.util.Optional;

@EventBusSubscriber(modid = MythicRitual.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void useItemOnBlock(@Nonnull UseItemOnBlockEvent event) {
        if (event.getPlayer() == null
            || event.getUsePhase() != UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK
            || event.getHand() == InteractionHand.OFF_HAND) return;

        Level level = event.getLevel();
        RecipeManager recipeManager = level.getRecipeManager();
        RecipeType<RitualRecipe> type = ModRecipes.RITUAL_RECIPE_TYPE.get();
        RitualRecipeContext context = new RitualRecipeContext(event);
        Optional<RecipeHolder<RitualRecipe>> recipe = recipeManager.getRecipeFor(type, context, level);

        if (recipe.isEmpty()) return;

        if (!level.isClientSide()) {
            recipe.get().value().assemble(context, level.registryAccess());
        }
        event.cancelWithResult(ItemInteractionResult.sidedSuccess(event.getLevel().isClientSide));
    }

    @SubscribeEvent
    public static void onEntitySacrifice(@Nonnull LivingDropsEvent event) {
        event.setCanceled(event.getEntity().getData(ModDataAttachments.IS_SACRIFICED));
    }

    @SubscribeEvent
    public static void onEntitySacrifice(@Nonnull LivingExperienceDropEvent event) {
        event.setCanceled(event.getEntity().getData(ModDataAttachments.IS_SACRIFICED));
    }

    @SubscribeEvent
    public static void registerRegistries(@Nonnull NewRegistryEvent event) {
        event.register(ModRegistries.ACTION_REGISTRY);
    }
}
