package com.github.zgraund.mythicritual.datagen;

import com.github.zgraund.mythicritual.MythicRitual;
import com.github.zgraund.mythicritual.component.BlockInput;
import com.github.zgraund.mythicritual.component.ItemInput;
import com.github.zgraund.mythicritual.component.ItemOutput;
import com.github.zgraund.mythicritual.core.RitualPosition;
import com.github.zgraund.mythicritual.core.RitualRecipe;
import com.github.zgraund.mythicritual.position.Origin;
import com.github.zgraund.mythicritual.position.Relative;
import com.github.zgraund.mythicritual.position.RelativeDirection;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RitualRecipeProvider extends RecipeProvider {
    public RitualRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput recipeOutput) {
        ItemStack diamond = new ItemStack(Items.DIAMOND);
        add(recipeOutput, "test", new RitualRecipe(
                List.of(
                        new RitualPosition(
                                Origin.INSTANCE,
                                List.of(new BlockInput(BlockPredicate.Builder.block().of(Blocks.STONE).build(), false)),
                                List.of(new ItemOutput(diamond))
                        ),
                        new RitualPosition(
                                Relative.of(
                                        Map.of(RelativeDirection.RIGHT, 2),
                                        Map.of(RelativeDirection.LEFT, 2)
                                ),
                                List.of(new BlockInput(BlockPredicate.Builder.block().of(Blocks.GOLD_BLOCK).build(), true)),
                                List.of()
                        ),
                        new RitualPosition(
                                Relative.of(
                                        Map.of(RelativeDirection.RIGHT, 2, RelativeDirection.UP, 1),
                                        Map.of(RelativeDirection.LEFT, 2, RelativeDirection.UP, 1)
                                ),
                                List.of(new ItemInput(SizedIngredient.of(Items.GOLD_INGOT, 1), 0)),
                                List.of()
                        )
                )
        ));
    }

    protected void add(RecipeOutput output, String id, RitualRecipe recipe) {
        output.accept(MythicRitual.asResource(id), recipe, null);
    }
}
