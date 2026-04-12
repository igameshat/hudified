package com.swaphat.client.overlaymanager.mixin.patches.totem;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class TotemOverlayMixin {

    @Shadow private ItemStack itemActivationItem;
    @Shadow private int itemActivationTicks;

    @Inject(method = "displayItemActivation", at = @At("TAIL"))
    private void onDisplayItemActivation(ItemStack itemStack, RandomSource randomSource, CallbackInfo ci) {
        if (ConfigInstance.Totem.enabled && !ConfigInstance.Totem.showTotemAnimation) {
            this.itemActivationItem = null;
            this.itemActivationTicks = 0;
        }
    }
}