package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public class LayoutEditorScreen extends Screen {
    public enum EditMode { PIE_CHART, BOSS_BAR, SCOREBOARD, ATTACK_INDICATOR }

    private final Screen parent;
    private final EditMode mode;

    private boolean isDragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public LayoutEditorScreen(Screen parent, EditMode mode) {
        super(Component.literal("Layout Editor"));
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
        String elementName = switch (mode) {
            case PIE_CHART -> "Pie Chart";
            case BOSS_BAR -> "Boss Bar";
            case SCOREBOARD -> "Scoreboard";
            case ATTACK_INDICATOR -> "Attack Indicator";
        };
        guiGraphics.drawCenteredString(this.font, "Click and Drag to reposition the " + elementName, this.width / 2, 20, 0xFFFFFF);



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


        if (mode == EditMode.BOSS_BAR && ConfigInstance.BossBar.enabled) {
            int bx = (this.width / 2) + ConfigInstance.BossBar.XOffset;
            int by = ConfigInstance.BossBar.YOffset;
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



        if (mode == EditMode.SCOREBOARD && ConfigInstance.Scoreboard.enabled) {

            int sx = this.width + ConfigInstance.Scoreboard.XOffset;
            int sy = (this.height / 2) + ConfigInstance.Scoreboard.YOffset;
            float sScale = ConfigInstance.Scoreboard.scale;

            int scW = 100;
            int scH = 60;

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(sx, sy);
            guiGraphics.pose().scale(sScale, sScale);


            guiGraphics.fill(-scW, -scH / 2, 0, scH / 2, 0x880000AA);
            guiGraphics.drawCenteredString(this.font, "Scoreboard", -scW / 2, -4, 0xFFFFFF);

            guiGraphics.pose().popMatrix();
        }



        if (mode == EditMode.ATTACK_INDICATOR && ConfigInstance.AttackIndicator.enabled) {

            int ax = (this.width / 2) + ConfigInstance.AttackIndicator.XOffset;
            int ay = (this.height / 2) + ConfigInstance.AttackIndicator.YOffset;
            float aScale = ConfigInstance.AttackIndicator.scale;

            int aiSize = 18;

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(ax, ay);
            guiGraphics.pose().scale(aScale, aScale);

            guiGraphics.fill(-aiSize / 2, -aiSize / 2, aiSize / 2, aiSize / 2, 0x88AA0000);
            guiGraphics.drawCenteredString(this.font, "+", 0, -4, 0xFFFFFF);

            guiGraphics.pose().popMatrix();
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() == 0) {
            double mx = mouseButtonEvent.x();
            double my = mouseButtonEvent.y();

            if (mode == EditMode.BOSS_BAR && ConfigInstance.BossBar.enabled) {
                int bx = (this.width / 2) + ConfigInstance.BossBar.XOffset;
                int by = ConfigInstance.BossBar.YOffset;
                float bScale = ConfigInstance.BossBar.scale;

                double minX = bx - ((182 / 2.0) * bScale);
                double maxX = bx + ((182 / 2.0) * bScale);
                double maxY = by + (15 * bScale);

                if (mx >= minX && mx <= maxX && my >= (double) by && my <= maxY) {
                    this.isDragging = true;
                    this.dragOffsetX = mx - bx;
                    this.dragOffsetY = my - by;
                    return true;
                }
            }


            if (mode == EditMode.PIE_CHART && ConfigInstance.PieChart.enabled) {
                int px = ConfigInstance.PieChart.x;
                int py = ConfigInstance.PieChart.y;
                float pScale = ConfigInstance.PieChart.scale;

                // Left: perfect (-220)
                double minX = px - (220 * pScale);
                // Right: Shaved off 5 pixels
                double maxX = px - (5 * pScale);
                // Top: Shaved off 10 pixels (250 -> 240)
                double minY = py - (240 * pScale);
                // Bottom: Shaved off 1 pixel (10 -> 9)
                double maxY = py + (9 * pScale);

                if (mx >= minX && mx <= maxX && my >= minY && my <= maxY) {
                    this.isDragging = true;
                    this.dragOffsetX = mx - px;
                    this.dragOffsetY = my - py;
                    return true;
                }
            }

            if (mode == EditMode.SCOREBOARD && ConfigInstance.Scoreboard.enabled) {
                int sx = this.width + ConfigInstance.Scoreboard.XOffset;
                int sy = (this.height / 2) + ConfigInstance.Scoreboard.YOffset;
                float sScale = ConfigInstance.Scoreboard.scale;

                double minX = sx - (100 * sScale);
                double minY = sy - (30 * sScale);
                double maxY = sy + (30 * sScale);

                if (mx >= minX && mx <= (double) sx && my >= minY && my <= maxY) {
                    this.isDragging = true;
                    this.dragOffsetX = mx - sx;
                    this.dragOffsetY = my - sy;
                    return true;
                }
            }

            if (mode == EditMode.ATTACK_INDICATOR && ConfigInstance.AttackIndicator.enabled) {
                int ax = (this.width / 2) + ConfigInstance.AttackIndicator.XOffset;
                int ay = (this.height / 2) + ConfigInstance.AttackIndicator.YOffset;
                float aScale = ConfigInstance.AttackIndicator.scale;

                double minX = ax - (9 * aScale);
                double maxX = ax + (9 * aScale);
                double minY = ay - (9 * aScale);
                double maxY = ay + (9 * aScale);

                if (mx >= minX && mx <= maxX && my >= minY && my <= maxY) {
                    this.isDragging = true;
                    this.dragOffsetX = mx - ax;
                    this.dragOffsetY = my - ay;
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
                int targetX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                ConfigInstance.PieChart.x = targetX;
                ConfigInstance.PieChart.y = targetY;

            } else if (mode == EditMode.BOSS_BAR) {
                float bScale = ConfigInstance.BossBar.scale;
                int targetAbsoluteX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                int minBx = (int) (91 * bScale);
                int maxBx = (int) (this.width - (91 * bScale));
                int clampedAbsoluteX = Mth.clamp(targetAbsoluteX, minBx, maxBx);

                int minY = 0;
                int maxY = (int) (this.height - (15 * bScale));

                ConfigInstance.BossBar.XOffset = clampedAbsoluteX - (this.width / 2);
                ConfigInstance.BossBar.YOffset = Mth.clamp(targetY, minY, maxY);

            } else if (mode == EditMode.SCOREBOARD) {
                float sScale = ConfigInstance.Scoreboard.scale;
                int targetAbsoluteX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetAbsoluteY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                int minX = (int) (100 * sScale);
                int maxX = this.width;
                int minY = (int) (30 * sScale);
                int maxY = (int) (this.height - (30 * sScale));

                int clampedX = Mth.clamp(targetAbsoluteX, minX, maxX);
                int clampedY = Mth.clamp(targetAbsoluteY, minY, maxY);

                ConfigInstance.Scoreboard.XOffset = clampedX - this.width;
                ConfigInstance.Scoreboard.YOffset = clampedY - (this.height / 2);

            } else if (mode == EditMode.ATTACK_INDICATOR) {
                float aScale = ConfigInstance.AttackIndicator.scale;
                int targetAbsoluteX = (int) (mouseButtonEvent.x() - this.dragOffsetX);
                int targetAbsoluteY = (int) (mouseButtonEvent.y() - this.dragOffsetY);

                int minX = 0;
                int maxX = (int) (this.width - (1 * aScale));
                int minY = 0;
                int maxY = (int) (this.height - (1 * aScale));

                int clampedX = Mth.clamp(targetAbsoluteX, minX, maxX);
                int clampedY = Mth.clamp(targetAbsoluteY, minY, maxY);

                ConfigInstance.AttackIndicator.XOffset = clampedX - (this.width / 2);
                ConfigInstance.AttackIndicator.YOffset = clampedY - (this.height / 2);
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