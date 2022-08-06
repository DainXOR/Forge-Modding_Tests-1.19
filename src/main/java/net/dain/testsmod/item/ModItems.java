package net.dain.testsmod.item;


import net.dain.testsmod.TestsMod;
import net.dain.testsmod.item.custom.DiceItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestsMod.MOD_ID);

    public static final RegistryObject<Item> WOLFRAMIUM_INGOT =
            ITEMS.register("wolframium_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> RAW_WOLFRAMITE =
            ITEMS.register("raw_wolframite", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> GOLDEN_DICE =
            ITEMS.register("golden_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 6));

    public static final RegistryObject<Item> TEN_DICE =
            ITEMS.register("10_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,10, 6));

    public static final RegistryObject<Item> DEMONIC_DICE =
            ITEMS.register("demonic_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 5));

    public static final RegistryObject<Item> LIGHT_DICE =
            ITEMS.register("light_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 5));

    public static final RegistryObject<Item> BLACK_DICE =
            ITEMS.register("black_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 5));

    public static final RegistryObject<Item> EMERALD_DICE =
            ITEMS.register("emerald_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 5));

    public static final RegistryObject<Item> DRAGON_DICE =
            ITEMS.register("dragon_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 4));

    public static final RegistryObject<Item> ENDER_DICE =
            ITEMS.register("ender_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 3));

    public static final RegistryObject<Item> DICE =
            ITEMS.register("dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,6, 2));

    public static final RegistryObject<Item> THREE_DICE =
            ITEMS.register("3_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,3, 1));

    public static final RegistryObject<Item> TWO_DICE =
            ITEMS.register("2_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,2, 1));

    public static final RegistryObject<Item> CERO_ONE_DICE =
            ITEMS.register("0_1_dice", () -> new DiceItem(
                    new Item.Properties().tab(DainCreativeModeTab.ARTIFACTS_TAB)
                            .stacksTo(1)
                    ,1, 1));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
