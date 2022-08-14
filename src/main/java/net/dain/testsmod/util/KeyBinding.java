package net.dain.testsmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.testsmod";
    public static final String KEY_DRINK_WATER = "key.testsmod.drink_water";
    public static final String KEY_RPG_STATS_MENU = "key.testsmod.rpg_stats_menu";

    public static final KeyMapping DRINK_WATER_KEY = new KeyMapping(
            KEY_DRINK_WATER,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            KEY_CATEGORY
            );

    public static final KeyMapping RPG_STATS_MENU_KEY = new KeyMapping(
            KEY_RPG_STATS_MENU,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            KEY_CATEGORY
    );
}
