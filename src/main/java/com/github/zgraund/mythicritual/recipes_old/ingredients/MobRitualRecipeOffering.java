package com.github.zgraund.mythicritual.recipes_old.ingredients;

import com.github.zgraund.mythicritual.util.EntityConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record MobRitualRecipeOffering(
        ResourceLocation type,
        Vec3i offset
) implements RitualRecipeOffering {
    public static final MapCodec<MobRitualRecipeOffering> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(MobRitualRecipeOffering::type),
                    Vec3i.CODEC.optionalFieldOf("pos", new Vec3i(0, 1, 0)).forGetter(MobRitualRecipeOffering::offset)
            ).apply(inst, MobRitualRecipeOffering::new)
    );

    @Override
    @Nonnull
    public Boolean test(EntityConsumer entityConsumer) {
        Entity entity = entityConsumer.entity();
        int used = entityConsumer.used();
        if (!(entity instanceof LivingEntity) || used >= 1) return false;
        return entity.isAlive() && BuiltInRegistries.ENTITY_TYPE.get(this.type) == entity.getType();
    }

    @Contract(" -> new")
    @Override
    public RitualRecipeOffering copy() {
        return new MobRitualRecipeOffering(this.type, this.offset);
    }

    @Override
    public Entity asEntity(Level level) {
        return this.asEntityType().create(level);
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return asEntityType().getDescription();
    }

    @Override
    @Nonnull
    public List<Component> getTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        List<Component> lines = new ArrayList<>(2);
        lines.add(this.getDisplayName());
        if (tooltipFlag.isAdvanced()) lines.add(Component.literal(this.type.toString()).withStyle(ChatFormatting.DARK_GRAY));
        return lines;
    }

    @Nonnull
    public EntityType<?> asEntityType() {
        return BuiltInRegistries.ENTITY_TYPE.get(type);
    }
}
