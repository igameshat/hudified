package com.swaphat.client.hudified.mixin.patches.itemScale;

import com.mojang.blaze3d.vertex.PoseStack;
import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
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

    // Safely stores the custom scale for each item without needing an external interface class
    @Unique
    private static final Map<ItemEntityRenderState, Float> overlayManager$scaleCache = new WeakHashMap<>();

    // Phase 1: Store the scale during extraction (matches your working mod's updateRenderState logic)
    @Inject(
            method = {"extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V"},
            at = @At("TAIL")
    )
    private void onExtractRenderState(ItemEntity itemEntity, ItemEntityRenderState state, float partialTicks, CallbackInfo ci) {
        // Clear old scale to prevent ghosting
        overlayManager$scaleCache.remove(state);

        if (!ConfigInstance.DroppedItems.enabled) {
            return;
        }

        ItemStack itemStack = itemEntity.getItem();
        if (itemStack.isEmpty()) {
            return;
        }

        String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
        String shortId = itemId.replace("minecraft:", "");

        if (ConfigInstance.DroppedItems.itemList.contains(itemId) || ConfigInstance.DroppedItems.itemList.contains(shortId)) {
            overlayManager$scaleCache.put(state, ConfigInstance.DroppedItems.customScale);
        }
    }

    // Phase 2: Apply the scale immediately after pushPose() (matches your working mod's render logic exactly)
    @Inject(
            method = {"submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void applyCustomItemScale(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        Float scale = overlayManager$scaleCache.get(state);

        if (scale != null && scale != 1.0f) {
            poseStack.scale(scale, scale, scale);
        }
    }
}