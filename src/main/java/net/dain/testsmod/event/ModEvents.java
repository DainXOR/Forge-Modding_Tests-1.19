package net.dain.testsmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.dain.testsmod.TestsMod;
import net.dain.testsmod.item.ModItems;
import net.dain.testsmod.networking.ModMessages;
import net.dain.testsmod.networking.packet.ThirstDataSyncS2CPacket;
import net.dain.testsmod.thirst.PlayerThirst;
import net.dain.testsmod.thirst.PlayerThirstProvider;
import net.dain.testsmod.villager.ModVillagers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = TestsMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent tradesEvent){
        if(tradesEvent.getType() == VillagerProfession.CLERIC){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradesEvent.getTrades();
            ItemStack stack = new ItemStack(ModItems.ZERO_ONE_DICE.get(), 1);
            int villagerLevel = 5;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD_BLOCK, 20),
                    stack, 2, 40, 0.5f
            ));
        }

        if(tradesEvent.getType() == ModVillagers.NUCLEAR_ENGINEER.get()){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradesEvent.getTrades();

            trades.get(/* Trading level */ 1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1), /* Price */
                    new ItemStack(Items.WATER_BUCKET, 1), /* Stock per price */
                    10, 8, 0.02f
            ));/* Uses, Villager xp, price multiplier */
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.REDSTONE, 20),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.IRON_INGOT, 10),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.REDSTONE_BLOCK, 3),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.IRON_BLOCK, 2),
                    10, 8, 0.02f
            ));

            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.REDSTONE, 32),
                    new ItemStack(Items.EMERALD, 1),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.IRON_INGOT, 20),
                    new ItemStack(Items.EMERALD, 2),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.REDSTONE_BLOCK, 6),
                    new ItemStack(Items.EMERALD, 2),
                    10, 8, 0.02f
            ));
            trades.get(1).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.IRON_BLOCK, 6),
                    new ItemStack(Items.EMERALD, 3),
                    10, 8, 0.02f
            ));


            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(Items.OBSIDIAN, 2),
                    5, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(Items.CRYING_OBSIDIAN, 1),
                    5, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(Items.IRON_CHESTPLATE, 1),
                    3, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.IRON_HELMET, 1),
                    3, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 7),
                    new ItemStack(Items.LAVA_BUCKET, 1),
                    5, 8, 0.02f
            ));

            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.OBSIDIAN, 3),
                    new ItemStack(Items.EMERALD, 3),
                    5, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.CRYING_OBSIDIAN, 2),
                    new ItemStack(Items.EMERALD, 3),
                    5, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.IRON_CHESTPLATE, 1),
                    new ItemStack(Items.EMERALD, 1),
                    3, 8, 0.02f
            ));
            trades.get(2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.IRON_HELMET, 1),
                    new ItemStack(Items.EMERALD, 1),
                    3, 8, 0.02f
            ));


            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.QUARTZ, 16),
                    20, 8, 0.02f
            ));
            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.BLACKSTONE, 16),
                    20, 8, 0.02f
            ));
            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.GOLDEN_APPLE, 1),
                    3, 8, 0.02f
            ));
            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.MILK_BUCKET, 1),
                    5, 8, 0.02f
            ));
            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 7),
                    new ItemStack(Items.POTION, 1),
                    5, 8, 0.02f
            ));

            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.GOLDEN_APPLE, 1),
                    new ItemStack(Items.EMERALD, 2),
                    3, 8, 0.02f
            ));
            trades.get(3).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.POTION, 1),
                    new ItemStack(Items.EMERALD, 3),
                    5, 8, 0.02f
            ));


            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.SUSPICIOUS_STEW, 1),
                    10, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.HONEY_BOTTLE, 1),
                    10, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 30),
                    new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                    3, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 24),
                    new ItemStack(Items.DIAMOND_HELMET, 1),
                    3, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 45),
                    new ItemStack(ModItems.WOLFRAMIUM_INGOT.get(), 1),
                    1, 8, 0.02f
            ));

            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
                    new ItemStack(Items.EMERALD, 15),
                    3, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.DIAMOND_HELMET, 1),
                    new ItemStack(Items.EMERALD, 10),
                    3, 8, 0.02f
            ));
            trades.get(4).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.WOLFRAMIUM_INGOT.get(), 1),
                    new ItemStack(Items.EMERALD, 40),
                    1, 8, 0.02f
            ));


            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 64),
                    new ItemStack(Items.EMERALD, 45),
                    new ItemStack(Items.NETHERITE_CHESTPLATE, 1),
                    2, 8, 0.02f
            ));
            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 64),
                    new ItemStack(Items.EMERALD, 32),
                    new ItemStack(Items.NETHERITE_HELMET, 1),
                    2, 8, 0.02f
            ));
            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 64),
                    new ItemStack(Items.EMERALD, 16),
                    new ItemStack(Items.BEACON, 1),
                    5, 8, 0.02f
            ));

            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.NETHERITE_CHESTPLATE, 1),
                    new ItemStack(Items.EMERALD, 64),
                    2, 8, 0.02f
            ));
            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.NETHERITE_HELMET, 1),
                    new ItemStack(Items.EMERALD, 45),
                    2, 8, 0.02f
            ));
            trades.get(5).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.BEACON, 1),
                    new ItemStack(Items.EMERALD, 32),
                    5, 8, 0.02f
            ));

        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            if(!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()){
                event.addCapability(
                        new ResourceLocation(TestsMod.MOD_ID, "properties"),
                        new PlayerThirstProvider()
                        );
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
                   newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerThirst.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        /*
        if(event.side == LogicalSide.SERVER){
            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                if(thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.005f){ // Once every 10 sec average
                    thirst.subThirst(1);
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), (ServerPlayer) event.player);
                }
            });
        }
        */
        if(event.side == LogicalSide.SERVER){

            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                Player player = event.player;

                // Is immune
                if(player.isCreative() || player.isSpectator())
                    return;

                float randomTickThreshold = 0.0002f;

                // Activity thirst
                if(player.isSprinting() || player.isSwimming())
                    randomTickThreshold += randomTickThreshold * 4f;

                // Hot thirst
                if(!(player.fireImmune() || player.hasEffect(MobEffects.FIRE_RESISTANCE))) {
                    if (player.level.getBiome(player.blockPosition()).tags().toList().contains(Tags.Biomes.IS_HOT))
                        randomTickThreshold += randomTickThreshold * 10f;

                    if (player.isOnFire())
                        randomTickThreshold += randomTickThreshold * 5f;
                }

                // Eats or drinks results
                thirst.updateInventory(player);

                if(player.getUseItem().isEdible()){
                    randomTickThreshold += randomTickThreshold * 100f;

                } else if (thirst.getThirst() < thirst.MAX_THIRST && thirst.hasFinishDrinking()) {
                    thirst.increaseWater(2, 1);
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst(), thirst.getSatiety()), (ServerPlayer) player);
                }

                // Reduce thirst if random tick
                if(thirst.getThirst() > 0 && player.getRandom().nextFloat() < randomTickThreshold){
                    thirst.decreaseWater(1);
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst(), thirst.getSatiety()), (ServerPlayer) player);
                }


                // No drink effects
                if(thirst.getThirst() <= 6){
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 0, false, false, false));
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2, 0, false, false, false));
                    player.setSprinting(false);

                    if(thirst.getThirst() <= 0){
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 0, false, false, false));

                        if(player.getRandom().nextFloat() < 0.05f) {
                            player.hurt(DamageSource.DRY_OUT, 1);
                        }
                    }

                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event){
        if(!event.getLevel().isClientSide()){
            if(event.getEntity() instanceof ServerPlayer player){
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(playerThirst -> {
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(playerThirst.getThirst(), playerThirst.getSatiety()), player);
                });
            }
        }
    }

}
