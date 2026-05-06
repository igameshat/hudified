package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class PumpkinOverlayMixin {

    private static Identifier lastShaderLocation;

    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void captureMetadata(GuiGraphics guiGraphics, Identifier shaderLocation, float alpha, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        lastShaderLocation = shaderLocation;
        if (shaderLocation.getPath().equals("textures/misc/pumpkinblur.png")) {
            if (!ConfigInstance.PumpkinOverlay.enabled || ConfigInstance.PumpkinOverlay.opacity <= 0) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(
            method = "renderTextureOverlay",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0 // Targets the first float argument (which is 'alpha')
    )
    private float injectCustomAlpha(float originalAlpha) {
        if (lastShaderLocation != null && lastShaderLocation.getPath().equals("textures/misc/pumpkinblur.png") && ConfigInstance.OverlayEnabled) {
            return ConfigInstance.PumpkinOverlay.opacity / 255.0F;
        }
        return originalAlpha;
    }
}