package com.swaphat.client.hudified.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import com.swaphat.client.hudified.config.ConfigInstance;
import com.swaphat.client.hudified.config.ConfigManager;
import com.swaphat.client.hudified.extras.PieChartBounds;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class LayoutEditorScreen extends Screen {
    public enum EditMode { PIE_CHART, BOSS_BAR, SCOREBOARD, ATTACK_INDICATOR }

    private record Box(int minX, int minY, int maxX, int maxY) {
    }

    private final Screen parent;
    private final EditMode mode;

    private boolean isDragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public LayoutEditorScreen(Screen parent, EditMode mode) {
        super(Component.translatable("config.hudified.layoutEditor.title"));
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

    private Component name() {
        return Component.translatable(switch (mode) {
            case PIE_CHART -> "config.hudified.layoutEditor.element.pieChart";
            case BOSS_BAR -> "config.hudified.layoutEditor.element.bossBar";
            case SCOREBOARD -> "config.hudified.layoutEditor.element.scoreboard";
            case ATTACK_INDICATOR -> "config.hudified.layoutEditor.element.attackIndicator";
        });
    }

    private boolean isEnabled() {
        return switch (mode) {
            case PIE_CHART -> ConfigInstance.PieChart.enabled;
            case BOSS_BAR -> ConfigInstance.BossBar.enabled;
            case SCOREBOARD -> ConfigInstance.Scoreboard.enabled;
            case ATTACK_INDICATOR -> ConfigInstance.AttackIndicator.enabled;
        };
    }

    private float scale() {
        return switch (mode) {
            case PIE_CHART -> ConfigInstance.PieChart.scale;
            case BOSS_BAR -> ConfigInstance.BossBar.scale;
            case SCOREBOARD -> ConfigInstance.Scoreboard.scale;
            case ATTACK_INDICATOR -> ConfigInstance.AttackIndicator.scale;
        };
    }

    private int fillColor() {
        return switch (mode) {
            case PIE_CHART -> 0x5533AAFF;
            case BOSS_BAR -> 0x8800AA00;
            case SCOREBOARD -> 0x880000AA;
            case ATTACK_INDICATOR -> 0x88AA0000;
        };
    }

    private int anchorX() {
        return switch (mode) {
            case PIE_CHART -> ConfigInstance.PieChart.x;
            case BOSS_BAR -> this.width / 2 + ConfigInstance.BossBar.XOffset;
            case SCOREBOARD -> this.width + ConfigInstance.Scoreboard.XOffset;
            case ATTACK_INDICATOR -> this.width / 2 + ConfigInstance.AttackIndicator.XOffset;
        };
    }

    private int anchorY() {
        return switch (mode) {
            case PIE_CHART -> ConfigInstance.PieChart.y;
            case BOSS_BAR -> ConfigInstance.BossBar.YOffset;
            case SCOREBOARD -> this.height / 2 + ConfigInstance.Scoreboard.YOffset;
            case ATTACK_INDICATOR -> this.height / 2 + ConfigInstance.AttackIndicator.YOffset;
        };
    }

    private Box box() {
        int ax = anchorX();
        int ay = anchorY();
        float s = scale();
        return switch (mode) {
            case PIE_CHART -> new Box(ax - Math.round(PieChartBounds.left * s), ay - Math.round(PieChartBounds.top * s), ax - Math.round(PieChartBounds.RIGHT * s), ay + Math.round(PieChartBounds.BOTTOM * s));            case BOSS_BAR -> new Box(ax - Math.round(91 * s), ay, ax + Math.round(91 * s), ay + Math.round(15 * s));
            case SCOREBOARD -> new Box(ax - Math.round(100 * s), ay - Math.round(30 * s), ax, ay + Math.round(30 * s));
            case ATTACK_INDICATOR -> new Box(ax - Math.round(9 * s), ay - Math.round(9 * s), ax + Math.round(9 * s), ay + Math.round(9 * s));
        };
    }

    private void moveTo(int x, int y) {
        float s = scale();
        switch (mode) {
            case PIE_CHART -> {
                ConfigInstance.PieChart.x = Math.clamp(x, Math.round(PieChartBounds.left * s), this.width + Math.round(PieChartBounds.RIGHT * s));
                ConfigInstance.PieChart.y = Math.clamp(y, Math.round(PieChartBounds.top * s), this.height - Math.round(PieChartBounds.BOTTOM * s));
            }
            case BOSS_BAR -> {
                int cx = Math.clamp(x, Math.round(91 * s), this.width - Math.round(91 * s));
                ConfigInstance.BossBar.XOffset = cx - this.width / 2;
                ConfigInstance.BossBar.YOffset = Math.clamp(y, 0, this.height - Math.round(15 * s));
            }
            case SCOREBOARD -> {
                int cx = Math.clamp(x, Math.round(100 * s), this.width);
                int cy = Math.clamp(y, Math.round(30 * s), this.height - Math.round(30 * s));
                ConfigInstance.Scoreboard.XOffset = cx - this.width;
                ConfigInstance.Scoreboard.YOffset = cy - this.height / 2;
            }
            case ATTACK_INDICATOR -> {
                int cx = Math.clamp(x, 0, this.width);
                int cy = Math.clamp(y, 0, this.height);
                ConfigInstance.AttackIndicator.XOffset = cx - this.width / 2;
                ConfigInstance.AttackIndicator.YOffset = cy - this.height / 2;
            }
        }
    }


    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics) {
        super.extractRenderState(graphics);

        graphics.centeredText(this.font, Component.translatable("config.hudified.layoutEditor.instruction" , name()), this.width / 2, 20, 0xFFFFFFFF);

        if (!isEnabled()) {
            graphics.centeredText(this.font, Component.translatable("config.hudified.layoutEditor.disabled", name()), this.width / 2, 34, 0xFFFF5555);
            return;
        }

        if (mode == EditMode.PIE_CHART) this.minecraft.getDebugOverlay().getProfilerPieChart().extractRenderState(graphics, graphics.guiWidth(), graphics.guiHeight());

        Box b = box();
        graphics.fill(b.minX(), b.minY(), b.maxX(), b.maxY(), fillColor());

        int border = isDragging ? 0xFFFFFF55 : 0xFFFFFFFF;
        graphics.fill(b.minX(), b.minY(), b.maxX(), b.minY() + 1, border);
        graphics.fill(b.minX(), b.maxY() - 1, b.maxX(), b.maxY(), border);

        graphics.fill(b.minX(), b.minY(), b.minX() + 1, b.maxY(), border);
        graphics.fill(b.maxX() - 1, b.minY(), b.maxX(), b.maxY(), border);

        graphics.centeredText(this.font, name(), (b.minX() + b.maxX()) / 2, (b.minY() + b.maxY()) / 2 - 4, 0xFFFFFFFF);
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if ((event.button() == InputConstants.MOUSE_BUTTON_LEFT || event.button() == InputConstants.MOUSE_BUTTON_RIGHT) && isEnabled()) {
            this.isDragging = true;
            this.dragOffsetX = event.x() - anchorX();
            this.dragOffsetY = event.y() - anchorY();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent event, double dx, double dy) {
        if (this.isDragging) {
            moveTo((int) (event.x() - this.dragOffsetX), (int) (event.y() - this.dragOffsetY));
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent event) {
        if (event.button() == 0) this.isDragging = false;
        return super.mouseReleased(event);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreenAndShow(this.parent);
    }
}