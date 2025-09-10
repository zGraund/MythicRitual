package com.github.zgraund.mythicritual;

import com.github.zgraund.mythicritual.component.ModDataComponent;
import com.github.zgraund.mythicritual.ingredient.ModIngredients;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.item.ModItems;
import com.github.zgraund.mythicritual.recipes.RitualRecipe;
import com.github.zgraund.mythicritual.recipes.RitualRecipeContext;
import com.github.zgraund.mythicritual.recipes.ingredients.RitualRecipeOffering;
import com.github.zgraund.mythicritual.registries.ModParticles;
import com.github.zgraund.mythicritual.registries.ModRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import java.util.Arrays;
import java.util.List;
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
        ModIngredients.register(modEventBus);
        ModItems.register(modEventBus);

        ModDataComponent.register(modEventBus);

        ModParticles.register(modEventBus);
    }

    @Nonnull
    public static ResourceLocation ID(String path) {return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);}

    // FIXME: ugly in game debugging, to remove asap
    @SubscribeEvent // on the game event bus
    public void useItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        // FIXME: there's nothing more permanent than a temporary solution
        if (event.getItemStack().getItem() == Items.END_ROD) {
            String validItemsWithComponents = """
                    {
                        "items": ["minecraft:diamond_sword"],
                        "components": {
                            "minecraft:enchantments": {
                                "levels": {
                                    "minecraft:sharpness": 5
                                }
                            }
                        },
                        "count": 1
                    }
                    """;

            // 2. Valid: entity and count
            String validEntity = """
                    {
                        "entity": "minecraft:zombie",
                        "count": 2
                    }
                    """;

            // 3. Invalid: items, components, entity, and count (should fail validation)
            String invalidBoth = """
                    {
                        "items": ["minecraft:diamond_sword"],
                        "components": {
                            "minecraft:enchantments": {
                                "levels": {
                                    "minecraft:sharpness": 5
                                }
                            }
                        },
                        "entity": "minecraft:zombie",
                        "count": 3
                    }
                    """;

            // 4. Invalid: items, entity, and count (should fail validation)
            String invalidItemsAndEntity = """
                    {
                        "items": ["minecraft:iron_sword"],
                        "entity": "minecraft:skeleton",
                        "count": 1
                    }
                    """;

            // 4. Invalid: no items, entity, or count (should fail validation)
            String invalid = """
                    {
                        "count": 1
                    }
                    """;

            List<JsonElement> testJsons = Arrays.asList(
                    JsonParser.parseString(validItemsWithComponents),
                    JsonParser.parseString(validEntity),
                    JsonParser.parseString(invalidBoth),
                    JsonParser.parseString(invalidItemsAndEntity),
                    JsonParser.parseString(invalid)
            );

            for (JsonElement json : testJsons) {
                DataResult<Pair<RitualIngredient, JsonElement>> result =
                        RitualIngredient.CODEC.codec().decode(RegistryOps.create(JsonOps.INSTANCE, event.getLevel().registryAccess()), json);
                LOGGER.debug("\n\n{}\nJson: {}",
                        result.isError() ? "error: " + result.error().get() :
                                "success: " + result.result().get().getFirst(),
                        json
                );
                event.getEntity().sendSystemMessage(Component.literal(result.isError() ? "error" : "success"));
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }
        if (event.getHand() == InteractionHand.OFF_HAND) return;

        Level level = event.getLevel();

        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);
        ItemStack itemStack = event.getItemStack();
        RecipeManager recipes = level.getRecipeManager();
        RecipeType<RitualRecipe> type = ModRecipes.RITUAL_RECIPE_TYPE.get();
        RitualRecipeContext input = new RitualRecipeContext(blockState, pos, itemStack, level, event.getEntity(), event.getHand());

        Optional<RecipeHolder<RitualRecipe>> recipe = recipes.getRecipeFor(type, input, level);

        if (recipe.isEmpty()) return;
        if (!level.isClientSide) {
            RitualRecipeOffering result = recipe.get().value().execute(input);
            Entity entity = result.asEntity(level);
            if (entity == null) return;
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            entity.setDeltaMovement(0, 0.20, 0);
            if (entity instanceof LivingEntity l) {
                Direction dir = event.getEntity().getDirection();
                Direction dir2 = dir.getOpposite();

                entity.setYRot(dir2.toYRot());
                entity.setYBodyRot(dir2.toYRot());
                entity.setYHeadRot(dir2.toYRot());
                entity.setDeltaMovement(0, 0, 0);
            }
            level.addFreshEntity(entity);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {LOGGER.info("HELLO from server starting");}
}
