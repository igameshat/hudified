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
            at = @At("RETURN") // Inject at the very end when FogData is fully populated
    )
    private void onSetupFogReturn(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        if (!ConfigInstance.OverlayEnabled) return;

        // Grab the fully configured FogData object
        FogData fogData = cir.getReturnValue();

        // 1.21.11 maps NONE to ATMOSPHERIC internally
        FogType fogType = camera.getFluidInCamera();
        if (fogType == FogType.NONE) {
            fogType = FogType.ATMOSPHERIC;
        }

        // The far plane is strictly render distance * 16 blocks
        float farPlaneDistance = renderDistanceInChunks * 16.0F;

        if (fogType == FogType.LAVA && ConfigInstance.Environment.clearLava) {
            fogData.environmentalStart = 0.0F;
            fogData.environmentalEnd = 50.0F;
        } else if (fogType == FogType.WATER && ConfigInstance.Environment.clearWater) {
            fogData.environmentalStart = 0.0F;
            fogData.environmentalEnd = 96.0F;
        } else if (fogType == FogType.ATMOSPHERIC) {
            if (ConfigInstance.Environment.disableFog) {
                // Push all fog types beyond the render distance
                fogData.environmentalStart = farPlaneDistance * 2.0F;
                fogData.environmentalEnd = farPlaneDistance * 2.0F;
                fogData.renderDistanceStart = farPlaneDistance * 2.0F;
                fogData.renderDistanceEnd = farPlaneDistance * 2.0F;
                fogData.skyEnd = farPlaneDistance * 2.0F;
            } else if (ConfigInstance.Environment.fogMultiplier != 1.0f) {
                // Apply our custom multiplier
                fogData.environmentalStart *= ConfigInstance.Environment.fogMultiplier;
                fogData.environmentalEnd *= ConfigInstance.Environment.fogMultiplier;
                fogData.renderDistanceStart = farPlaneDistance * ConfigInstance.Environment.fogMultiplier * 0.75f;
                fogData.renderDistanceEnd = farPlaneDistance * ConfigInstance.Environment.fogMultiplier;
            }
        }
    }
}