package net.dain.testsmod;

import com.mojang.logging.LogUtils;
import net.dain.testsmod.block.ModBlocks;
import net.dain.testsmod.block.entity.ModBlockEntities;
import net.dain.testsmod.item.ModItems;
import net.dain.testsmod.networking.ModMessages;
import net.dain.testsmod.painting.ModPaintings;
import net.dain.testsmod.villager.ModVillagers;
import net.dain.testsmod.world.feature.ModConfiguredFeatures;
import net.dain.testsmod.world.feature.ModPlacedFeature;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;


@Mod(TestsMod.MOD_ID)
public class TestsMod
{
    public static final String MOD_ID = "testsmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public TestsMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockBehaviour.Properties.of(new Material.Builder(MaterialColor.COLOR_BLACK).build());
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        //ModBlockEntities.register(modEventBus);
        ModPaintings.register(modEventBus);
        ModVillagers.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeature.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Rutilegister ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModVillagers.registerPOIs();
        });

        ModMessages.register();

        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
