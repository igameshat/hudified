package com.swaphat.client.overlaymanager.mixin.Gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BossHealthOverlay.class)
public class BossBarMixin {

    /**
     * 1. GLOBAL OFFSET & DISABLE
     * Shifts the entire HUD element (Text + Bar) vertically based on config.
     * If disabled, prevents the entire render method from running.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void applyGlobalOffset(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!ConfigInstance.BossBar.enabled) {
            ci.cancel();
            return;
        }

        // Vanilla default Y is 12. We calculate the difference.
        int yOffset = ConfigInstance.BossBar.bossBarYOffset - 12;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0.0F, (float) yOffset);
    }

    /**
     * 2. SCALE HEALTH CALCULATION (THE FIX FOR DAMAGE RENDERING)
     * We redirect the lerp math. Instead of calculating progress based on 182 pixels,
     * we calculate it based on (182 * scale). This ensures the "filled" part
     * of the bar matches the custom width of the background.
     */
    @Redirect(
            method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerpDiscrete(FII)I")
    )
    private int scaleHealthCalculation(float delta, int start, int end) {
        // 'end' is vanilla 182. We return (182 * scale) instead.
        float scale = ConfigInstance.BossBar.scale;
        return Mth.lerpDiscrete(delta, 0, (int) (182 * scale));
    }

    /**
     * 3. SCALE THE BACKGROUND & CENTER ALIGNMENT
     * This modifies the internal call to the second drawBar method.
     * We adjust 'i' (X position) for centering and 'k' (Width) for the background.
     */
    @ModifyArgs(
            method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;I[Lnet/minecraft/resources/Identifier;[Lnet/minecraft/resources/Identifier;)V")
    )
    private void scaleAndCenter(Args args) {
        float scale = ConfigInstance.BossBar.scale;

        // args.get(1) is 'i' (X Position)
        // args.get(4) is 'k' (The Width)
        int originalX = args.get(1);
        int currentWidth = args.get(4);

        int maxScaledWidth = (int) (182 * scale);

        // Calculate shift to keep the bar centered: (Original Max - New Max) / 2
        int centerShift = (182 - maxScaledWidth) / 2;

        // If the current width is 182, it's the background bar. Scale it.
        // If it's not 182, it's the progress bar (already scaled by our @Redirect).
        if (currentWidth == 182) {
            args.set(4, maxScaledWidth);
        }

        // Apply the centering shift to the X coordinate
        args.set(1, originalX + centerShift);
    }

    /**
     * 4. CLEANUP
     * Pops the matrix after the render loop is finished so other HUD elements aren't moved.
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void popGlobalOffset(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (ConfigInstance.BossBar.enabled) {
            guiGraphics.pose().popMatrix();
        }
    }
}