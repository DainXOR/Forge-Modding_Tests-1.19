package net.dain.testsmod.block;

import net.dain.testsmod.TestsMod;
import net.dain.testsmod.block.custom.CoreBlock;
import net.dain.testsmod.block.custom.ReactorBlock;
import net.dain.testsmod.block.custom.StraberryCropBlock;
import net.dain.testsmod.fluid.ModFluids;
import net.dain.testsmod.item.ModItems;
import net.dain.testsmod.item.DainCreativeModeTab;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestsMod.MOD_ID);

    public static final RegistryObject<Block> WOLFRAMITE_ORE = registerBlock("wolframite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(20f)
                    .destroyTime(30f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(75f)),
            CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> DEEPSLATE_WOLFRAMITE_ORE = registerBlock("deepslate_wolframite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(20f)
                    .destroyTime(40f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(75f)),
            CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> END_STONE_ENDER_ESSENCE_ORE = registerBlock("end_stone_ender_essence_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(10f)
                    .destroyTime(15f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(5f)
                    .lightLevel(state -> 8)),
            CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> NETHERRACK_ENDER_ESSENCE_ORE = registerBlock("netherrack_ender_essence_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(10f)
                    .destroyTime(15f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(5f)
                    .lightLevel(state -> 8)),
            CreativeModeTab.TAB_BUILDING_BLOCKS);


    public static final RegistryObject<Block> WOLFRAMIUM_BLOCK = registerBlock("wolframium_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL)
                    .strength(60f)
                    .destroyTime(50f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(200f)
                    ),
            CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> REACTOR_BLOCK = registerBlock("reactor_block",
            () -> new ReactorBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL)
                    .strength(25f)
                    .destroyTime(15f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(20f)
                    .noOcclusion()
            ),
            DainCreativeModeTab.ARTIFACTS_TAB);

    public static final RegistryObject<Block> CORE_BLOCK = registerBlock("core_block",
            () -> new CoreBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL)
                    .strength(50000f)
                    .destroyTime(50f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(2000f)
                    .noOcclusion()
            ),
            DainCreativeModeTab.ARTIFACTS_TAB);


    public static final RegistryObject<Block> STRAWBERRY_CROP = BLOCKS.register("strawberry_crop",
            () -> new StraberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT))
    );

    public static final RegistryObject<LiquidBlock> ENDER_ESSENCE_FLUID_BLOCK = BLOCKS.register(
            "ender_essence_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_ENDER_ESSENCE_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)
                    .sound(SoundType.HONEY_BLOCK)
                    .lightLevel(state -> 12)
            ));
    public static final RegistryObject<LiquidBlock> CONTAMINATED_WATER_FLUID_BLOCK = BLOCKS.register(
            "contaminated_water_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_CONTAMINATED_WATER_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)
            ));




    public static final RegistryObject<Block> DUMMY_BLOCK = registerBlock("dummy_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(60f)
                    .requiresCorrectToolForDrops()
                    .explosionResistance(200f)
            ),
            DainCreativeModeTab.HMMM_TAB);

    private static <T extends Block>RegistryObject<T>registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
