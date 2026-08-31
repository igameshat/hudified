package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class SpyglassOverlayMixin {
    @Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglassOverlayHead(GuiGraphicsExtractor guiGraphics, float scopeScale, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (!ConfigInstance.SpyglassOverlay.enabled) {
            ci.cancel();
        }
    }

    @ModifyVariable(
            method = "extractSpyglassOverlay",
            at = @At("HEAD"),
            argsOnly = true,
            name = "scale")
    private float modifySpyglassScale(float originalScale) {

        if (ConfigInstance.SpyglassOverlay.enabled && ConfigInstance.OverlayEnabled) {
            return originalScale * ConfigInstance.SpyglassOverlay.scale;
        }
        return originalScale;
    }


    @ModifyVariable(
            method = "extractVignette",
            at = @At("STORE"),
            name = "borderWarningStrength")
    private float applyVignetteOpacity(float f) {
        return Mth.clamp(f * (ConfigInstance.Vignette.enabled && ConfigInstance.OverlayEnabled ? ConfigInstance.Vignette.opacity : 1.0f), 0.0f, 1.0f);        }
}