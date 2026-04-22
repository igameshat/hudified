package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class VignetteOverlayMixin {
    @ModifyVariable(
            method = "renderVignette",
            at = @At("STORE"),
            ordinal = 0
    )
    private float applyVignetteOpacity(float f) {
        return Mth.clamp(f * (ConfigInstance.Vignette.enabled && ConfigInstance.OverlayEnabled ? ConfigInstance.Vignette.opacity/255 : 1), 0, 1);
    }
}