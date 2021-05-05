package frankv.hbde;

import frankv.hbde.data.CapabilityToggleState;
import frankv.hbde.data.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import frankv.hbde.network.PacketToggleTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.glfw.GLFW;

public class ClientEvents {
    public static KeyBinding TOGGLEDE;
    private static int[] data = new int[9];

    public static void setup(){
        TOGGLEDE = new KeyBinding("keybind.hbde.togglede", GLFW.GLFW_KEY_COMMA, "keybind.categories.hbde");
        ClientRegistry.registerKeyBinding(TOGGLEDE);

        for(int i=0; i<9; i++){
            data[i] = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void keyEvent(InputEvent.KeyInputEvent event){
        if (Minecraft.getInstance().player == null) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (TOGGLEDE.consumeClick()){
            PlayerEntity player = Minecraft.getInstance().player;
            ToggleStateHandler.requestToggleState(player, player.inventory.selected);
            NetworkHandler.CHANNEL_INSTANCE.sendToServer(new PacketToggleTrigger());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void setClientToggleData(int[] svData){
        data = svData;
        if(Minecraft.getInstance().player == null) return;

        PlayerEntity player = Minecraft.getInstance().player;
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            ts.setToggleDEState(svData);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static int[] getData() {
        return data;
    }
}
