package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HUDPieChartMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderPieOnHUD(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        Minecraft mc = Minecraft.getInstance();

        if (ConfigInstance.PieChart.enabled && !mc.getDebugOverlay().showDebugScreen()) {

            int x = ConfigInstance.PieChart.x;
            int y = ConfigInstance.PieChart.y;
            float scale = ConfigInstance.PieChart.scale;

            if (x == -1) {
                x = guiGraphics.guiWidth() - 115;
                y = guiGraphics.guiHeight() - 5;
                ConfigInstance.PieChart.x = x;
                ConfigInstance.PieChart.y = y;
            }

            guiGraphics.pose().pushMatrix();

            guiGraphics.pose().translate(x, y);

            guiGraphics.pose().scale(scale, scale);

            guiGraphics.pose().translate(-x, -y);

            Minecraft.getInstance().getDebugOverlay().getProfilerPieChart().render(guiGraphics);

            guiGraphics.pose().popMatrix();
        }
    }
}