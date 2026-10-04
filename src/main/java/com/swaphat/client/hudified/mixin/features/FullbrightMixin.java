package com.swaphat.client.hudified.mixin.features;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public class FullbrightMixin {

    @Shadow
    private boolean needsUpdate;

    @Unique
    private boolean hudified$wasEnabled = false;

    @Inject(
            method = "extract",
            at = @At("HEAD")
    )
    private void hudified$refreshFullbright(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        boolean enabled =
                ConfigInstance.OverlayEnabled
                        && ConfigInstance.Environment.fullbright;

        if (enabled != hudified$wasEnabled) {
            hudified$wasEnabled = enabled;
            needsUpdate = true;
        }
    }

    @Inject(
            method = "extract",
            at = @At("TAIL")
    )
    private void hudified$applyFullbright(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        if (!renderState.needsUpdate) {
            return;
        }

        if (!ConfigInstance.OverlayEnabled
                || !ConfigInstance.Environment.fullbright) {
            return;
        }

        renderState.ambientColor = new Vector3f(1f, 1f, 1f);
    }
}