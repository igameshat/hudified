package com.swaphat.client.hudified.mixin.patches.particle;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void overlaymanager$filterGlobalParticles(
            ParticleOptions options, double x, double y, double z,
            double dx, double dy, double dz, CallbackInfoReturnable<Particle> cir) {

        if (!ConfigInstance.Particle.enabled) return;

        Identifier particleId = BuiltInRegistries.PARTICLE_TYPE.getKey(options.getType());
        if (particleId == null) return;

        String idString = particleId.toString();


        float chance = ConfigInstance.Particle.customParticleChances.getOrDefault(
                idString,
                ConfigInstance.Particle.globalChance
        );

        if (chance < 1.0f) {
            if (chance <= 0.0f || Math.random() > chance) {
                cir.setReturnValue(null);
            }
        }
    }
}