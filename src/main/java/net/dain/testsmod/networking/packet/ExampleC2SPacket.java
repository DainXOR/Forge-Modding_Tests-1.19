package net.dain.testsmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

import javax.swing.text.html.parser.Entity;
import java.util.function.Supplier;

public class ExampleC2SPacket {
    public ExampleC2SPacket(){

    }

    public ExampleC2SPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // This is server side

            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.ELYTRA));
            EntityType.WOLF.spawn(
                    level,
                    null,
                    null,
                    player.blockPosition(),
                    MobSpawnType.NATURAL,
                    true,
                    false
                    );
        });
        return true;
    }
}
