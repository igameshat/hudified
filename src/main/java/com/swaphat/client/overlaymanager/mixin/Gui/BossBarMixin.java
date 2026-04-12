package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossBarMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void overlayManager$onRenderHead(GuiGraphics guiGraphics, CallbackInfo ci) {
        // 1. Handle Toggles
        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.BossBar.enabled) {
            ci.cancel();
            return;
        }

        Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();

        float scale = ConfigInstance.BossBar.scale;

        // Vanilla rendering origin points
        float centerX = guiGraphics.guiWidth() / 2.0f;
        float vanillaY = 12.0f;

        // Offsets
        float offsetX = ConfigInstance.BossBar.bossBarXOffset;
        float offsetY = ConfigInstance.BossBar.bossBarYOffset - vanillaY;

        // Apply offsets and move to the center of the bar to apply scale
        poseStack.translate(centerX + offsetX, vanillaY + offsetY);
        poseStack.scale(scale, scale);
        // Move the matrix back so vanilla drawing code works relative to the original center
        poseStack.translate(-centerX, -vanillaY);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void overlayManager$onRenderReturn(GuiGraphics guiGraphics, CallbackInfo ci) {
        // Pop the 2D matrix back to normal.
        // MUST be the exact opposite condition of the HEAD cancel!
        if (ConfigInstance.OverlayEnabled && ConfigInstance.BossBar.enabled) {
            guiGraphics.pose().popMatrix();
        }
    }
}