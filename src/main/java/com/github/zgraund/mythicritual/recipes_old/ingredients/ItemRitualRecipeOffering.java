package com.github.zgraund.mythicritual.recipes_old.ingredients;

import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public record ItemRitualRecipeOffering(
        ResourceLocation type,
        int quantity,
        Vec3i offset
) implements RitualRecipeOffering {
    public static final MapCodec<ItemRitualRecipeOffering> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(ItemRitualRecipeOffering::type),
                    ExtraCodecs.intRange(1, 99).fieldOf("quantity").orElse(1).forGetter(ItemRitualRecipeOffering::quantity),
                    Vec3i.CODEC.optionalFieldOf("pos", new Vec3i(0, 1, 0)).forGetter(ItemRitualRecipeOffering::offset)
            ).apply(inst, ItemRitualRecipeOffering::new)
    );

    @Override
    @Nonnull
    public Boolean test(@NotNull EntityConsumer entityConsumer) {
        Entity entity = entityConsumer.entity();
        int used = entityConsumer.used();
        if (!(entity instanceof ItemEntity itemEntity)) return false;
        ItemStack input = itemEntity.getItem();
        return input.is(BuiltInRegistries.ITEM.get(type)) && input.getCount() - used >= quantity;
    }

    @Contract(" -> new")
    @Override
    public @NotNull RitualRecipeOffering copy() {
        return new ItemRitualRecipeOffering(this.type, this.quantity, this.offset);
    }

    @Contract("_ -> new")
    @Override
    public Entity asEntity(Level level) {
        return new ItemEntity(level, 0, 0, 0, this.asItemStack());
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return asItemStack().getHoverName();
    }

    @Override
    @Nonnull
    public List<Component> getTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        return this.asItemStack().getTooltipLines(tooltipContext, player, tooltipFlag);
    }

    @Contract(" -> new")
    @Nonnull
    public ItemStack asItemStack() {
        ItemStack item = new ItemStack(BuiltInRegistries.ITEM.get(type), quantity);
        if (item.isDamageableItem()) item.setCount(1);
        return item;
    }
}
