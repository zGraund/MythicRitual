package com.github.zgraund.mythicritual.recipes.ingredients;

import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.mojang.serialization.Codec;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.List;

public sealed interface RitualRecipeOffering permits ItemRitualRecipeOffering, MobRitualRecipeOffering {

    Codec<RitualRecipeOffering> CODEC = ResourceLocation.CODEC.dispatch(
            RitualRecipeOffering::type,
            type -> {
                if (BuiltInRegistries.ITEM.containsKey(type)) return ItemRitualRecipeOffering.CODEC;
                if (!type.equals(ResourceLocation.parse("minecraft:item")) && BuiltInRegistries.ENTITY_TYPE.containsKey(type))
                    return MobRitualRecipeOffering.CODEC;
                throw new IllegalArgumentException("Unknown ritual ingredient type: " + type);
            }
    );

    ResourceLocation type();

    Vec3i offset();

    default Boolean test(Entity entity) {return test(new EntityConsumer(entity, 0));}

    Boolean test(EntityConsumer entityConsumer);

    default int quantity() {return 1;}

    Component getDisplayName();

    List<Component> getTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag);

    // TODO: BlockState as ingredient
    // Boolean test(BlockState block);
}
