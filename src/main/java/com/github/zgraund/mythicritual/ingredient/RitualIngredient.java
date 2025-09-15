package com.github.zgraund.mythicritual.ingredient;

import com.github.zgraund.mythicritual.component.ModDataComponents;
import com.github.zgraund.mythicritual.item.ModItems;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class RitualIngredient implements ICustomIngredient {
    public static final RitualIngredient EMPTY = new RitualIngredient(HolderSet.empty(), DataComponentPredicate.EMPTY, 0);
    public static final MapCodec<RitualIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            NeoForgeExtraCodecs.xor(
                    ItemsHolder.CODEC,
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")
            ).xmap(
                    i -> i.map(items -> items, ItemsHolder::fromEntity),
                    holder -> ItemsHolder.isEntity(holder) ? Either.right(ItemsHolder.toEntity(holder)) : Either.left(holder)
            ).forGetter(RitualIngredient::itemsHolder),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(RitualIngredient::count)
    ).apply(inst, RitualIngredient::new));

    private final HolderSet<Item> items;
    private final DataComponentPredicate components;
    private final int count;
    @Nullable
    private ItemStack[] stacks;

    public RitualIngredient(@Nonnull ItemsHolder holder, int count) {
        this.items = holder.items;
        this.components = holder.components;
        this.count = count;
    }

    public RitualIngredient(HolderSet<Item> items, DataComponentPredicate components, int count) {
        this(new ItemsHolder(items, components), count);
    }

    @Nonnull
    public static RitualIngredient of(EntityType<?> entity) {return new RitualIngredient(ItemsHolder.fromEntity(entity), 1);}

    @Nonnull
    public static RitualIngredient of(@Nonnull ItemLike... items) {return of(1, items);}

    @Nonnull
    public static RitualIngredient of(int quantity, @Nonnull ItemLike... items) {return of(quantity, DataComponentPredicate.EMPTY, items);}

    @SuppressWarnings("deprecation")
    @Nonnull
    public static RitualIngredient of(int quantity, DataComponentPredicate components, @Nonnull ItemLike... items) {
        return new RitualIngredient(HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()), components, quantity);
    }

    @Override
    public boolean test(@Nonnull ItemStack stack) {
        return ((items.size() == 0 && stack.isEmpty()) || items.contains(stack.getItemHolder()))
               && stack.getCount() >= count
               && components.test(stack);
    }

    @Nonnull
    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) this.stacks = this.items.stream().map(i -> new ItemStack(i, count, components.asPatch())).toArray(ItemStack[]::new);
        return Arrays.stream(stacks);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Nonnull
    @Override
    public IngredientType<?> getType() {
        return ModIngredients.RITUAL_INGREDIENT.get();
    }

    public DataComponentPredicate components() {return components;}

    public HolderSet<Item> items() {return items;}

    public ItemsHolder itemsHolder() {return new ItemsHolder(this.items, this.components);}

    public int count() {return count;}

    public ItemStack asItemStack() {return getItems().findFirst().orElse(ItemStack.EMPTY);}

    @Nullable
    public EntityType<?> entityType() {
        return ItemsHolder.toEntity(itemsHolder());
    }

    @Override
    public String toString() {
        return "RitualIngredient{" +
               "\n  items=" + items +
               "\n  components=" + components +
               "\n  count=" + count +
               "\n}";
    }

    public record ItemsHolder(HolderSet<Item> items, DataComponentPredicate components) {
        public static final MapCodec<ItemsHolder> CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
                HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false)
                              .fieldOf("items")
                              .forGetter(ItemsHolder::items),
                DataComponentPredicate.CODEC.optionalFieldOf("components", DataComponentPredicate.EMPTY).forGetter(ItemsHolder::components)
        ).apply(ins, ItemsHolder::new));

        @Nonnull
        public static ItemsHolder fromEntity(EntityType<?> entity) {
            return new ItemsHolder(HolderSet.direct(ModItems.SOUL), DataComponentPredicate.builder().expect(ModDataComponents.SOUL_ENTITY_TYPE.get(), entity).build());
        }

        @Nullable
        public static EntityType<?> toEntity(@Nonnull ItemsHolder holder) {
            Optional<? extends EntityType<?>> entity = holder.components.asPatch().get(ModDataComponents.SOUL_ENTITY_TYPE.get());
            return entity != null && entity.isPresent() ? entity.get() : null;
        }

        public static boolean isEntity(ItemsHolder holder) {return toEntity(holder) != null;}
    }
}
