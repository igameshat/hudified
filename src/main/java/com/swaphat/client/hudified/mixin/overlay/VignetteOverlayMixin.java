package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.Hud;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Hud.class)
public class VignetteOverlayMixin {
    @ModifyVariable(
            method = "extractVignette",
            at = @At("STORE"),
            name = "borderWarningStrength")
    private float applyVignetteOpacity(float f) {
        return Mth.clamp(f * (ConfigInstance.Vignette.enabled && ConfigInstance.OverlayEnabled ? ConfigInstance.Vignette.opacity : 1), 0, 1);
    }
}