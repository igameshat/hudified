package com.swaphat.client.overlaymanager.mixin.features;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

    // Shadow the private rendering method so we can call it from our Redirect
    @Shadow
    private void renderInstances(VertexConsumer vertexConsumer, List list, Vec3 vec3, float f, int i, float g) { }

    // ── 1. Reduce Rain Opacity ───────────────────────────────────────────────
    // We intercept the call to renderInstances. Vanilla passes f = 1.0F for rain
    // and f = 0.8F for snow. We check for rain, and multiply the intensity (g).
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;renderInstances(Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/phys/Vec3;FIF)V"
            )
    )
    private void redirectRenderInstances(WeatherEffectRenderer instance, VertexConsumer vertexConsumer, List list, Vec3 vec3, float f, int i, float g) {
        if (f == 1.0F && ConfigInstance.Environment.rainOpacity != 1.0f) {
            // Multiply the overall intensity (g) by our custom opacity slider
            this.renderInstances(vertexConsumer, list, vec3, f, i, g * ConfigInstance.Environment.rainOpacity);
        } else {
            // Let snow (or 100% opacity rain) render normally
            this.renderInstances(vertexConsumer, list, vec3, f, i, g);
        }
    }

    // ── 2. Clear Snow Particles ──────────────────────────────────────────────
    // Pretend the biome has NO precipitation if it is currently snowing.
    @Inject(method = "getPrecipitationAt", at = @At("RETURN"), cancellable = true)
    private void onGetPrecipitationAt(Level level, BlockPos blockPos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (cir.getReturnValue() == Biome.Precipitation.SNOW && ConfigInstance.Environment.noSnow) {
            cir.setReturnValue(Biome.Precipitation.NONE);
        }
    }

    // ── 3. Disable Rain Particles & Sounds (Splashes) ────────────────────────
    // Cancels the tick event that spawns ground splashing effects and rain noise.
    @Inject(method = "tickRainParticles", at = @At("HEAD"), cancellable = true)
    private void onTickRainParticles(ClientLevel clientLevel, Camera camera, int i, ParticleStatus particleStatus, int j, CallbackInfo ci) {
        if (ConfigInstance.Environment.noRainParticles) {
            ci.cancel();
        }
    }
}
