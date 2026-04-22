package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
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
        String path = identifier.getPath();
        if (path.contains("blindness") && !ConfigInstance.BlindnessOverlay.enabled && ConfigInstance.OverlayEnabled) {
            ci.cancel();
        } else if (path.contains("darkness") && !ConfigInstance.DarknessOverlay.enabled && ConfigInstance.OverlayEnabled) {
            ci.cancel();
        }
    }
}
