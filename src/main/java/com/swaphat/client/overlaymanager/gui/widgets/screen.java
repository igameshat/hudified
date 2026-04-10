package com.swaphat.client.overlaymanager.gui.widgets;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class screen extends Screen { // Changed name to start with capital
    private final Screen parent;

    public screen(Screen parent) {
        super(Component.literal("Overlay Manager"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 10;

        // Current value must be divided by the MAX (255) so the slider handle sits in the right spot
        double initialValue = ConfigInstance.Vignette.opacity / 255.0;

        this.addRenderableWidget(new AbstractSliderButton(x, y, 200, 20, Component.empty(), initialValue) {
            {
                this.updateMessage();
            }

            @Override
            protected void updateMessage() {
                // Cast to int for a clean display (e.g., "Opacity: 120")
                int displayValue = (int) ConfigInstance.Vignette.opacity;
                this.setMessage(Component.literal("Vignette Opacity: " + displayValue));
            }

            @Override
            protected void applyValue() {
                // this.value is always 0.0 to 1.0, so multiply by 255
                ConfigInstance.Vignette.opacity = (float) (this.value * 255);
            }
        });

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 100, this.height / 2 + 30, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}