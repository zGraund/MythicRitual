package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.RitualRecipeInput;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
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
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
    }

    // FIXME: ugly in game debugging, to remove asap
    @SubscribeEvent // on the game event bus
    public void useItemOnBlock(UseItemOnBlockEvent event) {
        if (event.getPlayer() == null) return;
        if (event.getUsePhase() != UseItemOnBlockEvent.UsePhase.BLOCK) return;
        UseOnContext context = event.getUseOnContext();
        Level level = context.getLevel();
        if (level.isClientSide) return;
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        ItemStack itemStack = context.getItemInHand();
        RecipeManager recipes = level.getRecipeManager();
        RecipeType<RitualRecipe> type = ModRecipes.RITUAL_RECIPE_TYPE.get();
//        List<RecipeHolder<RitualRecipe>> t = recipes.getAllRecipesFor(type);
//        LOGGER.debug("all recipes for type {} \n{}", type, t);
        RitualRecipeInput input = new RitualRecipeInput(blockState, pos, itemStack, level);
        Optional<RecipeHolder<RitualRecipe>> optional = recipes.getRecipeFor(
                type,
                input,
                level
        );
        if (optional.isEmpty()) return;
        ItemStack result = optional.get().value().consume(input);
        event.getPlayer().swing(event.getHand(), true);
        ItemEntity entity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                result);
        entity.setDeltaMovement(0, 0.20, 0);
        level.addFreshEntity(entity);
        LOGGER.debug("crafting input {} and result {}", input, result);
        event.cancelWithResult(ItemInteractionResult.SUCCESS);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {LOGGER.info("HELLO from server starting");}
}
