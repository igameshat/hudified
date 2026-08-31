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

    // Use raw List to avoid inner-class scoping issues with ColumnInstance
    @Shadow
    private void renderInstances(VertexConsumer builder, List columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) { }

    @Inject(
            method = "render(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
            at = @At("HEAD")
    )
    private void onRenderHead(Vec3 cameraPos, WeatherRenderState renderState, CallbackInfo ci) {
        // Handle noSnow     by emptying the snow list before rendering occurs
        if (ConfigInstance.Environment.noSnow && ConfigInstance.OverlayEnabled) {
            renderState.snowColumns.clear();
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;renderInstances(Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/phys/Vec3;FIF)V"
            )
    )
    private void redirectRenderInstances(WeatherEffectRenderer instance, VertexConsumer builder, List columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) {
        // maxAlpha is 1.0F for rain and 0.8F for snow. only alter rain.
        if (maxAlpha == 1.0F && ConfigInstance.Environment.rainOpacity != 1.0F && ConfigInstance.OverlayEnabled) {
            this.renderInstances(builder, columns, cameraPos, maxAlpha, radius, intensity * ConfigInstance.Environment.rainOpacity);
        } else {
            this.renderInstances(builder, columns, cameraPos, maxAlpha, radius, intensity);
        }
    }
}