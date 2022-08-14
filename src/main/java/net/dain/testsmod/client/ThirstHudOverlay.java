package net.dain.testsmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dain.testsmod.TestsMod;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ThirstHudOverlay {
    public static final ResourceLocation FILLED_THIRST = new ResourceLocation(TestsMod.MOD_ID,
            "textures/thirst/filled_thirst.png");
    public static final ResourceLocation MID_THIRST = new ResourceLocation(TestsMod.MOD_ID,
            "textures/thirst/mid_thirst.png");
    public static final ResourceLocation EMPTY_THIRST = new ResourceLocation(TestsMod.MOD_ID,
            "textures/thirst/empty_thirst.png");

    public static final IGuiOverlay HUD_THIRST = ((gui, poseStack, partialTick, width, height) -> {


       int x = (width / 2) + 91;
       int y = height - 49;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, EMPTY_THIRST);

        for(int i = 0; i < 10; i++){
            GuiComponent.blit(
                    poseStack,
                    x - i * 8 - 9,
                    y,
                    0, 0, 9, 9, 9, 9);

        }

        RenderSystem.setShaderTexture(0, FILLED_THIRST);
        boolean odd = ClientThirstData.getPlayerThirst() % 2 != 0;
        int full = ClientThirstData.getPlayerThirst() / 2;
        int i = 0;

        for(; i < full; i++) {
            GuiComponent.blit(
                    poseStack,
                    x - i * 8 - 9,
                    y,
                    0, 0, 9, 9, 9, 9);
        }
        if(odd){
            RenderSystem.setShaderTexture(0, MID_THIRST);
            GuiComponent.blit(
                    poseStack,
                    x - i * 8 - 9,
                    y,
                    0, 0, 9, 9, 9, 9);
        }
    });

}
