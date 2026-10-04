package com.swaphat.client.hudified.mixin.patches;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientLevel.class)
public class BlockBreakingOverlayMixin {

    @ModifyVariable(
            method = "destroyBlockProgress",
            at = @At("HEAD"),
            argsOnly = true,
            name = "pos")
    private BlockPos overlayManager$disableBlockBreakingOverlay(BlockPos pos) {
        if (ConfigInstance.OverlayEnabled && ConfigInstance.Environment.blockBreakingOverlay) {
            return new BlockPos(0,-66,0);
        }
        return pos;
    }
}