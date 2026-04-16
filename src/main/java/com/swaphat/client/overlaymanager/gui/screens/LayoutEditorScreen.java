package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth; // Added for clamping
import org.jspecify.annotations.NonNull;

public class LayoutEditorScreen extends Screen {
    public enum EditMode { PIE_CHART, BOSS_BAR }

    private final Screen parent;
    private final EditMode mode;

    private boolean isDragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public LayoutEditorScreen(Screen parent, EditMode mode) {
        super(Component.literal("Layout Editor - " + (mode == EditMode.PIE_CHART ? "Pie Chart" : "Boss Bar")));
        this.parent = parent;
        this.mode = mode;
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
        String elementName = mode == EditMode.PIE_CHART ? "Pie Chart" : "Boss Bar";
        guiGraphics.drawCenteredString(this.font, "Click and Drag to reposition the " + elementName, this.width / 2, 20, 0xFFFFFF);

        // ==========================================
        // 1. Render ONLY Pie Chart
        // ==========================================
        if (mode == EditMode.PIE_CHART && ConfigInstance.PieChart.enabled) {
            int px = ConfigInstance.PieChart.x;
            int py = ConfigInstance.PieChart.y;
            float pScale = ConfigInstance.PieChart.scale;

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(px, py);
            guiGraphics.pose().scale(pScale, pScale);
            guiGraphics.pose().translate(-px, -py);

            this.minecraft.getDebugOverlay().getProfilerPieChart().render(guiGraphics);
            guiGraphics.pose().popMatrix();
        }

        // ==========================================
        // 2. Render ONLY Boss Bar Placeholder
        // ==========================================
        if (mode == EditMode.BOSS_BAR && ConfigInstance.BossBar.enabled) {
            int bx = (this.width / 2) + ConfigInstance.BossBar.bossBarXOffset;
            int by = ConfigInstance.BossBar.bossBarYOffset;
            float bScale = ConfigInstance.BossBar.scale;

            int bbW = 182;
            int bbH = 15;

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(bx, by);
            guiGraphics.pose().scale(bScale, bScale);

            guiGraphics.fill(-bbW / 2, 0, bbW / 2, bbH, 0x8800AA00);
            guiGraphics.drawCenteredString(this.font, "Boss Bar", 0, 4, 0xFFFFFF);

            guiGraphics.pose().popMatrix();
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() == 0) {
            double mx = mouseButtonEvent.x();
            double my = mouseButtonEvent.y();

            // Handle Boss Bar Hitbox
            if (mode == EditMode.BOSS_BAR && ConfigInstance.BossBar.enabled) {
                int bx = (this.width / 2) + ConfigInstance.BossBar.bossBarXOffset;
                int by = ConfigInstance.BossBar.bossBarYOffset;
                float bScale = ConfigInstance.BossBar.scale;

                double minX = bx - ((182 / 2.0) * bScale);
                double maxX = bx + ((182 / 2.0) * bScale);
                double minY = by;
                double maxY = by + (15 * bScale);

                if (mx >= minX && mx <= maxX && my >= minY && my <= maxY) {
                    this.isDragging = true;
                    this.dragOffsetX = mx - bx;
                    this.dragOffsetY = my - by;
                    return true;
                }
            }

            // Handle Pie Chart Hitbox
            if (mode == EditMode.PIE_CHART && ConfigInstance.PieChart.enabled) {
                int px = ConfigInstance.PieChart.x;
                int py = ConfigInstance.PieChart.y;
                float pScale = ConfigInstance.PieChart.scale;

                if (mx >= px - (110 * pScale) && mx <= px + (110 * pScale) &&
                        my >= py - (250 * pScale) && my <= py + (10 * pScale)) {

                    this.isDragging = true;
                    this.dragOffsetX = mx - px;
                    this.dragOffsetY = my - py;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double d, double e) {
        if (this.isDragging) {
            if (mode == EditMode.PIE_CHART) {
                float pScale = ConfigInstance.PieChart.scale;
                int targetX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                // Calculate screen bounds accounting for scale
                int minX = (int) (110 * pScale);
                int maxX = (int) (this.width - (110 * pScale));
                int minY = (int) (250 * pScale);
                int maxY = (int) (this.height - (10 * pScale));

                ConfigInstance.PieChart.x = Mth.clamp(targetX, minX, maxX);
                ConfigInstance.PieChart.y = Mth.clamp(targetY, minY, maxY);

            } else if (mode == EditMode.BOSS_BAR) {
                float bScale = ConfigInstance.BossBar.scale;
                int targetAbsoluteX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                // Calculate screen bounds accounting for scale
                int minBx = (int) (91 * bScale); // Half of 182
                int maxBx = (int) (this.width - (91 * bScale));
                int clampedAbsoluteX = Mth.clamp(targetAbsoluteX, minBx, maxBx);

                int minY = 0;
                int maxY = (int) (this.height - (15 * bScale));

                ConfigInstance.BossBar.bossBarXOffset = clampedAbsoluteX - (this.width / 2);
                ConfigInstance.BossBar.bossBarYOffset = Mth.clamp(targetY, minY, maxY);
            }
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