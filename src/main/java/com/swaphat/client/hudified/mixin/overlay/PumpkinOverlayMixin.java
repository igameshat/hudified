package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class PumpkinOverlayMixin {

    @Unique
    private static Identifier lastShaderLocation;

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void captureMetadata(GuiGraphicsExtractor graphics, Identifier texture, float alpha, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        lastShaderLocation = texture;
        if (texture.getPath().equals("textures/misc/pumpkinblur.png")) {
            if (!ConfigInstance.PumpkinOverlay.enabled || ConfigInstance.PumpkinOverlay.opacity <= 0) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(
            method = "extractTextureOverlay",
            at = @At("HEAD"),
            argsOnly = true,
            name = "alpha")
    private float injectCustomAlpha(float alpha) {
        if (lastShaderLocation != null && lastShaderLocation.getPath().equals("textures/misc/pumpkinblur.png") && ConfigInstance.OverlayEnabled) {
            return ConfigInstance.PumpkinOverlay.opacity / 255f;
        }
        return alpha;
    }
}