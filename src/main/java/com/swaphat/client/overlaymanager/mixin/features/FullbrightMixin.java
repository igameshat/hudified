package com.swaphat.client.overlaymanager.mixin.features;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public class FullbrightMixin {

    @Redirect(
            method = "updateLightTexture(F)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F")
    )
    private float injectFullbright(Double instance) {
        if (ConfigInstance.Environment.fullbright && ConfigInstance.OverlayEnabled) {
            // 10.0F is the standard internal multiplier for maximum brightness
            return 10.0F;
        }
        // Otherwise, return the user's actual vanilla gamma setting
        return instance.floatValue();
    }
}