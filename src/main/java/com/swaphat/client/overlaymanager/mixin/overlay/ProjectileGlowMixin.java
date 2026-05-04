package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.mixin.accessors.AbstractProjectileAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ProjectileGlowMixin {

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void forceProjectileGlow(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigInstance.projectileHighlight.enabled && ConfigInstance.OverlayEnabled) {

            if ((Object) this instanceof Projectile projectile) {

                if (projectile instanceof AbstractArrow arrow) {
                    if (((AbstractProjectileAccessor) arrow).overlayManager$isInGround()) {
                        return;
                    }
                }

                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && player.hasLineOfSight(projectile)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void changeProjectileGlowColor(CallbackInfoReturnable<Integer> cir) {
        if (ConfigInstance.projectileHighlight.enabled && ConfigInstance.OverlayEnabled) {

            if ((Object) this instanceof Projectile projectile) {

                if (projectile instanceof AbstractArrow arrow) {
                    if (((AbstractProjectileAccessor) arrow).overlayManager$isInGround()) {
                        return;
                    }
                }

                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && player.hasLineOfSight(projectile)) {
                    int r = ConfigInstance.projectileHighlight.red;
                    int g = ConfigInstance.projectileHighlight.green;
                    int b = ConfigInstance.projectileHighlight.blue;
                    int a = (int) (ConfigInstance.projectileHighlight.opacity * 255);

                    cir.setReturnValue(ARGB.color(a, r, g, b));
                }
            }
        }
    }
}