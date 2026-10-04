package com.swaphat.client.hudified.mixin.patches.pieChart;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.swaphat.client.hudified.config.ConfigInstance;
import com.swaphat.client.hudified.extras.PieChartBounds;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProfilerPieChart.class)
public class ProfilerPieChartMixin {

    @Shadow private int bottomOffset;

    @ModifyVariable(method = "extractRenderState", at = @At("HEAD"), argsOnly = true, name = "scaledScreenWidth")
    private int hudified$overrideWidth(int scaledScreenWidth) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.x == -1 && !ConfigInstance.OverlayEnabled) {
            return scaledScreenWidth;
        }
        return ConfigInstance.PieChart.x;
    }

    @ModifyVariable(method = "extractRenderState", at = @At("HEAD"), argsOnly = true, name = "scaledScreenHeight")
    private int hudified$overrideHeight(int scaledScreenHeight) {
        if (!ConfigInstance.PieChart.enabled || ConfigInstance.PieChart.y == -1 && !ConfigInstance.OverlayEnabled) {
            return scaledScreenHeight;
        }
        return ConfigInstance.PieChart.y + this.bottomOffset + 5;
    }

    @WrapOperation(
            method = "extractRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V")
    )
    private void hudified$captureBounds(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col, Operation<Void> original) {
        original.call(instance, x0, y0, x1, y1, col);
        int anchorX = x1 - 5;
        int anchorY = y1 - 5;
        PieChartBounds.left = anchorX - x0;
        PieChartBounds.top = anchorY - y0;
    }


    @Unique
    private boolean hudified$scaled = false;

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void hudified$pushScale(GuiGraphicsExtractor graphics, int scaledScreenWidth, int scaledScreenHeight, CallbackInfo ci) {
        hudified$scaled = false;
        if (!ConfigInstance.PieChart.enabled || !ConfigInstance.OverlayEnabled) return;
        if (ConfigInstance.PieChart.x == -1) return;

        int x = ConfigInstance.PieChart.x;
        int y = ConfigInstance.PieChart.y;
        float scale = ConfigInstance.PieChart.scale;

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-x, -y);
        hudified$scaled = true;
    }

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void hudified$popScale(GuiGraphicsExtractor graphics, int scaledScreenWidth, int scaledScreenHeight, CallbackInfo ci) {
        if (hudified$scaled) {
            graphics.pose().popMatrix();
            hudified$scaled = false;
        }
    }
}