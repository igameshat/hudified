package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
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
        if (!ConfigInstance.OverlayEnabled) {
            return;
        }

        if (!ConfigInstance.Scoreboard.enabled) {
            ci.cancel();
            return;
        }

        guiGraphics.pose().pushMatrix();

        float scale = ConfigInstance.Scoreboard.scale;
        float offsetX = ConfigInstance.Scoreboard.XOffset;
        float offsetY = ConfigInstance.Scoreboard.YOffset;


        float originX = guiGraphics.guiWidth();
        float originY = guiGraphics.guiHeight() / 2.0f;

        guiGraphics.pose().translate(originX + offsetX, originY + offsetY);
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.pose().translate(-originX, -originY);
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("RETURN"))
    private void overlayManager$onRenderReturn(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.OverlayEnabled && ConfigInstance.Scoreboard.enabled) {
            guiGraphics.pose().popMatrix();
        }
    }
}