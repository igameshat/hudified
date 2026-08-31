package com.swaphat.client.hudified.mixin.overlay;

import com.swaphat.client.hudified.config.ConfigInstance;
import com.swaphat.client.hudified.mixin.accessors.AbstractProjectileAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ProjectileGlowMixin {

    @Shadow public abstract net.minecraft.world.level.Level level();

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void forceProjectileGlow(CallbackInfoReturnable<Boolean> cir) {
        if (!this.level().isClientSide()) return;

        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.ProjectileHighlight.enabled) return;

        if ((Object) this instanceof Projectile projectile) {
            String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString();

            if (!ConfigInstance.ProjectileHighlight.supportedProjectiles.contains(entityId)) {
                return;
            }

            if (projectile instanceof AbstractArrow arrow) {
                if (((AbstractProjectileAccessor) arrow).overlayManager$isInGround()) {
                    return;
                }
            }

            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void changeProjectileGlowColor(CallbackInfoReturnable<Integer> cir) {
        if (!this.level().isClientSide()) return;

        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.ProjectileHighlight.enabled) return;

        if ((Object) this instanceof Projectile projectile) {
            String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString();

            if (!ConfigInstance.ProjectileHighlight.supportedProjectiles.contains(entityId)) {
                return;
            }

            if (projectile instanceof AbstractArrow arrow) {
                if (((AbstractProjectileAccessor) arrow).overlayManager$isInGround()) {
                    return;
                }
            }

            // 3. Uses 'opacity', 'red', 'green', and 'blue' to create the ARGB integer
            int a = (int) (ConfigInstance.ProjectileHighlight.opacity * 255);
            int r = ConfigInstance.ProjectileHighlight.red;
            int g = ConfigInstance.ProjectileHighlight.green;
            int b = ConfigInstance.ProjectileHighlight.blue;

            int argb = (a << 24) | (r << 16) | (g << 8) | b;

            cir.setReturnValue(argb);
        }
    }
}