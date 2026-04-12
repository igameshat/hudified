package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.mixin.accessors.AbstractArrowAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ArrowGlowMixin {

    // 1. Force the arrow to glow ONLY if it is in the air
    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void forceArrowGlow(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigInstance.ArrowHighlight.enabled) {

            if ((Object) this instanceof AbstractArrow arrow) {
                // Call the Invoker method to check the SynchedEntityData
                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    // 2. Change the color ONLY if it is in the air
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void changeArrowGlowColor(CallbackInfoReturnable<Integer> cir) {
        if (ConfigInstance.ArrowHighlight.enabled) {

            if ((Object) this instanceof AbstractArrow arrow) {

                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {
                    int r = ConfigInstance.ArrowHighlight.red;
                    int g = ConfigInstance.ArrowHighlight.green;
                    int b = ConfigInstance.ArrowHighlight.blue;

                    int customColor = (r << 16) | (g << 8) | b;
                    cir.setReturnValue(customColor);
                }
            }
        }
    }
}