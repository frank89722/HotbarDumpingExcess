package frankv.hbde;

import com.mojang.blaze3d.matrix.MatrixStack;
import frankv.hbde.data.CapabilityToggleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;


public class ClientRender {
    @OnlyIn(Dist.CLIENT)
    public static void onRender(RenderGameOverlayEvent.Post event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {

            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            String texture = "nah";
            assert player != null;


            int[] slots = ClientEvents.getData();
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
        }
    }
}
