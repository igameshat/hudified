package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class FreezingOverlayMixin {

    @Inject(method = {"extractTextureOverlay"}, at = @At("HEAD"), cancellable = true)
    private void renderCornerSnappedFreeze(GuiGraphicsExtractor graphics, Identifier texture, float alpha, CallbackInfo ci) {
        if (texture.getPath().contains("powder_snow_outline")) {

            if (!ConfigInstance.FreezeOverlay.enabled || !ConfigInstance.OverlayEnabled) {
                return;
            } else ci.cancel();


            float opacityMultiplier = ConfigInstance.FreezeOverlay.opacity;
            int color = ARGB.white(alpha * opacityMultiplier);

            int screenWidth = graphics.guiWidth();
            int screenHeight = graphics.guiHeight();

            int drawSizeX = (int) (640 * ConfigInstance.FreezeOverlay.Xscale);
            int drawSizeY = (int) (338.3339264 * ConfigInstance.FreezeOverlay.Yscale);

            // Texture Constants
            // We are sampling 128px chunks from a 256px total file
            int totalTex = 256;
            float halfTex = 128;

            // TOP-LEFT: Samples (0,0) to (128,128)
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                    0, 0,
                    0, 0, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // TOP-RIGHT: Samples (128,0) to (256,128)
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                    screenWidth - drawSizeX, 0,
                    halfTex, 0, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // BOTTOM-LEFT: Samples (0,128) to (128,256)
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                    0, screenHeight - drawSizeY,
                    0, halfTex, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);

            // BOTTOM-RIGHT: Samples (128,128) to (256,256)
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                    screenWidth - drawSizeX, screenHeight - drawSizeY,
                    halfTex, halfTex, drawSizeX, drawSizeY, 128, 128, totalTex, totalTex, color);
        }
    }
}