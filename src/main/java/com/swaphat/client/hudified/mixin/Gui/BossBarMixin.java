package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossBarMixin {

    // Tracks exactly if we pushed a matrix so we never forget to pop it
    @Unique
    private boolean overlayManager$matrixPushed = false;

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void overlayManager$onRenderHead(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        this.overlayManager$matrixPushed = false;
        if (!ConfigInstance.BossBar.enabled) {
            ci.cancel();
            return;
        }

        Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();

        // Flag that we successfully pushed to the stack
        this.overlayManager$matrixPushed = true;

        float scale = ConfigInstance.BossBar.scale;

        // Vanilla rendering origin points
        float centerX = guiGraphics.guiWidth() / 2.0f;
        float vanillaY = 12.0f;

        // Offsets
        float offsetX = ConfigInstance.BossBar.XOffset;
        float offsetY = ConfigInstance.BossBar.YOffset - vanillaY;

        // Apply offsets and move to the center of the bar to apply scale
        poseStack.translate(centerX + offsetX, vanillaY + offsetY);
        poseStack.scale(scale, scale);

        // Move the matrix back so vanilla drawing code works relative to the original center
        poseStack.translate(-centerX, -vanillaY);
    }

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void overlayManager$onRenderReturn(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (this.overlayManager$matrixPushed) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$matrixPushed = false;
        }
    }
}