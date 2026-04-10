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

            if (!ConfigInstance.FreezeOverlay.enabled) {
                ci.cancel();
                return;
            }

            ci.cancel();

            float opacityMultiplier = ConfigInstance.FreezeOverlay.opacity / 255.0F;
            int color = ARGB.white(f * opacityMultiplier);

            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();

            // Scale determines how big the corner pieces are on your screen
            float scale = ConfigInstance.FreezeOverlay.scale;
            int drawSize = (int) (128 * scale);

            // Texture Constants
            // We are sampling 128px chunks from a 256px total file
            int totalTex = 256;
            float halfTex = 128.0F;

            // 1. TOP-LEFT: Samples (0,0) to (128,128)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    0, 0,
                    0.0F, 0.0F, drawSize, drawSize, 128, 128, totalTex, totalTex, color);

            // 2. TOP-RIGHT: Samples (128,0) to (256,128)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    screenWidth - drawSize, 0,
                    halfTex, 0.0F, drawSize, drawSize, 128, 128, totalTex, totalTex, color);

            // 3. BOTTOM-LEFT: Samples (0,128) to (128,256)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    0, screenHeight - drawSize,
                    0.0F, halfTex, drawSize, drawSize, 128, 128, totalTex, totalTex, color);

            // 4. BOTTOM-RIGHT: Samples (128,128) to (256,256)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, identifier,
                    screenWidth - drawSize, screenHeight - drawSize,
                    halfTex, halfTex, drawSize, drawSize, 128, 128, totalTex, totalTex, color);
        }
    }
}