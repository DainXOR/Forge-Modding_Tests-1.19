package net.dain.testsmod.world.feature;

import net.dain.testsmod.TestsMod;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeature {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, TestsMod.MOD_ID);

    public static final RegistryObject<PlacedFeature> WOLFRAMITE_ORE_PLACED =
            PLACED_FEATURES.register(
                    "wolframite_ore_placed",
                    () -> new PlacedFeature(ModConfiguredFeatures.WOLFRAMITE_ORE.getHolder().get(),
                            rareOrePlacement(
                                    3,
                                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(20))
                                    )
                    )
            );

    public static final RegistryObject<PlacedFeature> ENDER_ESSENCE_ORE_PLACED =
            PLACED_FEATURES.register(
                    "ender_essence_ore_placed",
                    () -> new PlacedFeature(ModConfiguredFeatures.ENDER_ESSENCE_ORE.getHolder().get(),
                            commonOrePlacement(
                                    10,
                                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))
                            )
                    )
            );


    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier1) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(veinsPerChunk), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int veinsPerChunk, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(veinsPerChunk), placementModifier);
    }

    public static void register(IEventBus eventBus){
        PLACED_FEATURES.register(eventBus);
    }

}
