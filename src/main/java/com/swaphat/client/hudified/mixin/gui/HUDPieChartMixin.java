package com.swaphat.client.hudified.mixin.gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HUDPieChartMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderPieOnHUD(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;
        Minecraft mc = Minecraft.getInstance();

        if (ConfigInstance.PieChart.enabled && !mc.getDebugOverlay().showDebugScreen()) {
            int guiW = graphics.guiWidth();
            int guiH = graphics.guiHeight();

            if (ConfigInstance.PieChart.x == -1) {
                ConfigInstance.PieChart.x = guiW - 115;
                ConfigInstance.PieChart.y = guiH - 5;
            }

            int x = ConfigInstance.PieChart.x;
            int y = ConfigInstance.PieChart.y;
            float scale = ConfigInstance.PieChart.scale;

            graphics.pose().pushMatrix();
            graphics.pose().translate(x, y);
            graphics.pose().scale(scale, scale);
            graphics.pose().translate(-x, -y);

            // GUI-scaled size, same as the editor screen
            mc.getDebugOverlay().getProfilerPieChart().extractRenderState(graphics, guiW, guiH);

            graphics.pose().popMatrix();
        }
    }
}