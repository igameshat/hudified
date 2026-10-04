package com.swaphat.client.hudified.mixin.features;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

    @Shadow
    private void prepareInstances(VertexConsumer builder, List<WeatherEffectRenderer.ColumnInstance> columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) { }

    @Inject(
            method = "prepare(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
            at = @At("HEAD")
    )
    private void hudified$onPrepareHead(Vec3 cameraPos, WeatherRenderState renderState, CallbackInfo ci) {
        if (ConfigInstance.Environment.noSnow && ConfigInstance.OverlayEnabled) {
            renderState.snowColumns.clear();
        }
    }

    @Redirect(
            method = "prepare(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;prepareInstances(Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/phys/Vec3;FIF)V"
            )
    )
    private void hudified$redirectPrepareInstances(WeatherEffectRenderer instance, VertexConsumer builder, List<WeatherEffectRenderer.ColumnInstance> columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) {
        if (maxAlpha == 1f && ConfigInstance.Environment.rainOpacity != 1f && ConfigInstance.OverlayEnabled) {
            this.prepareInstances(builder, columns, cameraPos, maxAlpha, radius, intensity * ConfigInstance.Environment.rainOpacity);
        } else {
            this.prepareInstances(builder, columns, cameraPos, maxAlpha, radius, intensity);
        }
    }
}