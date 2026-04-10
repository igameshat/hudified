package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class VignetteOverlayMixin {

    /**
     * Minecraft calculates the vignette's brightness/opacity as a float 'f'.
     * We intercept that value right before it's used to draw and multiply it
     * by your config setting.
     */
    @ModifyVariable(
            method = "renderVignette",
            at = @At("STORE"),
            ordinal = 0
    )
    private float applyVignetteOpacity(float f) {
        // Multiply vanilla's brightness factor by our scale (0.0 to 1.0)
        // Clamp it to ensure it stays in a valid range (0.0 - 1.0)
        return Mth.clamp(f * (ConfigInstance.Vignette.enabled ? ConfigInstance.Vignette.opacity/255 : 1), 0.0F, 1.0F);
    }
}