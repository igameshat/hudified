package com.swaphat.client.overlaymanager.mixin.patches;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class BlockBreakingOverlayMixin {

    @Inject(
            method = {"renderBlockDestroyAnimation"},
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableBlockBreakingOverlay(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource, net.minecraft.client.renderer.state.LevelRenderState levelRenderState, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (ConfigInstance.Environment.blockBreakingOverlay) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = {"renderBlockDestroyAnimation"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/SheetedDecalTextureGenerator;<init>(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lcom/mojang/blaze3d/vertex/PoseStack$Pose;F)V"
            ),
            index = 2
    )
    private float modifyBlockBreakingScale(float originalScale) {
        if(!ConfigInstance.OverlayEnabled) return originalScale;
        return ConfigInstance.Environment.blockBreakingOverlay ? 0 : originalScale;
    }
}