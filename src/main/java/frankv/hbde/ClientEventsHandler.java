package frankv.hbde;

import frankv.hbde.capability.CapabilityToggleState;
import frankv.hbde.capability.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import frankv.hbde.network.PacketToggleTrigger;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import org.lwjgl.glfw.GLFW;

public class ClientEventsHandler {
    public static KeyMapping TOGGLEDE;

    public static void setup(){
        TOGGLEDE = new KeyMapping("keybind.hbde.togglede", GLFW.GLFW_KEY_COMMA, "keybind.categories.hbde");
        ClientRegistry.registerKeyBinding(TOGGLEDE);
    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event){
        if (Minecraft.getInstance().player == null) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (TOGGLEDE.consumeClick()){
            Player player = Minecraft.getInstance().player;
            ToggleStateHandler.requestToggleState(player, player.getInventory().selected);
            NetworkHandler.CHANNEL_INSTANCE.sendToServer(new PacketToggleTrigger());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            ToggleSlotOverlay overlay = new ToggleSlotOverlay();

            assert player != null;
            player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
                int[] slots = ts.getToggleDEState();
                for(int i=0; i<slots.length; i++){
                    if(slots[i] == 1) {
                        overlay.render(i);
                    }
                }
            });
        }
    }

    public static void setClientToggleData(int[] svData){
        if(Minecraft.getInstance().player == null) return;

        Player player = Minecraft.getInstance().player;
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            ts.setToggleDEState(svData);
        });
    }
}
