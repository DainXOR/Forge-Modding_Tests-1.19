package net.dain.testsmod.networking.packet;

import net.dain.testsmod.client.ClientThirstData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThirstDataSyncS2CPacket {
    // private static final String MESSAGE_DRINK_WATER = "message.testsmod.drink_water";
    // private static final String MESSAGE_NO_WATER = "message.testsmod.no_water";

    private final int thirst;
    private final int satiety;

    public ThirstDataSyncS2CPacket(int thirst, int satiety){
        this.thirst = thirst;
        this.satiety = satiety;
    }

    public ThirstDataSyncS2CPacket(FriendlyByteBuf buf){
        this.thirst = buf.readInt();
        this.satiety = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(thirst);
        buf.writeInt(satiety);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Here we are on the client
            ClientThirstData.setPlayerThirst(thirst);
            ClientThirstData.setPlayerSatiety(satiety);

        });
        return true;
    }

}
