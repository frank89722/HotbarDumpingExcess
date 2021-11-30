package frankv.hbde.network;

import frankv.hbde.ClientEventsHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDataSlots {
    private final int[] data;

    public PacketDataSlots(int[] data) {
        this.data = data;
    }

    public static PacketDataSlots fromBytes(FriendlyByteBuf buf){
        return new PacketDataSlots(buf.readVarIntArray());
    }

    public static void toBytes(PacketDataSlots msg, FriendlyByteBuf buf) {
        buf.writeVarIntArray(msg.data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientEventsHandler.setClientToggleData(data);
        });
        ctx.get().setPacketHandled(true);
    }
}
