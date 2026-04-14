package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class LayoutEditorScreen extends Screen {
    private final Screen parent;
    private boolean isDragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public LayoutEditorScreen(Screen parent) {
        super(Component.literal("Layout Editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        if (ConfigInstance.PieChart.x == -1) {
            ConfigInstance.PieChart.x = this.width - 115;
            ConfigInstance.PieChart.y = this.height - 5;
        }
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, "Click and Drag to reposition the Pie Chart", this.width / 2, 20, 0xFFFFFF);

        int x = ConfigInstance.PieChart.x;
        int y = ConfigInstance.PieChart.y;
        float scale = ConfigInstance.PieChart.scale;

        guiGraphics.pose().pushMatrix();

// 1. Move to the center of the widget
        guiGraphics.pose().translate(x, y);

// 2. Scale the Text and Background Box
        guiGraphics.pose().scale(scale, scale);

// 3. Move back (The @Redirect Mixin anchors the widget natively)
        guiGraphics.pose().translate(-x, -y);

// 4. Draw! (The Mixin intercepts the slices and scales them to match)
        this.minecraft.getDebugOverlay().getProfilerPieChart().render(guiGraphics);

        guiGraphics.pose().popMatrix();

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() == 0) {
            int x = ConfigInstance.PieChart.x;
            int y = ConfigInstance.PieChart.y;
            float scale = ConfigInstance.PieChart.scale;

            double mx = mouseButtonEvent.x();
            double my = mouseButtonEvent.y();

            // Multiply the hitbox boundaries by the scale variable
            if (mx >= x - (110 * scale) && mx <= x + (110 * scale) &&
                    my >= y - (250 * scale) && my <= y + (10 * scale)) {

                this.isDragging = true;
                this.dragOffsetX = mx - x;
                this.dragOffsetY = my - y;
                return true;
            }
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double d, double e) {
        if (this.isDragging) {
            ConfigInstance.PieChart.x = (int) (mouseButtonEvent.x() - this.dragOffsetX);
            ConfigInstance.PieChart.y = (int) (mouseButtonEvent.y() - this.dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseButtonEvent, d, e);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent mouseButtonEvent) {
        if (mouseButtonEvent.button() == 0) {
            this.isDragging = false;
        }
        return super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreen(this.parent);
    }
}