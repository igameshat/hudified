package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class DarknessOverlayMixin {
    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void stopBlackOverlays(GuiGraphicsExtractor graphics, Identifier texture, float alpha, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        String path = texture.getPath();
        if (path.contains("blindness") && !ConfigInstance.BlindnessOverlay.enabled) {
            ci.cancel();
        } else if (path.contains("darkness") && !ConfigInstance.DarknessOverlay.enabled) {
            ci.cancel();
        }
    }
}
