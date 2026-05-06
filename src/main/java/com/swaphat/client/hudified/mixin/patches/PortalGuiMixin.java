package com.swaphat.client.hudified.mixin.patches;

import com.swaphat.client.hudified.config.ConfigInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class PortalGuiMixin {

    @Shadow private Minecraft minecraft;

    @Inject(method = {"renderPortalOverlay"}, at = @At("HEAD"), cancellable = true)
    private void bypassVanillaEasing(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if(!ConfigInstance.OverlayEnabled) return;
        if(ConfigInstance.PortalOverlay.enabled) {
            ci.cancel();
            return;
        }

        int i = ARGB.white(f);
        TextureAtlasSprite textureAtlasSprite = this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, textureAtlasSprite, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), i);
    }
}
