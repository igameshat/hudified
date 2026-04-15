package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
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
    private boolean overlayManager$appliedScaling = false;

    // 1. DYNAMIC TOGGLE
    // Instead of Redirecting the OptionInstance (which is hard in 1.21.4),
    // we simply check our config and cancel the render if disabled.
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void checkCrosshairEnabled(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ConfigInstance.AttackIndicator.enabled) {
            // If the user turned it off in our mod, we check vanilla settings.
            // If vanilla is set to CROSSHAIR, we cancel so it doesn't draw.
            if (Minecraft.getInstance().options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    private void checkHotbarEnabled(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ConfigInstance.AttackIndicator.enabled) {
            if (Minecraft.getInstance().options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                ci.cancel();
            }
        }
    }

    // 2. MOVE & SCALE CROSSHAIR
    @Inject(
            method = "renderCrosshair",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void moveCrosshairStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            int xOff = ConfigInstance.AttackIndicator.hotbarXOffset;
            int yOff = ConfigInstance.AttackIndicator.hotbarYOffset;

            guiGraphics.pose().pushMatrix();

            int centerX = guiGraphics.guiWidth() / 2;
            int centerY = guiGraphics.guiHeight() / 2;

            // Pivot point scaling logic
            guiGraphics.pose().translate(centerX + xOff, centerY + yOff);
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.pose().translate(-centerX, -centerY);

            this.overlayManager$appliedScaling = true;
        }
    }
    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void moveCrosshairEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.overlayManager$appliedScaling) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$appliedScaling = false;
        }
    }

    // 3. MOVE & SCALE HOTBAR INDICATOR
    @Inject(
            method = "renderItemHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void moveHotbarStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            int xOff = ConfigInstance.AttackIndicator.hotbarXOffset;
            int yOff = ConfigInstance.AttackIndicator.hotbarYOffset;

            guiGraphics.pose().pushMatrix();

            int centerX = (guiGraphics.guiWidth() / 2) + 91;
            int centerY = guiGraphics.guiHeight() - 20;

            guiGraphics.pose().translate(centerX + xOff, centerY + yOff);
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.pose().translate(-centerX, -centerY);

            this.overlayManager$appliedScaling = true;
        }
    }

    @Inject(method = "renderItemHotbar", at = @At("RETURN"))
    private void moveHotbarEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.overlayManager$appliedScaling) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$appliedScaling = false;
        }
    }
}