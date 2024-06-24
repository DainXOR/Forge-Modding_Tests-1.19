package net.dain.testsmod.fluid;

import net.dain.testsmod.TestsMod;
import net.dain.testsmod.block.ModBlocks;
import net.dain.testsmod.block.custom.ReactorBlock;
import net.dain.testsmod.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TestsMod.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_ENDER_ESSENCE_FLUID = FLUIDS.register(
            "ender_essence_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.ENDER_ESSENCE_FLUID_PROPERTIES)
            );
    public static final RegistryObject<FlowingFluid> FLOWING_ENDER_ESSENCE_FLUID = FLUIDS.register(
            "flowing_ender_essence_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.ENDER_ESSENCE_FLUID_PROPERTIES)
            );

    public static final RegistryObject<FlowingFluid> SOURCE_CONTAMINATED_WATER_FLUID = FLUIDS.register(
            "contaminated_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CONTAMINATED_WATER_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> FLOWING_CONTAMINATED_WATER_FLUID = FLUIDS.register(
            "flowing_contaminated_water_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CONTAMINATED_WATER_FLUID_PROPERTIES)
    );

    public static final ForgeFlowingFluid.Properties ENDER_ESSENCE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.ENDER_ESSENCE_FLUID_TYPE,
            SOURCE_ENDER_ESSENCE_FLUID,
            FLOWING_ENDER_ESSENCE_FLUID
            ).slopeFindDistance(2).block(ModBlocks.ENDER_ESSENCE_FLUID_BLOCK).bucket(ModItems.ENDER_ESSENCE_BUCKET);

    public static final ForgeFlowingFluid.Properties CONTAMINATED_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.CONTAMINATED_WATER_FLUID_TYPE,
            SOURCE_CONTAMINATED_WATER_FLUID,
            FLOWING_CONTAMINATED_WATER_FLUID
    ).slopeFindDistance(3).block(ModBlocks.CONTAMINATED_WATER_FLUID_BLOCK).bucket(ModItems.CONTAMINATED_WATER_BUCKET);

    public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
    }

}
