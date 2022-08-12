package net.dain.testsmod.networking.packet;

import net.dain.testsmod.thirst.PlayerThirstProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrinkWaterC2SPacket {
    public static final String DRINK_WATER_MESSAGE = "message.testsmod.drink_water";
    public static final String NO_WATER_MESSAGE = "message.testsmod.no_water";

    public DrinkWaterC2SPacket(){

    }

    public DrinkWaterC2SPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // This is server side

            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(hasWaterAround(player, level)){
                player.sendSystemMessage(Component.translatable(DRINK_WATER_MESSAGE).withStyle(ChatFormatting.DARK_AQUA));

                level.playSound(
                        null,
                        player.getOnPos(),
                        SoundEvents.GENERIC_DRINK,
                        SoundSource.PLAYERS,
                        0.5f,
                        level.random.nextFloat() * 0.1f + 0.9f
                        );

                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    thirst.addThirst(1);
                    player.sendSystemMessage(Component.literal("Current thirst: " + thirst.getThirst())
                            .withStyle(ChatFormatting.AQUA));
                });


            }
            else {
                player.sendSystemMessage(Component.translatable(NO_WATER_MESSAGE).withStyle(ChatFormatting.RED));

                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    player.sendSystemMessage(Component.literal("Current thirst: " + thirst.getThirst())
                            .withStyle(ChatFormatting.AQUA));
                });
            }

        });
        return true;
    }

    private boolean hasWaterAround(ServerPlayer player, ServerLevel level) {
        return level.getBlockStates(player.getBoundingBox().inflate(0.2))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }
}
