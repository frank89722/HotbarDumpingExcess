package frankv.hbde.network;

import frankv.hbde.ClientEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDataSlots {
    private final int[] data;

    public PacketDataSlots(int[] data) {
        this.data = data;
    }

    public static PacketDataSlots fromBytes(PacketBuffer buf){
        return new PacketDataSlots(buf.readVarIntArray());
    }

    public static void toBytes(PacketDataSlots msg, PacketBuffer buf) {
        buf.writeVarIntArray(msg.data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientEvents.setClientToggleData(data);
        });
        ctx.get().setPacketHandled(true);
    }
}
