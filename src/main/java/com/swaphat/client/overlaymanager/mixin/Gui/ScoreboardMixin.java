package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class ScoreboardMixin {

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void overlayManager$onRenderHead(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // 1. If the master switch is off, render exactly like vanilla.
        if (!ConfigInstance.OverlayEnabled) {
            return;
        }

        // 2. If mod is on, but scoreboard is disabled, hide it.
        if (!ConfigInstance.Scoreboard.enabled) {
            ci.cancel();
            return;
        }

        // 3. Apply custom scale and offsets
        guiGraphics.pose().pushMatrix();

        float scale = ConfigInstance.Scoreboard.scale;
        float offsetX = ConfigInstance.Scoreboard.XOffset;
        float offsetY = ConfigInstance.Scoreboard.YOffset;

        // Scoreboards render on the right edge.
        // Using the right edge and center height as the focal point for scaling.
        float originX = guiGraphics.guiWidth();
        float originY = guiGraphics.guiHeight() / 2.0f;

        guiGraphics.pose().translate(originX + offsetX, originY + offsetY);
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.pose().translate(-originX, -originY);
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("RETURN"))
    private void overlayManager$onRenderReturn(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Must match the exact condition where we pushed the matrix!
        if (ConfigInstance.OverlayEnabled && ConfigInstance.Scoreboard.enabled) {
            guiGraphics.pose().popMatrix();
        }
    }
}