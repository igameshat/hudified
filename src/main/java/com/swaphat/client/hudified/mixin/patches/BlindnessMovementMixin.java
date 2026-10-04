package com.swaphat.client.hudified.mixin.patches;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.player.LocalPlayer;
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
        if (!ConfigInstance.OverlayEnabled) return;

        if (!ConfigInstance.BlindnessOverlay.enabled) {
            if ((Object) this instanceof LocalPlayer) {
                if (effect.equals(MobEffects.BLINDNESS)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}