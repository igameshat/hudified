package com.swaphat.client.overlaymanager.mixin.features;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.renderer.LightTexture;
import com.mojang.blaze3d.buffers.Std140Builder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    /**
     * In 1.21.11, the GPU builds the lightmap using a chained sequence of `putFloat` calls.
     * The 4th call (ordinal = 3) writes the Night Vision float variable `n`.
     * We intercept just that specific call and force it to 1.0F if fullbright is enabled.
     */
    @Redirect(
            method = "updateLightTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putFloat(F)Lcom/mojang/blaze3d/buffers/Std140Builder;",
                    ordinal = 3
            )
    )
    private Std140Builder redirectNightVisionScale(Std140Builder instance, float originalNightVision) {
        if (ConfigInstance.Environment.fullbright) {
            // 1.0F forces the shader to render with maximum Night Vision brightness
            return instance.putFloat(1.0F);
        }
        // Otherwise, pass through the vanilla value (usually 0.0F)
        return instance.putFloat(originalNightVision);
    }
}