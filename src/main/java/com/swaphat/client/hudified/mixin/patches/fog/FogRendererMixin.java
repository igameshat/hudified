package com.swaphat.client.hudified.mixin.patches.fog;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {


    @Inject(
            method = "setupFog",
            at = @At("RETURN")
    )
    private void onSetupFogReturn(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        if (!ConfigInstance.OverlayEnabled) return;

        FogData fogData = cir.getReturnValue();

        FogType fogType = camera.getFluidInCamera();
        if (fogType == FogType.NONE) {
            fogType = FogType.ATMOSPHERIC;
        }

        float farPlaneDistance = renderDistanceInChunks * 16f;

        if (fogType == FogType.LAVA && ConfigInstance.Environment.clearLava) {
            fogData.environmentalStart = farPlaneDistance * 2f;
            fogData.environmentalEnd = farPlaneDistance * 2f;
            fogData.renderDistanceStart = farPlaneDistance * 2f;
            fogData.renderDistanceEnd = farPlaneDistance * 2f;
        } else if (fogType == FogType.WATER && ConfigInstance.Environment.clearWater) {
            fogData.environmentalStart = farPlaneDistance * 2f;
            fogData.environmentalEnd = farPlaneDistance * 2f;
            fogData.renderDistanceStart = farPlaneDistance * 2f;
            fogData.renderDistanceEnd = farPlaneDistance * 2f;
        } else if (fogType == FogType.ATMOSPHERIC) {
            if (ConfigInstance.Environment.disableFog) {
                fogData.environmentalStart = farPlaneDistance * 2f;
                fogData.environmentalEnd = farPlaneDistance * 2f;
                fogData.renderDistanceStart = farPlaneDistance * 2f;
                fogData.renderDistanceEnd = farPlaneDistance * 2f;
                fogData.skyEnd = farPlaneDistance * 2f;
            } else if (ConfigInstance.Environment.fogMultiplier != 1.0f) {
                fogData.environmentalStart *= ConfigInstance.Environment.fogMultiplier;
                fogData.environmentalEnd *= ConfigInstance.Environment.fogMultiplier;
                fogData.renderDistanceStart = farPlaneDistance * ConfigInstance.Environment.fogMultiplier * 0.75f;
                fogData.renderDistanceEnd = farPlaneDistance * ConfigInstance.Environment.fogMultiplier;
            }
        }
    }
}