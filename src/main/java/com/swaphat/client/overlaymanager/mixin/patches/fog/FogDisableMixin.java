package com.swaphat.client.overlaymanager.mixin.patches.fog;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FogRenderer.class)
public class FogDisableMixin {

    @Shadow @Final private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void removeUnwantedEnvironments(CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;
        FOG_ENVIRONMENTS.removeIf(env ->
                (env instanceof BlindnessFogEnvironment && !ConfigInstance.BlindnessOverlay.enabled) ||
                        (env instanceof DarknessFogEnvironment && !ConfigInstance.DarknessOverlay.enabled)
        );
    }
}