package com.swaphat.client.hudified.mixin.overlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class PortalOverlayMixin {
    @WrapOperation(
            method = {"handlePortalTransitionEffect"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isAllowedInPortal()Z")
    )
    private boolean allowScreensInPortal(Screen instance, Operation<Boolean> original) {
        if(!ConfigInstance.OverlayEnabled) original.call(instance);
        return ConfigInstance.PortalOverlay.allowGuisInPortal || original.call(instance);
    }

    @Inject(method = {"handlePortalTransitionEffect"}, at = @At("TAIL"))
    private void adjustPortalSpeedAndOpacity(CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (!ConfigInstance.PortalOverlay.enabled) {
            player.portalEffectIntensity = 0;
            return;
        }

        float vanillaDelta = player.portalEffectIntensity - player.oPortalEffectIntensity;
        float speedMultiplier = ConfigInstance.PortalOverlay.speed;
        float maxOpacity = ConfigInstance.PortalOverlay.opacity;

        player.portalEffectIntensity = player.oPortalEffectIntensity + (vanillaDelta * speedMultiplier);

        if (player.portalEffectIntensity > maxOpacity) player.portalEffectIntensity = maxOpacity;

        if (player.portalEffectIntensity < 0) player.portalEffectIntensity = 0;

    }
}