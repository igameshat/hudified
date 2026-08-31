package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Inject(method = "showProfilerChart()Z", at = @At("HEAD"), cancellable = true)
    private void renderPieOnHUD(CallbackInfoReturnable<Boolean> info) {
        if(!ConfigInstance.OverlayEnabled) return;
        Minecraft minecraft = Minecraft.getInstance();

        info.setReturnValue(ConfigInstance.PieChart.enabled && !minecraft.gui.hud.isHidden());
    }
}
