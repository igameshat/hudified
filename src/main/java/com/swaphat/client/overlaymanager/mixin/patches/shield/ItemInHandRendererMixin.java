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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Unique
    private boolean overlayManager$shouldTransform(LivingEntity entity, ItemStack stack, ItemDisplayContext context) {
        // Must be enabled
        if (!ConfigInstance.OverlayEnabled || !ConfigInstance.Shields.enabled) return false;
        // Must be a shield
        if (stack == null || !(stack.getItem() instanceof ShieldItem)) return false;
        // Must be the local player
        Minecraft mc = Minecraft.getInstance();
        if (entity == null || mc.player == null || entity.getId() != mc.player.getId()) return false;
        // Must be in first person hands
        return context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
    }

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void overlayManager$firstPersonBefore(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector, int seed, CallbackInfo ci) {
        if (!overlayManager$shouldTransform(entity, stack, displayContext)) return;

        ConfigInstance.ShieldSettings settings = getShieldSettings(stack, displayContext);

        // Apply transformations
        poseStack.pushPose();
        poseStack.translate(settings.xOffset / 100.0, settings.yOffset / 100.0, settings.zOffset / 100.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(settings.rotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(settings.rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(settings.rotZ));
        poseStack.scale(Math.max(.01f, settings.scaleX), Math.max(.01f, settings.scaleY), Math.max(.01f, settings.scaleZ));
    }

    private static ConfigInstance.ShieldSettings getShieldSettings(ItemStack stack, ItemDisplayContext displayContext) {
        Minecraft mc = Minecraft.getInstance();
        boolean isMainHand = displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        assert mc.player != null;
        boolean isBlocking = mc.player.isUsingItem() && mc.player.getUseItem() == stack;

        ConfigInstance.HandSettings handSettings = isMainHand ?
                ConfigInstance.Shields.firstPersonMain :
                ConfigInstance.Shields.firstPersonOff;

        ConfigInstance.ShieldSettings settings = isBlocking ? handSettings.blocking : handSettings.idle;
        return settings;
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void overlayManager$firstPersonAfter(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector, int seed, CallbackInfo ci) {
        // Use the EXACT SAME method call to decide if we pop
        if (overlayManager$shouldTransform(entity, stack, displayContext)) {
            poseStack.popPose();
        }
    }
}