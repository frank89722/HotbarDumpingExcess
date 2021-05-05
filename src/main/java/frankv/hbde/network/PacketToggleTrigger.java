package frankv.hbde.network;

import frankv.hbde.data.ToggleStateHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleTrigger {

    public PacketToggleTrigger(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public PacketToggleTrigger() {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();

            assert player != null;
            ToggleStateHandler.requestToggleState(player, player.inventory.selected);

        });
        ctx.get().setPacketHandled(true);
    }
}
