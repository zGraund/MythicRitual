package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.RitualRecipeContext;
import com.github.zgraund.mythicritual.registries.ModParticles;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;

@Mod(MythicRitual.MOD_ID)
public class MythicRitual {
    public static final String MOD_ID = "mythicritual";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MythicRitual(@NotNull IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        ModRecipes.register(modEventBus);
        ModParticles.register(modEventBus);
    }

    @Nonnull
    public static ResourceLocation ID(String path) {return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);}

    // FIXME: ugly in game debugging, to remove asap
    @SubscribeEvent // on the game event bus
    public void useItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.OFF_HAND) return;

        Level level = event.getLevel();
        if (level.isClientSide) return;

        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);
        ItemStack itemStack = event.getItemStack();
        RecipeManager recipes = level.getRecipeManager();
        RecipeType<RitualRecipe> type = ModRecipes.RITUAL_RECIPE_TYPE.get();
        RitualRecipeContext input = new RitualRecipeContext(blockState, pos, itemStack, level, event.getEntity(), event.getHand());

        Optional<RecipeHolder<RitualRecipe>> recipe = recipes.getRecipeFor(type, input, level);

        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().value().execute(input);
        ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
        entity.setDeltaMovement(0, 0.20, 0);
        level.addFreshEntity(entity);

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {LOGGER.info("HELLO from server starting");}
}
