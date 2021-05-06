package frankv.hbde;

import com.mojang.blaze3d.matrix.MatrixStack;
import frankv.hbde.data.CapabilityToggleState;
import frankv.hbde.data.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import frankv.hbde.network.PacketToggleTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.glfw.GLFW;

public class ClientEvents {
    public static KeyBinding TOGGLEDE;

    public static void setup(){
        TOGGLEDE = new KeyBinding("keybind.hbde.togglede", GLFW.GLFW_KEY_COMMA, "keybind.categories.hbde");
        ClientRegistry.registerKeyBinding(TOGGLEDE);
    }

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent event){
        if (Minecraft.getInstance().player == null) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (TOGGLEDE.consumeClick()){
            PlayerEntity player = Minecraft.getInstance().player;
            ToggleStateHandler.requestToggleState(player, player.inventory.selected);
            NetworkHandler.CHANNEL_INSTANCE.sendToServer(new PacketToggleTrigger());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            String texture = "nah";
            assert player != null;
            player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
                int[] slots = ts.getToggleDEState();
                for(int i=0; i<9; i++){
                    if(slots[i] == 1) {
                        Minecraft.getInstance().textureManager.bind(new ResourceLocation(HBDE.MODID, "textures/gui/" + texture + ".png"));
                        AbstractGui.blit(new MatrixStack(),
                                Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 90 + i * 20 + 2,
                                Minecraft.getInstance().getWindow().getGuiScaledHeight() - 16 - 3,
                                0, 0, 16, 16, 16, 16
                        );
                    }
                }
            });
        }
    }

    public static void setClientToggleData(int[] svData){
        if(Minecraft.getInstance().player == null) return;

        PlayerEntity player = Minecraft.getInstance().player;
        player.getCapability(CapabilityToggleState.TOGGLE_STATE_STORAGE).ifPresent(ts -> {
            ts.setToggleDEState(svData);
        });
    }
}
