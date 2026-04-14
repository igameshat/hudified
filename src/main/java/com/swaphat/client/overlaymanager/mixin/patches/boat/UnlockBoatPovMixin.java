package com.swaphat.client.overlaymanager.mixin.patches.boat;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBoat.class)
public class UnlockBoatPovMixin {
    @Redirect(
            method = "clampRotation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F")
    )
    private float modifyClamp(float value, float min, float max, Entity passenger) {
        if (ConfigInstance.Boat.unlockBoatPov && ConfigInstance.Boat.enabled) return Mth.clamp(value, min, max);
        if (passenger instanceof Player) {
            if (Mth.clamp(value, min, max) != value)
                passenger.setYBodyRot(passenger.getViewYRot(1f) - Mth.clamp(value, min, max));
            return value;
        }
        return Mth.clamp(value, min, max);
    }
}
