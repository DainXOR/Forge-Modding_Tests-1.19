package net.dain.testsmod.world.feature;

import com.google.common.base.Suppliers;
import net.dain.testsmod.TestsMod;
import net.dain.testsmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.MatchResult;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, TestsMod.MOD_ID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_WOLFRAMITE_ORES =
            Suppliers.memoize(() -> List.of(
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.WOLFRAMITE_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_WOLFRAMITE_ORE.get().defaultBlockState())
            ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> ENDER_ESSENCE_ORES =
            Suppliers.memoize(() -> List.of(
                    OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), ModBlocks.END_STONE_ENDER_ESSENCE_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, ModBlocks.NETHERRACK_ENDER_ESSENCE_ORE.get().defaultBlockState())
            ));

    public static final RegistryObject<ConfiguredFeature<?, ?>> WOLFRAMITE_ORE = CONFIGURED_FEATURES.register(
            "wolframite_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_WOLFRAMITE_ORES.get(), 4))
            );

    public static final RegistryObject<ConfiguredFeature<?, ?>> ENDER_ESSENCE_ORE = CONFIGURED_FEATURES.register(
            "ender_essence_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ENDER_ESSENCE_ORES.get(), 5))
    );


    public static void register(IEventBus eventBus){
        CONFIGURED_FEATURES.register(eventBus);
    }
}
