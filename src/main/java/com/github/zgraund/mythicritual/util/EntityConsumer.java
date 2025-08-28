package com.github.zgraund.mythicritual.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public final class EntityConsumer {
    private final Entity entity;
    private int used;

    public EntityConsumer(Entity entity, int used) {
        this.entity = entity;
        this.used = used;
    }

    public void increment(int amount) {used += amount;}

    public void consume() {
        switch (entity) {
            case ItemEntity i -> i.getItem().shrink(used);
            case LivingEntity l -> {
                if (used <= 0) return;
                // AT to set the loot table of the mob ingredient to empty
                l.getType().lootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("minecraft:empty"));
                l.hurt(new DamageSources(entity.level().registryAccess()).magic(), Float.MAX_VALUE);
            }
            default -> entity.kill();
        }
    }

    public Entity entity() {return entity;}

    public int used() {return used;}
}
