package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.ingredient.RitualIngredient;
import com.github.zgraund.mythicritual.recipe.ActionOnTransmute;
import com.github.zgraund.mythicritual.recipe.EffectHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = MythicRitual.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(@Nonnull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ModRecipeProvider modRecipeProvider = new ModRecipeProvider(packOutput, lookupProvider);

        RitualRecipeBuilder recipeBuilder = new RitualRecipeBuilder(Blocks.NETHERITE_BLOCK.defaultBlockState(), RitualIngredient.from(EntityType.WITHER_SKELETON))
                .catalyst(RitualIngredient.from(ItemTags.SWORDS))
                .addLocations(List.of(RitualIngredient.from(Items.DIAMOND, 5), RitualIngredient.from(Items.GOLD_INGOT, 10)))
                .addLocations(new Vec3i(1, 0, 0), List.of(RitualIngredient.from(EntityType.ZOMBIE)))
                .dimensions(List.of(Level.OVERWORLD))
                .effect(EffectHelper.LIGHTNING)
                .onTransmute(ActionOnTransmute.DESTROY)
                .needSky(true);

        modRecipeProvider.addRecipe(
                recipeBuilder,
                MythicRitual.ID("nether_stick")
        );

        generator.addProvider(event.includeServer(), modRecipeProvider);
    }
}
