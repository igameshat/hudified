package com.swaphat.client.hudified.mixin.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.hudified.config.ConfigInstance;
import com.swaphat.client.hudified.config.ConfigManager;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class FireOverlayMixin {

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
    private static void onSubmitFireHead(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;

        float opacity = ConfigInstance.FireOverlay.opacity;

        if (ConfigInstance.FireOverlay.enabled && opacity <= 0.0F) {
            ci.cancel();
            return;
        }

        if (ConfigInstance.FireOverlay.enabled) {
            poseStack.pushPose();
            float pixelOffset = -ConfigManager.ConfigHelper.pixelsToFloat(ConfigInstance.FireOverlay.offsetPixels);
            poseStack.translate(0, pixelOffset, 0);
        }
    }

    @Inject(method = "submitFire", at = @At("RETURN"))
    private static void onSubmitFireReturn(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;

        if (ConfigInstance.FireOverlay.enabled && ConfigInstance.FireOverlay.opacity > 0.0F) {
            poseStack.popPose();
        }
    }

    @ModifyArg(
            method = "buildFireQuad",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;buildSpriteQuad(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;FFFFFI)V"
            ),
            index = 8
    )
    private static int modifyFireAlpha(int originalColor) {
        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.FireOverlay.enabled) {
            return originalColor;
        }

        float opacity = ConfigInstance.FireOverlay.opacity;
        int alpha = (int) (opacity * 255.0F);

        alpha = Math.clamp(alpha, 0, 255);

        return (alpha << 24) | (originalColor & 0xFFFFFF);
    }
}