package frankv.hbde;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class ToggleSlotOverlay extends GuiComponent {
    private final int width;
    private final int height;
    private final Minecraft mc;
    private ResourceLocation texture = new ResourceLocation(HBDE.MODID, "textures/gui/slot_dumping.png");

    public ToggleSlotOverlay() {
        this.mc = Minecraft.getInstance();
        this.width = this.mc.getWindow().getGuiScaledWidth();
        this.height = this.mc.getWindow().getGuiScaledHeight();
    }

    public void render(int i) {
        //mc.textureManager.bindForSetup(texture);
        RenderSystem.setShaderTexture(0, texture);
        blit(new PoseStack(),
                width / 2 - 90 + i * 20 + 2,
                height - 16 - 3,
                0, 0, 16, 16, 16, 16
        );
    }
}
