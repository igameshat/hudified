package com.swaphat.client.overlaymanager.mixin.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class FireOverlayMixin {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void onRenderFireHead(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite, CallbackInfo ci) {

        float opacity = ConfigInstance.FireOverlay.opacity;

        if (!ConfigInstance.FireOverlay.enabled && opacity <= 0 && !ConfigInstance.OverlayEnabled) {
            ci.cancel();
            return;
        }

        poseStack.pushPose();

        float pixelOffset = -ConfigManager.ConfigHelper.pixelsToFloat(ConfigInstance.FireOverlay.offsetPixels);
        poseStack.translate(0, pixelOffset, 0);
    }

    @Inject(method = "renderFire", at = @At("RETURN"))
    private static void onRenderFireReturn(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite, CallbackInfo ci) {
        float opacity = ConfigInstance.FireOverlay.opacity;

        // Only reverse our changes if we actually pushed them in HEAD
        if (ConfigInstance.FireOverlay.enabled && opacity > 0 && ConfigInstance.OverlayEnabled) {
            poseStack.popPose();
        }
    }

    @ModifyConstant(method = "renderFire", constant = @Constant(floatValue = .9F))
    private static float modifyFireAlpha(float originalAlpha) {
        return ConfigInstance.FireOverlay.opacity / 255;
    }
}