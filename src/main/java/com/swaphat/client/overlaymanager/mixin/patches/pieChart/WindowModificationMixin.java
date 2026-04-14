package com.swaphat.client.overlaymanager.mixin.patches.pieChart;

import com.mojang.blaze3d.platform.Window;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowModificationMixin {

    @Inject(method = "onResize(JII)V", at = @At("HEAD"))
    private void handleResize(long window, int width, int height, CallbackInfo ci) {
        if (ConfigInstance.PieChart.oldWindowWidth == -1) {
            ConfigInstance.PieChart.oldWindowWidth = width;
            ConfigInstance.PieChart.oldWindowHeight = height;
            return;
        }

        // Calculate the ratio of the resize
        double scaleX = (double) width / ConfigInstance.PieChart.oldWindowWidth;
        double scaleY = (double) height / ConfigInstance.PieChart.oldWindowHeight;

        // Update stored size
        ConfigInstance.PieChart.oldWindowWidth = width;
        ConfigInstance.PieChart.oldWindowHeight = height;
    }
}