package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class DarknessOverlayMixin {
    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void stopBlackOverlays(GuiGraphics guiGraphics, Identifier identifier, float f, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        String path = identifier.getPath();
        if (path.contains("blindness") && !ConfigInstance.BlindnessOverlay.enabled) {
            ci.cancel();
        } else if (path.contains("darkness") && !ConfigInstance.DarknessOverlay.enabled) {
            ci.cancel();
        }
    }
}
