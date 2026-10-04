package com.swaphat.client.hudified.mixin.patches.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class ItemInHandRendererMixin {

    @Unique
    private boolean overlayManager$shouldTransform(ItemStack stack) {
        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.Shields.enabled) return false;
        return stack != null && stack.getItem() instanceof ShieldItem;
    }

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    private void overlayManager$firstPersonBefore(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!overlayManager$shouldTransform(itemStack)) return;

        ConfigInstance.ShieldSettings settings = getShieldSettings(playerState, hand);

        poseStack.pushPose();
        poseStack.translate(settings.xOffset / 100.0f, (settings.yOffset + ConfigInstance.Shields.simpleYOffset) / 100.0f, settings.zOffset / 100.0f);

        poseStack.rotateDegrees(Axis.XP, settings.rotX);
        poseStack.rotateDegrees(Axis.YP, settings.rotY);
        poseStack.rotateDegrees(Axis.ZP, settings.rotZ);

        poseStack.scale(Math.max(0, settings.scaleX), Math.max(0, settings.scaleY), Math.max(0, settings.scaleZ));
    }

    @Unique
    private static ConfigInstance.ShieldSettings getShieldSettings(PlayerRenderState playerState, InteractionHand hand) {
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;

        boolean isBlocking = playerState.avatarRenderState != null && playerState.avatarRenderState.isUsingItem && playerState.avatarRenderState.useItemHand == hand;

        ConfigInstance.HandSettings handSettings = isMainHand ?
                ConfigInstance.Shields.firstPersonMain :
                ConfigInstance.Shields.firstPersonOff;

        return isBlocking ? handSettings.blocking : handSettings.idle;
    }

    @Inject(method = "submitArmWithItem", at = @At("RETURN"))
    private void overlayManager$firstPersonAfter(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (overlayManager$shouldTransform(itemStack)) {
            poseStack.popPose();
        }
    }
}