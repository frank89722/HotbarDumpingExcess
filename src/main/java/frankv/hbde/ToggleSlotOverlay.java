package frankv.hbde;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class ToggleSlotOverlay extends GuiComponent {
    private final Minecraft mc;
    private ResourceLocation texture = new ResourceLocation(HBDE.MODID, "textures/gui/slot_dumping.png");

    public ToggleSlotOverlay() {
        this.mc = Minecraft.getInstance();
    }

    public void render(int i) {
        final int width = this.mc.getWindow().getGuiScaledWidth();
        final int height = this.mc.getWindow().getGuiScaledHeight();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 2.0f);
        RenderSystem.setShaderTexture(0, texture);
        blit(new PoseStack(),
                width / 2 - 90 + i * 20 + 2,
                height - 16 - 3,
                0, 0, 16, 16, 16, 16
        );
    }
}
