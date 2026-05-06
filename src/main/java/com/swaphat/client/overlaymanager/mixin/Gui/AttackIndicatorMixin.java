package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class AttackIndicatorMixin {

    @Unique
    private boolean overlayManager$appliedCrosshair = false;

    @Unique
    private boolean overlayManager$appliedHotbar = false;


    @Inject(
            method = "renderCrosshair",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void modifyCrosshairStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        guiGraphics.pose().pushMatrix();
        this.overlayManager$appliedCrosshair = true;

        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            float xOff = ConfigInstance.AttackIndicator.XOffset;
            float yOff = ConfigInstance.AttackIndicator.YOffset;

            float centerX = guiGraphics.guiWidth() / 2.0f;
            float centerY = guiGraphics.guiHeight() / 2.0f;

            guiGraphics.pose().translate(centerX + xOff, centerY + yOff);
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.pose().translate(-centerX, -centerY);
        } else {
            // Hide it without breaking the rest of the crosshair
            guiGraphics.pose().scale(0.0f, 0.0f);
        }
    }

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void modifyCrosshairEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (this.overlayManager$appliedCrosshair) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$appliedCrosshair = false;
        }
    }

    // ==========================================
    // 2. HOTBAR INDICATOR
    // ==========================================
    @Inject(
            method = "renderItemHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void modifyHotbarStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        guiGraphics.pose().pushMatrix();
        this.overlayManager$appliedHotbar = true;

        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            float xOff = ConfigInstance.AttackIndicator.XOffset;
            float yOff = ConfigInstance.AttackIndicator.YOffset;

            float centerX = (guiGraphics.guiWidth() / 2.0f) + 91.0f;
            float centerY = guiGraphics.guiHeight() - 20.0f;

            guiGraphics.pose().translate(centerX + xOff, centerY + yOff);
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.pose().translate(-centerX, -centerY);
        } else {
            guiGraphics.pose().scale(0.0f, 0.0f);
        }
    }

    @Inject(method = "renderItemHotbar", at = @At("RETURN"))
    private void modifyHotbarEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (this.overlayManager$appliedHotbar) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$appliedHotbar = false;
        }
    }
}