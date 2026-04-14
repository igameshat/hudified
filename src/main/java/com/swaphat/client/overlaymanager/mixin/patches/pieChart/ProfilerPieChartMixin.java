package com.swaphat.client.overlaymanager.mixin.patches.pieChart;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.util.profiling.ResultField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ProfilerPieChart.class)
public class ProfilerPieChartMixin {

    @Shadow private int bottomOffset;

    // 1. Anchor the X Center to our Config
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;guiWidth()I"))
    private int redirectWidth(GuiGraphics instance) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.x == -1) return instance.guiWidth();
        return ConfigInstance.PieChart.x + 115;
    }

    // 2. Anchor the Y Bottom to our Config
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;guiHeight()I"))
    private int redirectHeight(GuiGraphics instance) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.y == -1) return instance.guiHeight();
        return ConfigInstance.PieChart.y + this.bottomOffset + 5;
    }

    // 3. THE FIX: Intercept the unscalable Pie Slices and force them to scale!
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;submitProfilerChartRenderState(Ljava/util/List;IIII)V"))
    private void redirectPieSlices(GuiGraphics instance, List<ResultField> list, int minX, int minY, int maxX, int maxY) {
        if (!ConfigInstance.PieChart.enabled) {
            instance.submitProfilerChartRenderState(list, minX, minY, maxX, maxY);
            return;
        }

        int cx = ConfigInstance.PieChart.x;
        int cy = ConfigInstance.PieChart.y;
        float scale = ConfigInstance.PieChart.scale;

        // Apply the exact same mathematical transformation that the PoseStack applies to the text.
        // Formula: (Original Coordinate - Center Coordinate) * Scale + Center Coordinate
        int scaledMinX = (int) ((minX - cx) * scale + cx);
        int scaledMinY = (int) ((minY - cy) * scale + cy);
        int scaledMaxX = (int) ((maxX - cx) * scale + cx);
        int scaledMaxY = (int) ((maxY - cy) * scale + cy);

        // Send the perfectly scaled coordinates to the GPU
        instance.submitProfilerChartRenderState(list, scaledMinX, scaledMinY, scaledMaxX, scaledMaxY);
    }
}