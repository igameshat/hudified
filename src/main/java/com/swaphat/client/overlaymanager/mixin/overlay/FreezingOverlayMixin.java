package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class FreezingOverlayMixin {

    @Inject(method = {"renderTextureOverlay"}, at = @At("HEAD"), cancellable = true)
    private void renderCornerSnappedFreeze(GuiGraphics guiGraphics, Identifier identifier, float f, CallbackInfo ci) {
        if (identifier.getPath().contains("powder_snow_outline")) {

            if (!(ConfigInstance.FreezeOverlay.enabled && ConfigInstance.OverlayEnabled)) {
                return;
            } else ci.cancel();


            float opacityMultiplier = ConfigInstance.FreezeOverlay.opacity;
            int color = ARGB.white(f * opacityMultiplier);

            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();

            int drawSizeX = (int) (640 * ConfigInstance.FreezeOverlay.Xscale);
            int drawSizeY = (int) (338.3339264 * ConfigInstance.FreezeOverlay.Yscale);

            // Texture Constants
            // We are sampling 128px chunks from a 256px total file
            int totalTex = 256;
            float halfTex = 128;

            // 1. TOP-LEFT: Samples (0,0) to (128,128)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    0, 0,
                    0, 0, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // 2. TOP-RIGHT: Samples (128,0) to (256,128)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    screenWidth - drawSizeX, 0,
                    halfTex, 0, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // 3. BOTTOM-LEFT: Samples (0,128) to (128,256)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    0, screenHeight - drawSizeY,
                    0, halfTex, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // 4. BOTTOM-RIGHT: Samples (128,128) to (256,256)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    screenWidth - drawSizeX, screenHeight - drawSizeY,
                    halfTex, halfTex, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);
        }
    }
}