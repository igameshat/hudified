package com.swaphat.client.overlaymanager.mixin.patches.pieChart;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.extras.PieChartLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.SingleTickProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "finishProfilers(ZLnet/minecraft/util/profiling/SingleTickProfiler;)V", at = @At("HEAD"))
    private void drawProfilerResultsHead(boolean showChart, SingleTickProfiler singleTickProfiler, CallbackInfo info) {
        if (!ConfigInstance.OverlayEnabled) return;
        PieChartLogic.updateValues();
        ConfigInstance.PieChart.renderingPieChart = true;
        ConfigInstance.PieChart.windowIndex = 0;
    }

    @Inject(method = "finishProfilers(ZLnet/minecraft/util/profiling/SingleTickProfiler;)V", at = @At("TAIL"))
    private void drawProfilerResultsTail(boolean showChart, SingleTickProfiler singleTickProfiler, CallbackInfo info) {
        if (!ConfigInstance.OverlayEnabled) return;
        ConfigInstance.PieChart.renderingPieChart = false;
    }
}