package net.dain.testsmod.block.entity;

import net.dain.testsmod.TestsMod;
import net.dain.testsmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    TestsMod.MOD_ID);


    public static final RegistryObject<BlockEntityType<ReactorBlockEntity>> REACTOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(
                    "reactor_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ReactorBlockEntity::new,
                            ModBlocks.REACTOR_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
