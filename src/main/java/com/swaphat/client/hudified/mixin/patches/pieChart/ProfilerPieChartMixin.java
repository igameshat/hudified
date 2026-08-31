package com.swaphat.client.hudified.mixin.patches.pieChart;

import com.swaphat.client.hudified.config.ConfigInstance;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import net.minecraft.util.profiling.ResultField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ProfilerPieChart.class)
public class ProfilerPieChartMixin {

    @Shadow private int bottomOffset;

    @WrapOperation(
            method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiWidth()I")
    )
    private int wrapWidth(GuiGraphicsExtractor instance, Operation<Integer> original) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.x == -1 && !ConfigInstance.OverlayEnabled) {
            return original.call(instance);
        }
        return ConfigInstance.PieChart.x;
    }

    @WrapOperation(
            method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiHeight()I")
    )
    private int wrapHeight(GuiGraphicsExtractor instance, Operation<Integer> original) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.y == -1 && !ConfigInstance.OverlayEnabled) {
            return original.call(instance);
        }
        return ConfigInstance.PieChart.y + this.bottomOffset + 5;
    }

    @WrapOperation(
            method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;profilerChart(Ljava/util/List;IIII)V")
    )
    private void wrapPieSlices(GuiGraphicsExtractor instance, List<ResultField> list, int minX, int minY, int maxX, int maxY, Operation<Void> original) {
        if (!ConfigInstance.PieChart.enabled && !ConfigInstance.OverlayEnabled) {
            original.call(instance, list, minX, minY, maxX, maxY);
            return;
        }

        int cx = ConfigInstance.PieChart.x;
        int cy = ConfigInstance.PieChart.y;
        float scale = ConfigInstance.PieChart.scale;

        int scaledMinX = (int) ((minX - cx) * scale + cx);
        int scaledMinY = (int) ((minY - cy) * scale + cy);
        int scaledMaxX = (int) ((maxX - cx) * scale + cx);
        int scaledMaxY = (int) ((maxY - cy) * scale + cy);

        // Send the perfectly scaled coordinates to the GPU extractor
        original.call(instance, list, scaledMinX, scaledMinY, scaledMaxX, scaledMaxY);
    }
}