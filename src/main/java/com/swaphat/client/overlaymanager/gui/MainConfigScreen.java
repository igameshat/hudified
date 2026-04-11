package com.swaphat.client.overlaymanager.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class MainConfigScreen extends Screen {

    private final Screen previousScreen;

    private static final int COLOR_PURPLE = 0xFF9C27B0;

    public MainConfigScreen(Screen previousScreen) {
        super(Component.literal("Main Configuration Hub"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        int btnW = 200;
        int btnH = 20;

        int startX = this.width / 2 - btnW / 2;
        int startY = this.height / 2 - 45;

        this.addRenderableWidget(Button.builder(
                        Component.literal("TweakManager"),
                        btn -> this.minecraft.setScreen(new TweakManager(this)))
                .pos(startX, startY)
                .size(btnW, btnH)
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.literal("Visibility Tweaks"),
                        btn -> this.minecraft.setScreen(new VisibilityConfigScreen(this)))
                .pos(startX, startY + 24)
                .size(btnW, btnH)
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.literal("Automation Rules"),
                        btn -> this.minecraft.setScreen(new AutomationConfigScreen(this)))
                .pos(startX, startY + 48)
                .size(btnW, btnH)
                .build());


        this.addRenderableWidget(Button.builder(
                        Component.literal("Done"),
                        btn -> this.minecraft.setScreen(previousScreen))
                .pos(startX, startY + 85)
                .size(btnW, btnH)
                .build());
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int panelW = 240;
        int panelH = 175;
        int panelX = this.width / 2 - panelW / 2;
        int panelY = this.height / 2 - panelH / 2 - 5;

        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xBB1A1A1A);

        drawBox(graphics, panelX, panelY, panelX + panelW, panelY + panelH, COLOR_PURPLE, 0x00000000);

        graphics.drawCenteredString(this.font, Component.literal("Mod Configuration Hub"),
                this.width / 2, panelY + 12, COLOR_PURPLE);

        graphics.drawCenteredString(this.font, Component.literal("Select a module to configure:"),
                this.width / 2, panelY + 28, 0xFFAAAAAA);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        return super.mouseDragged(event, dragX, dragY);
    }


    private void drawBox(GuiGraphics g, int x1, int y1, int x2, int y2, int outline, int fill) {
        int left   = Math.min(x1, x2);
        int right  = Math.max(x1, x2);
        int top    = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);

        if (fill != 0x00000000)
            g.fill(left + 1, top + 1, right - 1, bottom - 1, fill);

        g.fill(left,      top,        right,     top + 1,    outline);
        g.fill(left,      bottom - 1, right,     bottom,     outline);
        g.fill(left,      top + 1,    left + 1,  bottom - 1, outline);
        g.fill(right - 1, top + 1,    right,     bottom - 1, outline);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
