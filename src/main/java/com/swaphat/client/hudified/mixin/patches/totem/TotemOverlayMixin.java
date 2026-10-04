package com.swaphat.client.hudified.mixin.patches.totem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class TotemOverlayMixin {

    @Inject(method = "renderItemActivationAnimation", at = @At("HEAD"), cancellable = true)
    private void hudified$hideTotemAnimation(PlayerRenderState playerRenderState, PoseStack poseStack, float partialTicks, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;
        if (ConfigInstance.Totem.enabled && !ConfigInstance.Totem.showTotemAnimation) {
            ci.cancel();
        }
    }
}