package com.swaphat.client.hudified.mixin.Gui;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class AttackIndicatorMixin {

    @Unique
    private boolean overlayManager$appliedCrosshair = false;

    @Unique
    private boolean overlayManager$appliedHotbar = false;

    @Inject(
            method = "extractCrosshair",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void modifyCrosshairStart(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        graphics.pose().pushMatrix();
        this.overlayManager$appliedCrosshair = true;

        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            float xOff = ConfigInstance.AttackIndicator.XOffset;
            float yOff = ConfigInstance.AttackIndicator.YOffset;

            float centerX = graphics.guiWidth() / 2.0f;
            float centerY = graphics.guiHeight() / 2.0f;

            graphics.pose().translate(centerX + xOff, centerY + yOff);
            graphics.pose().scale(scale, scale);
            graphics.pose().translate(-centerX, -centerY);
        } else {
            graphics.pose().scale(0.0f, 0.0f);
        }
    }

    @Inject(method = "extractCrosshair", at = @At("RETURN"))
    private void modifyCrosshairEnd(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (this.overlayManager$appliedCrosshair) {
            graphics.pose().popMatrix();
            this.overlayManager$appliedCrosshair = false;
        }
    }


    @Inject(
            method = "extractItemHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void modifyHotbarStart(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;

        graphics.pose().pushMatrix();
        this.overlayManager$appliedHotbar = true;

        if (ConfigInstance.AttackIndicator.enabled) {
            float scale = ConfigInstance.AttackIndicator.scale;
            float xOff = ConfigInstance.AttackIndicator.XOffset;
            float yOff = ConfigInstance.AttackIndicator.YOffset;

            float centerX = (graphics.guiWidth() / 2.0f) + 91.0f;
            float centerY = graphics.guiHeight() - 20.0f;

            graphics.pose().translate(centerX + xOff, centerY + yOff);
            graphics.pose().scale(scale, scale);
            graphics.pose().translate(-centerX, -centerY);
        } else {
            graphics.pose().scale(0.0f, 0.0f);
        }
    }

    @Inject(method = "extractItemHotbar", at = @At("RETURN"))
    private void modifyHotbarEnd(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if (this.overlayManager$appliedHotbar) {
            graphics.pose().popMatrix();
            this.overlayManager$appliedHotbar = false;
        }
    }
}