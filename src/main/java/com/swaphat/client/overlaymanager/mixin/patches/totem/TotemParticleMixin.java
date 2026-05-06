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

        if(!ConfigInstance.OverlayEnabled) return;
        boolean isTotem = (particleOptions.getType() == ParticleTypes.TOTEM_OF_UNDYING);

        if (isTotem) {
            if (ConfigInstance.Totem.enabled && ConfigInstance.Totem.showParticles && ConfigInstance.OverlayEnabled) {
                particleEngine.createTrackingEmitter(entity, particleOptions, lifetime);
            }
        } else {
            particleEngine.createTrackingEmitter(entity, particleOptions, lifetime);
        }
    }
}