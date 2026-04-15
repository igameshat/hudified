package com.swaphat.client.overlaymanager.mixin.patches.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(
            method = "submitArmWithItem",
            at = @At("HEAD")
    )
    private void overlayManager$thirdPersonBefore(ArmedEntityRenderState state, ItemStackRenderState itemState, ItemStack stack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, CallbackInfo ci) {
        if (!ConfigInstance.Shields.enabled || stack == null || !(stack.getItem() instanceof ShieldItem)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || state == null || mc.level == null) return;

        boolean isSelf = false;
        boolean isBlocking = false;

        try {
            int entityId = state.getClass().getField("id").getInt(state);
            isSelf = (entityId == mc.player.getId());

            // Check if this specific entity is blocking
            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof LivingEntity living) {
                if (living.isUsingItem() && living.getUseItem() == stack) {
                    isBlocking = true;
                }
            }
        } catch (Exception ignored) {}

        boolean isMainHand = (state.mainArm == arm);

        ConfigInstance.HandSettings handSettings;
        if (isSelf) {
            handSettings = isMainHand ? ConfigInstance.Shields.thirdPersonMain : ConfigInstance.Shields.thirdPersonOff;
        } else {
            handSettings = isMainHand ? ConfigInstance.Shields.otherPlayersMain : ConfigInstance.Shields.otherPlayersOff;
        }

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

    @Inject(
            method = "submitArmWithItem",
            at = @At("RETURN")
    )
    private void overlayManager$thirdPersonAfter(ArmedEntityRenderState state, ItemStackRenderState itemState, ItemStack stack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, CallbackInfo ci) {
        if (ConfigInstance.Shields.enabled && stack != null && stack.getItem() instanceof ShieldItem) {
            poseStack.popPose();
        }
    }
}