package com.swaphat.client.hudified.mixin.features;

import com.swaphat.client.hudified.config.ConfigInstance;
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

    @Shadow
    private void renderInstances(VertexConsumer vertexConsumer, List list, Vec3 vec3, float f, int i, float g) { }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;renderInstances(Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/phys/Vec3;FIF)V"
            )
    )
    private void redirectRenderInstances(WeatherEffectRenderer instance, VertexConsumer vertexConsumer, List list, Vec3 vec3, float f, int i, float g) {
        if (f == 1 && ConfigInstance.Environment.rainOpacity != 1 && ConfigInstance.OverlayEnabled) {
            this.renderInstances(vertexConsumer, list, vec3, f, i, g * ConfigInstance.Environment.rainOpacity);
        } else {
            this.renderInstances(vertexConsumer, list, vec3, f, i, g);
        }
    }

    @Inject(method = "getPrecipitationAt", at = @At("RETURN"), cancellable = true)
    private void onGetPrecipitationAt(Level level, BlockPos blockPos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (cir.getReturnValue() == Biome.Precipitation.SNOW && ConfigInstance.Environment.noSnow && ConfigInstance.OverlayEnabled) {
            cir.setReturnValue(Biome.Precipitation.NONE);
        }
    }


    @Inject(method = "tickRainParticles", at = @At("HEAD"), cancellable = true)
    private void onTickRainParticles(ClientLevel clientLevel, Camera camera, int i, ParticleStatus particleStatus, int j, CallbackInfo ci) {
        if (ConfigInstance.Environment.noRainParticles && ConfigInstance.OverlayEnabled) {
            ci.cancel();
        }
    }
}
