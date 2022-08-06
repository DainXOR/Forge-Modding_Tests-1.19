package net.dain.testsmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DainCreativeModeTab {
    public static final CreativeModeTab HMMM_TAB = new CreativeModeTab("hmmm_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WOLFRAMIUM_INGOT.get());
        }
    };

    public static final CreativeModeTab ARTIFACTS_TAB = new CreativeModeTab("artifacts_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GOLDEN_DICE.get());
        }
    };
}
