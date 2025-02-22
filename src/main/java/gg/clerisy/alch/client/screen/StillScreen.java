package gg.clerisy.alch.client.screen;

import gg.clerisy.alch.AlchMod;
import gg.clerisy.alch.common.StillMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class StillScreen extends AbstractContainerScreen<StillMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AlchMod.MODID, "textures/gui/still");
    public StillScreen(StillMenu stillMenu, Inventory playerInv, Component title) {
        super(stillMenu, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 156;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        drawString(stack, font, title, this.leftPos + 8, this.topPos + 3, 0x404040);
        drawString(stack, font, playerInventoryTitle, this.leftPos + 8, this.topPos + 80, 0x404040);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ExtendedButton(this.leftPos + 20, this.topPos + 40, 32, 16, Component.literal("Herro!"), btn -> {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Herro World!"), false);
            AlchMod.LOGGER.info("Herro World!");
        }));
    }
}
