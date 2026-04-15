package com.swaphat.client.overlaymanager.mixin.patches.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(
            method = "renderItem",
            at = @At("HEAD")
    )
    private void overlayManager$firstPersonBefore(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector, int seed, CallbackInfo ci) {
        if (!ConfigInstance.Shields.enabled || stack == null || !(stack.getItem() instanceof ShieldItem)) return;

        Minecraft mc = Minecraft.getInstance();

        if (entity != null && mc.player != null && entity.getId() == mc.player.getId()) {
            boolean isMainHand = displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
            boolean isOffHand = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

            if (isMainHand || isOffHand) {
                // Check if the player is actively blocking with THIS specific shield stack
                boolean isBlocking = mc.player.isUsingItem() && mc.player.getUseItem() == stack;

                ConfigInstance.HandSettings handSettings = isMainHand ?
                        ConfigInstance.Shields.firstPersonMain :
                        ConfigInstance.Shields.firstPersonOff;

                // Route to Idle or Blocking config
                ConfigInstance.ShieldSettings settings = isBlocking ? handSettings.blocking : handSettings.idle;

                double x = settings.xOffset / 100.0;
                double y = settings.yOffset / 100.0;
                float s = Math.max(0.01f, settings.scale);

                poseStack.pushPose();
                poseStack.translate(x, y, 0.0D);
                poseStack.mulPose(Axis.XP.rotationDegrees(settings.rotX));
                poseStack.mulPose(Axis.YP.rotationDegrees(settings.rotY));
                poseStack.mulPose(Axis.ZP.rotationDegrees(settings.rotZ));
                poseStack.scale(s, s, s);
            }
        }
    }

    @Inject(
            method = "renderItem",
            at = @At("RETURN")
    )
    private void overlayManager$firstPersonAfter(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector, int seed, CallbackInfo ci) {
        if (!ConfigInstance.Shields.enabled || stack == null || !(stack.getItem() instanceof ShieldItem)) return;

        Minecraft mc = Minecraft.getInstance();
        if (entity != null && mc.player != null && entity.getId() == mc.player.getId()) {
            if (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                poseStack.popPose();
            }
        }
    }
}