package frankv.hbde.capability;

import frankv.hbde.HBDE;
import frankv.hbde.network.NetworkHandler;
import frankv.hbde.network.PacketDataSlots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ToggleStateHandler {

    public static void safeSendToClient(Player player) {
        if (player instanceof ServerPlayer) {
            player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
                NetworkHandler.sendToClient(new PacketDataSlots(ts.getToggleDEState()), (ServerPlayer) player);
            });
        }
    }

    public static void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof Player) {
            ToggleStateProvider provider = new ToggleStateProvider();
            event.addCapability(new ResourceLocation(HBDE.MODID, "destate"), provider);
            event.addListener(provider::invalidate);
            safeSendToClient((Player) event.getObject());
        }
    }

    public static void requestToggleState(Player player, int selected){
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            if(!player.getInventory().getItem(selected).isEmpty()){
                ts.toggleDEState(selected);
            }
            safeSendToClient(player);
        });
    }

    public static void playerClone(PlayerEvent.Clone event){
        Player player = event.getPlayer();

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            event.getOriginal().getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(oldts -> {
                ts.setToggleDEState(oldts.getToggleDEState());
            });
            safeSendToClient(player);
        });
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Inventory inv = player.getInventory();

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            if(ts.getToggleDEState() == null) return;
            int[] toggleState = ts.getToggleDEState();

            for(int i : toggleState){
                if(i == 0) continue;
                if(inv.getItem(i).isEmpty()) {
                    toggleState[i] = 0;
                }
            }

            ts.setToggleDEState(toggleState);
            safeSendToClient(player);
        });
    }
}
