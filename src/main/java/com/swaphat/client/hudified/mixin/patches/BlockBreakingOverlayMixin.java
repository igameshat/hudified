package com.swaphat.client.hudified.mixin.patches;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SubmitNodeStorage.class)
public class BlockBreakingOverlayMixin {

    @Inject(
            method = "submitBreakingBlockModel(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableBlockBreakingOverlay(PoseStack poseStack, List parts, int progress, CallbackInfo ci) {
        if (!ConfigInstance.OverlayEnabled) return;

        if (ConfigInstance.Environment.blockBreakingOverlay) {
            ci.cancel();
        }
    }
}