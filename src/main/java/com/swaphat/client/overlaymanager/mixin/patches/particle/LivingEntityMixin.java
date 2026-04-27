package com.swaphat.client.overlaymanager.mixin.patches.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(
            method = "tickEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
            )
    )
    private void overlaymanager$filterAmbientPotions(
            Level level, ParticleOptions options, double x, double y, double z,
            double dx, double dy, double dz, Operation<Void> original) {

        if (!ConfigInstance.Particle.enabled) {
            original.call(level, options, x, y, z, dx, dy, dz);
            return;
        }

        LivingEntity self = (LivingEntity) (Object) this;
        boolean isLocalPlayer = self == Minecraft.getInstance().player;

        // Fetch the specific chance based on who the entity is
        float chance = isLocalPlayer ?
                ConfigInstance.Particle.selfPotionChance :
                ConfigInstance.Particle.otherPotionChance;

        if (chance >= 1.0f || (chance > 0.0f && Math.random() <= chance)) {
            original.call(level, options, x, y, z, dx, dy, dz);
        }
    }
}