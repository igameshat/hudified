package com.swaphat.client.overlaymanager.mixin.overlay;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.mixin.accessors.AbstractArrowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ArrowGlowMixin {

    // 1. Force the arrow to glow ONLY if it is in the air AND visible
    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void forceArrowGlow(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigInstance.ArrowHighlight.enabled) {

            if ((Object) this instanceof AbstractArrow arrow) {
                // Ensure the arrow has not hit the ground
                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {

                    boolean canSee = true;
                    // If X-Ray is OFF, we mandate a Line of Sight check
                    if (!ConfigInstance.ArrowHighlight.xrayMode) {
                        LocalPlayer player = Minecraft.getInstance().player;
                        if (player != null && !player.hasLineOfSight(arrow)) {
                            canSee = false;
                        }
                    }

                    if (canSee) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    // 2. Change the color ONLY if it is in the air AND visible
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void changeArrowGlowColor(CallbackInfoReturnable<Integer> cir) {
        if (ConfigInstance.ArrowHighlight.enabled) {

            if ((Object) this instanceof AbstractArrow arrow) {

                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {

                    boolean canSee = true;
                    if (!ConfigInstance.ArrowHighlight.xrayMode) {
                        LocalPlayer player = Minecraft.getInstance().player;
                        if (player != null && !player.hasLineOfSight(arrow)) {
                            canSee = false;
                        }
                    }

                    if (canSee) {
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
}