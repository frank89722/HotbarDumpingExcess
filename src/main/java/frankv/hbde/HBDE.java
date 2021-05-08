package frankv.hbde;

import frankv.hbde.data.CapabilityToggleState;
import frankv.hbde.data.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HBDE.MODID)
public class HBDE {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "hbde";
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

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class,ToggleStateHandler::attachCapability);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerClone);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerTick);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        ClientEvents.setup();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void dump(EntityItemPickupEvent event){
        PlayerEntity player = event.getPlayer();
        if (player == null) return;

        final ItemStack itemPicked = event.getItem().getItem();
        if (itemPicked.isEmpty()) return;

        PlayerInventory inv = player.inventory;

        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {

            int[] toggleState = ts.getToggleDEState();
            int newCount = 0;
            int maxSize, totalItems;

            boolean shouldDump = false;
            for (int i=0; i<9; i++){
                if(toggleState[i] == 1){
                    maxSize = totalItems = 0;
                    if(inv.getItem(i).sameItem(itemPicked)) {
                        for (int k = 0; k < inv.items.size(); k++) {
                            if (!inv.getItem(k).isEmpty() && inv.getItem(k).sameItem(inv.getItem(i))) {
                                maxSize += inv.getItem(i).getMaxStackSize();
                                totalItems += inv.getItem(k).getCount();
                            }
                        }
                        if (totalItems + itemPicked.getCount() >= maxSize) {
                            shouldDump = true;
                            final int newSize = maxSize - totalItems;
                            if (newCount < newSize) newCount = newSize;
                        }
                    }
                }
            }
            if(shouldDump){
                event.getPlayer().take(event.getItem(), event.getItem().getItem().getCount());
                itemPicked.setCount(newCount);
            }
        });
    }
}
