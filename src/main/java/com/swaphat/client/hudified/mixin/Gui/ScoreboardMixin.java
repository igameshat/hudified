package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class ScoreboardMixin {

    @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void overlayManager$onRenderHead(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) {
            return;
        }

        if (!ConfigInstance.Scoreboard.enabled) {
            ci.cancel();
            return;
        }

        graphics.pose().pushMatrix();

        float scale = ConfigInstance.Scoreboard.scale;
        float offsetX = ConfigInstance.Scoreboard.XOffset;
        float offsetY = ConfigInstance.Scoreboard.YOffset;


        float originX = graphics.guiWidth();
        float originY = graphics.guiHeight() / 2f;

        graphics.pose().translate(originX + offsetX, originY + offsetY);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-originX, -originY);
    }

    @Inject(method = "extractScoreboardSidebar", at = @At("RETURN"))
    private void overlayManager$onRenderReturn(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.OverlayEnabled && ConfigInstance.Scoreboard.enabled) {
            guiGraphics.pose().popMatrix();
        }
    }
}