package net.dain.testsmod.fluid;

import com.mojang.math.Vector3f;
import net.dain.testsmod.TestsMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation ENDER_ESSENCE_OVERLAY_RL = new ResourceLocation(TestsMod.MOD_ID,"misc/in_ender_essence");
    public static final ResourceLocation CONTAMINATED_WATER_OVERLAY_RL = new ResourceLocation(TestsMod.MOD_ID,"misc/in_contaminated_water");
    public static final ResourceLocation PURE_WATER_OVERLAY_RL = new ResourceLocation(TestsMod.MOD_ID,"misc/in_pure_water");
    public static final ResourceLocation MAGIC_WATER_OVERLAY_RL = new ResourceLocation(TestsMod.MOD_ID,"misc/in_magic_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, TestsMod.MOD_ID);

    public static final RegistryObject<FluidType> ENDER_ESSENCE_FLUID_TYPE = register(
            "ender_essence_fluid",
            ENDER_ESSENCE_OVERLAY_RL,
            null,
            FluidType.Properties.create()
                    .motionScale(0.008D)
                    .canPushEntity(true)
                    .canSwim(true)
                    .canDrown(false)
                    //.fallDistanceModifier(0.05f)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .supportsBoating(true)
                    .sound(SoundAction.get("enter_fluid"), SoundEvents.HONEY_DRINK)
                    .canHydrate(false)
                    .lightLevel(12)
                    .density(2000)
                    .temperature(100)
                    .viscosity(2000)
                    .rarity(Rarity.EPIC)
            );

    public static final RegistryObject<FluidType> CONTAMINATED_WATER_FLUID_TYPE = register(
            "contaminated_water_fluid",
            CONTAMINATED_WATER_OVERLAY_RL,
            null,
            FluidType.Properties.create()
                    .motionScale(0.016D)
                    .canPushEntity(true)
                    .canSwim(false)
                    .canDrown(true)
                    //.fallDistanceModifier(0.05f)
                    .canExtinguish(true)
                    .canConvertToSource(true)
                    .supportsBoating(true)
                    .sound(SoundAction.get("enter_fluid"), SoundEvents.AMBIENT_UNDERWATER_ENTER)
                    .canHydrate(false)
                    .density(5000)
                    .temperature(300)
                    .viscosity(4000)
                    .rarity(Rarity.COMMON)
    );

    private static RegistryObject<FluidType> register(String name, ResourceLocation overlay, FluidColors colors, FluidType.Properties properties){
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                WATER_STILL_RL, WATER_FLOWING_RL, overlay,
                0xc04ec9b0, new Vector3f(78f/255f, 201f/255f, 176f/255f), properties));
    }

    public static void register(IEventBus eventBus){
        FLUID_TYPES.register(eventBus);
    }

    public class FluidColors{
        FluidColors(int tintColor, Vector3f fogColor){
            tint = tintColor;
            fog = fogColor;
        }

        public static int tint;
        public static Vector3f fog;
    }

    public FluidColors register(int tintColor, Vector3f fogColor){
        return new FluidColors(tintColor, fogColor);
    }
}

