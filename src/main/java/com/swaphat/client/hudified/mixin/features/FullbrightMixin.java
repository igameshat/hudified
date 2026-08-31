package com.swaphat.client.hudified.mixin.features;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lightmap.class)
public class FullbrightMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/state/LightmapRenderState;)V",
            at = @At("HEAD")
    )
    private void injectFullbright(LightmapRenderState renderState, CallbackInfo ci) {
        if (ConfigInstance.Environment.fullbright && ConfigInstance.OverlayEnabled) {
            renderState.brightness = 10.0F;
        }
    }
}