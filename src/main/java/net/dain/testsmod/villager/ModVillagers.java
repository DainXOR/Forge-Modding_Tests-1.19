package net.dain.testsmod.villager;

import com.google.common.collect.ImmutableSet;
import net.dain.testsmod.TestsMod;
import net.dain.testsmod.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, TestsMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.PROFESSIONS, TestsMod.MOD_ID);

    public static final RegistryObject<PoiType> REACTOR_BLOCK_POI = POI_TYPES.register(
            "reactor_block_poi",
            () -> new PoiType(
                    ImmutableSet.copyOf(ModBlocks.REACTOR_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1,
                    1
            ));

    public static final RegistryObject<VillagerProfession> NUCLEAR_ENGINEER = VILLAGER_PROFESSIONS.register(
            "nuclear_engineer",
            () -> new VillagerProfession(
                    "Nuclear Engineer",
                    x -> x.get() == REACTOR_BLOCK_POI.get(),
                    x -> x.get() == REACTOR_BLOCK_POI.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_TOOLSMITH
            )
    );

    public static void registerPOIs(){
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class)
                    .invoke(null, REACTOR_BLOCK_POI.get());
        }catch (InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus){
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
