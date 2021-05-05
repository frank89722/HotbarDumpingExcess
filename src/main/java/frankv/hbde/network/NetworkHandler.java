package frankv.hbde.network;

import frankv.hbde.HBDE;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel CHANNEL_INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HBDE.MODID, "tbde"),
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

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        CHANNEL_INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        CHANNEL_INSTANCE.sendToServer(packet);
    }
}
