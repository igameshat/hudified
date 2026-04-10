package com.swaphat.client.overlaymanager.mixin.patches;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class BlindnessMovementMixin {

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    private void fakeHasEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        // We check if this specific entity is the local player
        // (This prevents every mob in the game from ignoring blindness)
        if ((Object) this instanceof net.minecraft.client.player.LocalPlayer) {

            // If the game asks if the player has Blindness, say NO.
            if (effect.value() == MobEffects.BLINDNESS.value() && ConfigInstance.BlindnessOverlay.enableSlowdown) {
                cir.setReturnValue(false);
            }
        }
    }
}