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

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void forceArrowGlow(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigInstance.ArrowHighlight.enabled && ConfigInstance.OverlayEnabled) {

            if ((Object) this instanceof AbstractArrow arrow) {
                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null && player.hasLineOfSight(arrow)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void changeArrowGlowColor(CallbackInfoReturnable<Integer> cir) {
        if (ConfigInstance.ArrowHighlight.enabled && ConfigInstance.OverlayEnabled) {
            if ((Object) this instanceof AbstractArrow arrow) {
                if (!((AbstractArrowAccessor) arrow).overlayManager$isInGround()) {
                    LocalPlayer player = Minecraft.getInstance().player;

                    if (player != null && player.hasLineOfSight(arrow)) {
                        int r = ConfigInstance.ArrowHighlight.red;
                        int g = ConfigInstance.ArrowHighlight.green;
                        int b = ConfigInstance.ArrowHighlight.blue;
                        int a = (int) (ConfigInstance.ArrowHighlight.opacity * 255);

                        int customColor = (a << 24) | (r << 16) | (g << 8) | b;
                        cir.setReturnValue(customColor);
                    }
                }
            }
        }
    }
}