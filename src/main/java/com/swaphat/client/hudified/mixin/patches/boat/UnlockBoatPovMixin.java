package com.swaphat.client.hudified.mixin.patches.boat;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public class UnlockBoatPovMixin {

    @Inject(method = "clampRotation", at = @At("HEAD"), cancellable = true)
    private void overlayManager$unlockPov(Entity passenger, CallbackInfo ci) {
        // Changed to && so it respects the user's specific toggle choices
        if (ConfigInstance.OverlayEnabled && ConfigInstance.Boat.enabled && ConfigInstance.Boat.unlockBoatPov) {

            // If the entity in the boat is a player, cancel the clamp completely
            if (passenger instanceof Player) {
                ci.cancel();
            }
        }
    }
}