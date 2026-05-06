package com.swaphat.client.overlaymanager.mixin.patches;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererPortalMixin {

    @Redirect(
            method = "*",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;portalEffectIntensity:F")
    )
    private float stopCameraShake(LocalPlayer instance) {
        return (!ConfigInstance.PortalOverlay.allowCameraShake && !ConfigInstance.OverlayEnabled) ? 0 : instance.portalEffectIntensity;
    }

    @Redirect(
            method = "*",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;oPortalEffectIntensity:F")
    )
    private float stopOldCameraShake(LocalPlayer instance) {
        return (!ConfigInstance.PortalOverlay.allowCameraShake && !ConfigInstance.OverlayEnabled) ? 0 : instance.portalEffectIntensity;
    }
}
