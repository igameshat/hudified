package com.swaphat.client.hudified.mixin.patches.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.swaphat.client.hudified.config.ConfigInstance;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Unique
    private boolean overlayManager$didPush = false;

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    private void overlayManager$thirdPersonBefore(ArmedEntityRenderState state, ItemStackRenderState itemState, ItemStack stack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        overlayManager$didPush = false;

        if (!ConfigInstance.Shields.enabled || stack == null || !(stack.getItem() instanceof ShieldItem)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || state == null || mc.level == null) return;

        boolean isSelf;
        boolean isBlocking = false;

        try {
            int entityId = state.getClass().getField("id").getInt(state);
            isSelf = (entityId == mc.player.getId());

            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof LivingEntity living) {
                if (living.isUsingItem() && living.getUseItem() == stack) {
                    isBlocking = true;
                }
            }
        } catch (Exception ignored) {
            return; // Don't push if we couldn't get the entity info
        }

        boolean isMainHand = (state.mainArm == arm);

        ConfigInstance.HandSettings handSettings;
        if (isSelf) {
            handSettings = isMainHand ? ConfigInstance.Shields.thirdPersonMain : ConfigInstance.Shields.thirdPersonOff;
        } else {
            handSettings = isMainHand ? ConfigInstance.Shields.otherPlayersMain : ConfigInstance.Shields.otherPlayersOff;
        }

        ConfigInstance.ShieldSettings settings = isBlocking ? handSettings.blocking : handSettings.idle;

        poseStack.pushPose();
        overlayManager$didPush = true;

        poseStack.translate(settings.xOffset / 100.0, (settings.yOffset+ConfigInstance.Shields.simpleYOffset) / 100.0, settings.zOffset / 100.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(settings.rotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(settings.rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(settings.rotZ));
        poseStack.scale(Math.max(0, settings.scaleX), Math.max(0, settings.scaleY), Math.max(0, settings.scaleZ));
    }

    @Inject(method = "submitArmWithItem", at = @At("RETURN"))
    private void overlayManager$thirdPersonAfter(ArmedEntityRenderState state, ItemStackRenderState itemState, ItemStack stack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (overlayManager$didPush) {
            overlayManager$didPush = false;
            poseStack.popPose();
        }
    }
}