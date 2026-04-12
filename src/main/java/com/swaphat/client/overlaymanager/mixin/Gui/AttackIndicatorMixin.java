package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class AttackIndicatorMixin {

    // We use these flags to track if we successfully shifted the matrix
    // so we don't accidentally pop an empty stack and crash the game!
    @Unique
    private boolean overlayManager$pushedCrosshair = false;

    @Unique
    private boolean overlayManager$pushedHotbar = false;

    // 1. DISABLE LOGIC (Using the exact vanilla Codec structure you decompiled)
    @Redirect(
            method = {"renderCrosshair", "renderItemHotbar"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;")
    )
    private OptionInstance<AttackIndicatorStatus> hideIndicator(Options instance) {
        if (!ConfigInstance.AttackIndicator.enabled) {
            return new OptionInstance<>(
                    "options.attackIndicator",
                    OptionInstance.noTooltip(),
                    (component, value) -> value.caption(),
                    new OptionInstance.Enum<>(
                            java.util.Arrays.asList(AttackIndicatorStatus.values()),
                            AttackIndicatorStatus.LEGACY_CODEC
                    ),
                    AttackIndicatorStatus.OFF,
                    (value) -> {}
            );
        }
        return instance.attackIndicator();
    }

    // 2. MOVE CROSSHAIR INDICATOR
    // We inject right when it calculates attack strength (which happens immediately before drawing)
    @Inject(
            method = "renderCrosshair",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void moveCrosshairStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.AttackIndicator.enabled) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(ConfigInstance.AttackIndicator.hotbarXOffset, ConfigInstance.AttackIndicator.hotbarYOffset);
            this.overlayManager$pushedCrosshair = true;
        }
    }

    // We clean up at the very end of the method
    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void moveCrosshairEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.overlayManager$pushedCrosshair) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$pushedCrosshair = false;
        }
    }

    @Inject(
            method = "renderItemHotbar",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
    )
    private void moveHotbarStart(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (ConfigInstance.AttackIndicator.enabled) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(ConfigInstance.AttackIndicator.hotbarXOffset, ConfigInstance.AttackIndicator.hotbarYOffset);
            this.overlayManager$pushedHotbar = true;
        }
    }

    @Inject(method = "renderItemHotbar", at = @At("RETURN"))
    private void moveHotbarEnd(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.overlayManager$pushedHotbar) {
            guiGraphics.pose().popMatrix();
            this.overlayManager$pushedHotbar = false;
        }
    }
}