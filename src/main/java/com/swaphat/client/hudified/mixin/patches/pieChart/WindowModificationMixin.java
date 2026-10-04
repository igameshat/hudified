package com.swaphat.client.hudified.mixin.patches.pieChart;

import com.mojang.blaze3d.platform.Window;
import com.swaphat.client.hudified.config.ConfigInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowModificationMixin {

    @Inject(method = "onResize(II)V", at = @At("HEAD"))
    private void handleResize(int newWidth, int newHeight, CallbackInfo ci) {
        if (!ConfigInstance.PieChart.enabled && !ConfigInstance.OverlayEnabled) {
            return;
        }
        if (ConfigInstance.PieChart.oldWindowWidth == -1) {
            ConfigInstance.PieChart.oldWindowWidth = newWidth;
            ConfigInstance.PieChart.oldWindowHeight = newHeight;
            return;
        }

        // Update stored size
        ConfigInstance.PieChart.oldWindowWidth = newWidth;
        ConfigInstance.PieChart.oldWindowHeight = newHeight;
    }
}