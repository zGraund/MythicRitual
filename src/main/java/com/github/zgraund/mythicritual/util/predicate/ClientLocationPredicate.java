package com.github.zgraund.mythicritual.util.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public record ClientLocationPredicate(
        Optional<HolderSet<Biome>> biomes,
        Optional<List<ResourceKey<Level>>> dimensions,
        Optional<Boolean> smokey,
        Optional<MinMaxBounds.Ints> light,
        Optional<Boolean> canSeeSky
) {
    public static final ClientLocationPredicate ANY = ClientLocationPredicate.builder().build();
    public static final MapCodec<ClientLocationPredicate> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(ClientLocationPredicate::biomes),
            ResourceKey.codec(Registries.DIMENSION).listOf().optionalFieldOf("dimensions").forGetter(ClientLocationPredicate::dimensions),
            Codec.BOOL.optionalFieldOf("smokey").forGetter(ClientLocationPredicate::smokey),
            MinMaxBounds.Ints.CODEC.optionalFieldOf("light").forGetter(ClientLocationPredicate::light),
            Codec.BOOL.optionalFieldOf("requires_sky").forGetter(ClientLocationPredicate::canSeeSky)
    ).apply(inst, ClientLocationPredicate::new));

    public boolean matches(Level level, BlockPos pos) {
        return (biomes.isEmpty() || biomes.get() == level.getBiome(pos)) &&
               (dimensions.isEmpty() || dimensions.get().contains(level.dimension())) &&
               (smokey.isEmpty() || smokey.get() == CampfireBlock.isSmokeyPos(level, pos)) &&
               (light.isEmpty() || light.get().matches(level.getMaxLocalRawBrightness(pos))) &&
               (canSeeSky.isEmpty() || canSeeSky.get() == level.canSeeSky(pos));
    }

    @Nonnull
    @Contract(" -> new")
    public static Builder builder() {return new Builder();}

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
    public static class Builder {
        private Optional<HolderSet<Biome>> biomes = Optional.empty();
        private Optional<List<ResourceKey<Level>>> dimensions = Optional.empty();
        private Optional<Boolean> smokey = Optional.empty();
        private Optional<MinMaxBounds.Ints> light = Optional.empty();
        private Optional<Boolean> canSeeSk = Optional.empty();

        public void setBiomes(HolderSet<Biome> biomes) {
            this.biomes = Optional.of(biomes);
        }

        public void setDimensions(List<ResourceKey<Level>> dimensions) {
            this.dimensions = Optional.of(dimensions);
        }

        public void setSmokey(Boolean smokey) {
            this.smokey = Optional.of(smokey);
        }

        public void setLight(MinMaxBounds.Ints light) {
            this.light = Optional.of(light);
        }

        public void setCanSeeSk(Boolean canSeeSk) {
            this.canSeeSk = Optional.of(canSeeSk);
        }

        public ClientLocationPredicate build() {return new ClientLocationPredicate(biomes, dimensions, smokey, light, canSeeSk);}
    }
}
