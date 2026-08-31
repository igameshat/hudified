package com.swaphat.client.hudified.mixin.features;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelWeatherMixin {

    @Inject(method = "tickWeatherEffects", at = @At("HEAD"), cancellable = true)
    private void onTickWeatherEffects(CallbackInfo ci) {
        if (ConfigInstance.Environment.noRainParticles && ConfigInstance.OverlayEnabled) {
            ci.cancel();
        }
    }
}
