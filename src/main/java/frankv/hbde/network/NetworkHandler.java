package frankv.hbde.network;

import frankv.hbde.HBDE;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel CHANNEL_INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HBDE.MODID, "hbde"),
                () -> "1.0",
                s -> true,
                s -> true);

        CHANNEL_INSTANCE.messageBuilder(PacketToggleTrigger.class, nextID())
                .encoder(PacketToggleTrigger::toBytes)
                .decoder(PacketToggleTrigger::new)
                .consumer(PacketToggleTrigger::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketDataSlots.class, nextID())
                .encoder(PacketDataSlots::toBytes)
                .decoder(PacketDataSlots::fromBytes)
                .consumer(PacketDataSlots::handle)
                .add();

    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        CHANNEL_INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        CHANNEL_INSTANCE.sendToServer(packet);
    }
}
