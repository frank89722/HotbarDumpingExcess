package frankv.hbde;

import frankv.hbde.capability.CapabilityToggleState;
import frankv.hbde.capability.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HBDE.MODID)
public class HBDE {
    public static final String MODID = "hbde";
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLOGGER() {
        return LOGGER;
    }

    public HBDE() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityToggleState.register();
        NetworkHandler.register();

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ToggleStateHandler::attachCapability);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerClone);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerTick);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientEventsHandler.setup();

        MinecraftForge.EVENT_BUS.register(new ClientEventsHandler());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void dump(EntityItemPickupEvent event){
        Player player = event.getPlayer();
        if (player == null) return;

        final ItemStack itemPicked = event.getItem().getItem();
        if (itemPicked.isEmpty()) return;

        Inventory inv = player.getInventory();

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            int[] toggleState = ts.getToggleDEState();
            boolean shouldDump = false;

            for (int i=0; i<toggleState.length; i++){
                if(toggleState[i] == 1){
                    final ItemStack targetItem = inv.getItem(i);
                    if(targetItem.sameItem(itemPicked)) {
                        final int maxSize = targetItem.getMaxStackSize();
                        for (int k = 0; k < inv.items.size(); k++) {
                            final ItemStack slotItem = inv.getItem(k);
                            if (slotItem.sameItem(targetItem)) {
                                final int itemPickedCount = itemPicked.getCount();
                                final int slotItemCount = slotItem.getCount();
                                final int totalCount = itemPickedCount + slotItemCount;
                                if(totalCount >= maxSize){
                                    itemPicked.setCount(itemPickedCount - (maxSize - slotItemCount));
                                    slotItem.setCount(maxSize);
                                } else {
                                    slotItem.setCount(totalCount);
                                    itemPicked.setCount(0);
                                }
                                shouldDump = true;
                                if(itemPicked.getCount() == 0) break;
                            }
                        }
                    }
                }
            }
            if(shouldDump) {
                event.getPlayer().take(event.getItem(), event.getItem().getItem().getCount());
                itemPicked.setCount(0);
            }
        });
    }
}
