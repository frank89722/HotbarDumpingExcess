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

    public static void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            ToggleStateProvider provider = new ToggleStateProvider();
            event.addCapability(new ResourceLocation(HBDE.MODID, "destate"), provider);
            event.addListener(provider::invalidata);

            PlayerEntity player = (PlayerEntity) event.getObject();
            if (player instanceof ServerPlayerEntity) {
                player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
                    NetworkHandler.sendToClient(new PacketDataSlots(ts.getToggleDEState()), (ServerPlayerEntity) player);
                });
            }
            LOGGER.debug("destate set!");
        }
    }

    public static void requestToggleState(PlayerEntity player, int selected){
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            int[] toggleState = ts.getToggleDEState();
            if(!player.inventory.getItem(selected).isEmpty()){
                ts.toggleDEState(selected);
            }

            NetworkHandler.sendToClient(new PacketDataSlots(toggleState), (ServerPlayerEntity) player);
            LOGGER.debug(selected + " toggled, now: " + toggleState[selected]);
        });
    }

    public static void playerClone(PlayerEvent.Clone event){
        PlayerEntity player = event.getPlayer();

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            event.getOriginal().getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(oldts -> {
                ts.setToggleDEState(oldts.getToggleDEState());
            });
            LOGGER.debug("Cloned");
        });
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        PlayerInventory inv = player.inventory;

        if(!(player instanceof ServerPlayerEntity)) return;
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            int[] toggleState = ts.getToggleDEState();

            for(int i=0; i<9; i++){
                if(toggleState[i] == 1) {
                    if(inv.getItem(i).isEmpty()) {
                        toggleState[i] = 0;
                    }
                }
            }

            ts.setToggleDEState(toggleState);
            NetworkHandler.sendToClient(new PacketDataSlots(toggleState), (ServerPlayerEntity) player);
        });
    }

    public static void dump(EntityItemPickupEvent event){
        PlayerEntity player = event.getPlayer();
        if (player == null) return;

        final ItemStack itemPicked = event.getItem().getItem();
        if (itemPicked.isEmpty()) return;

        PlayerInventory inv = player.inventory;

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {

            int[] toggleState = ts.getToggleDEState();
            int newCount = 0;

            boolean shouldDump = false;
            for (int i=0; i<9; i++){
                if(toggleState[i] == 1){
                    if (!inv.getItem(i).isEmpty() && inv.getItem(i).sameItem(itemPicked)){
                        if(inv.getItem(i).getCount() + itemPicked.getCount() >= inv.getItem(i).getMaxStackSize()) {
                            shouldDump = true;
                            if (newCount < inv.getItem(i).getMaxStackSize() - inv.getItem(i).getCount()){
                                newCount = inv.getItem(i).getMaxStackSize() - inv.getItem(i).getCount();
                            }
                        }
                    }
                }
            }
               if(shouldDump){
                   itemPicked.setCount(newCount);
               }
        });
    }
}
