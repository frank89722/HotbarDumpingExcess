package frankv.hbde.network;

import frankv.hbde.capability.ToggleStateHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleTrigger {

    public PacketToggleTrigger(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public PacketToggleTrigger() {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            assert player != null;
            ToggleStateHandler.requestToggleState(player, player.getInventory().selected);

        });
        ctx.get().setPacketHandled(true);
    }
}
