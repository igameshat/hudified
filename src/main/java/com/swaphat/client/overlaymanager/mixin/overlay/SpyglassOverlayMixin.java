package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class SpyglassOverlayMixin {
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglassOverlayHead(GuiGraphics guiGraphics, float scopeScale, CallbackInfo ci) {
        if (!ConfigInstance.SpyglassOverlay.enabled && !ConfigInstance.OverlayEnabled) {
            ci.cancel();
        }
    }

    @ModifyVariable(
            method = "renderSpyglassOverlay",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifySpyglassScale(float originalScale) {
        if (ConfigInstance.SpyglassOverlay.enabled && ConfigInstance.OverlayEnabled) {
            return originalScale * ConfigInstance.SpyglassOverlay.scale;
        }
        return originalScale;
    }

    @Mixin(Gui.class)
    public static class VignetteOverlayMixin {
        @ModifyVariable(
                method = "renderVignette",
                at = @At("STORE"),
                ordinal = 0
        )
        private float applyVignetteOpacity(float f) {
            return Mth.clamp(f * (ConfigInstance.Vignette.enabled && ConfigInstance.OverlayEnabled ? ConfigInstance.Vignette.opacity : 1.0f), 0.0f, 1.0f);        }
    }
}