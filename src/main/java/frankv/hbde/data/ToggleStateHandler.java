package frankv.hbde.data;

import frankv.hbde.HBDE;
import frankv.hbde.network.NetworkHandler;
import frankv.hbde.network.PacketDataSlots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.Logger;

public class ToggleStateHandler {
    private static Logger LOGGER = HBDE.getLOGGER();

    public static void safeSendToClient(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
                NetworkHandler.sendToClient(new PacketDataSlots(ts.getToggleDEState()), (ServerPlayerEntity) player);
            });
        }
    }

    public static void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            ToggleStateProvider provider = new ToggleStateProvider();
            event.addCapability(new ResourceLocation(HBDE.MODID, "destate"), provider);
            event.addListener(provider::invalidate);
            safeSendToClient((PlayerEntity) event.getObject());
            //LOGGER.debug("destate set!");
        }
    }

    public static void requestToggleState(PlayerEntity player, int selected){
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            int[] toggleState = ts.getToggleDEState();
            if(!player.inventory.getItem(selected).isEmpty()){
                ts.toggleDEState(selected);
            }
            safeSendToClient(player);
            //LOGGER.debug(player.getStringUUID() + " " + selected + " toggled, now: " + toggleState[selected]);
        });
    }

    public static void playerClone(PlayerEvent.Clone event){
        PlayerEntity player = event.getPlayer();

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            event.getOriginal().getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(oldts -> {
                ts.setToggleDEState(oldts.getToggleDEState());
            });
            safeSendToClient(player);
            //LOGGER.debug("Cloned");
        });
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        PlayerInventory inv = player.inventory;

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            if(ts.getToggleDEState() == null) return;
            int[] toggleState = ts.getToggleDEState();

            for(int i=0; i<9; i++){
                if(toggleState[i] == 1) {
                    if(inv.getItem(i).isEmpty()) {
                        toggleState[i] = 0;
                    }
                }
            }
            ts.setToggleDEState(toggleState);
            safeSendToClient(player);
        });
    }
}
