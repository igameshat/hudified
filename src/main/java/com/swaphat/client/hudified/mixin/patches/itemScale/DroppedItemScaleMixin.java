package com.swaphat.client.hudified.mixin.patches.itemScale;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState; // <-- Updated package import
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.WeakHashMap;

@Mixin(ItemEntityRenderer.class)
public class DroppedItemScaleMixin {

    @Unique
    private static final Map<ItemEntityRenderState, Float> overlayManager$scaleCache = new WeakHashMap<>();

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void onExtractRenderState(ItemEntity entity, ItemEntityRenderState state, float partialTicks, CallbackInfo ci) {
        overlayManager$scaleCache.remove(state);

        if (!ConfigInstance.DroppedItems.enabled) {
            return;
        }

        ItemStack itemStack = entity.getItem();
        if (itemStack.isEmpty()) {
            return;
        }

        String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
        String shortId = itemId.replace("minecraft:", "");

        if (ConfigInstance.DroppedItems.itemList.contains(itemId) || ConfigInstance.DroppedItems.itemList.contains(shortId)) {
            overlayManager$scaleCache.put(state, ConfigInstance.DroppedItems.customScale);
        }
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void applyCustomItemScale(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        Float scale = overlayManager$scaleCache.get(state);

        if (scale != null && scale != 1f) {
            poseStack.scale(scale, scale, scale);
        }
    }
}