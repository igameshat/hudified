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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.joml.Vector4f;
import net.minecraft.world.entity.Entity;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    // Inject right before the GpuBuffer.MappedView is created to write to the GPU.
    // We capture the local variables, specifically 'fogData', so we can modify its fields.
    @Inject(
            method = "setupFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createCommandEncoder()Lcom/mojang/blaze3d/systems/CommandEncoder;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onSetupFogBufferWrite(Camera camera, int renderDistance, DeltaTracker deltaTracker, float f, ClientLevel clientLevel, CallbackInfoReturnable<Vector4f> cir, float g, Vector4f vector4f, float farPlaneDistance, FogType fogType, Entity entity, FogData fogData, float j) {

        if(!ConfigInstance.OverlayEnabled) return;
        if (fogType == FogType.LAVA && ConfigInstance.Environment.clearLava) {
            fogData.environmentalStart = 0.0F;
            fogData.environmentalEnd = 50.0F;
        } else if (fogType == FogType.WATER && ConfigInstance.Environment.clearWater) {
            fogData.environmentalStart = 0.0F;
            fogData.environmentalEnd = 96.0F;
        } else if (fogType == FogType.ATMOSPHERIC || fogType == FogType.NONE) { // In 1.21.11, default is now ATMOSPHERIC
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