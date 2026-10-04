package com.swaphat.client.hudified.mixin.patches;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererPortalMixin {

    @Unique
    private boolean shouldDisableShake() {
        return ConfigInstance.OverlayEnabled && !ConfigInstance.PortalOverlay.allowCameraShake;
    }

    @Redirect(
            method = "renderLevel",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;portalEffectIntensity:F",
                    opcode = Opcodes.GETFIELD)
    )
    private float stopCameraShake(PlayerRenderState instance) {
        return shouldDisableShake() ? 0.0F : instance.portalEffectIntensity;
    }
}