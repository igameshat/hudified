package com.swaphat.client.overlaymanager.mixin.patches.totem;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public class TotemParticleMixin {

    /**
     * Redirects the call that adds the totem particle emitter.
     * Target: ParticleEngine.addEmitter(Entity, ParticleOptions, int)
     */
    @Redirect(
            method = "handleEntityEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/ParticleEngine;createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;I)V"
            )
    )
    private void redirectTotemParticles(ParticleEngine particleEngine, Entity entity, ParticleOptions particleOptions, int lifetime) {
        Minecraft client = Minecraft.getInstance();

        // Check if the particle being spawned is the Totem particle
        boolean isTotem = (particleOptions.getType() == ParticleTypes.TOTEM_OF_UNDYING);

        if (isTotem) {
            // If feature is disabled OR showParticles is false, we just "do nothing" (don't call the original method)
            if (ConfigInstance.Totem.enabled && ConfigInstance.Totem.showParticles) {
                particleEngine.createTrackingEmitter(entity, particleOptions, lifetime);
            }
        } else {
            // If it's NOT a totem particle (like an explosion or splash), let it through normally
            particleEngine.createTrackingEmitter(entity, particleOptions, lifetime);
        }
    }
}