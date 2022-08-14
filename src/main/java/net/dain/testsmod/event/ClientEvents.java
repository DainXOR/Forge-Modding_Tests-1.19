package net.dain.testsmod.event;


import net.dain.testsmod.TestsMod;
import net.dain.testsmod.client.ThirstHudOverlay;
import net.dain.testsmod.networking.ModMessages;
import net.dain.testsmod.networking.packet.DrinkWaterC2SPacket;
import net.dain.testsmod.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = TestsMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents{

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            if(KeyBinding.DRINK_WATER_KEY.consumeClick()){
                ModMessages.sendToServer(new DrinkWaterC2SPacket());
            }
            if(KeyBinding.RPG_STATS_MENU_KEY.consumeClick()){

            }
        }
    }

    @Mod.EventBusSubscriber(modid = TestsMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBinding.DRINK_WATER_KEY);
            event.register(KeyBinding.RPG_STATS_MENU_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event){
            event.registerAboveAll("thirst", ThirstHudOverlay.HUD_THIRST);
        }
    }

}
