package com.swaphat.client.overlaymanager.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class DropDownWidget extends AbstractWidget {
    private boolean isOpen = false;

    public DropDownWidget(int x, int y) {
        super(x, y, 10, 10, Component.literal("DropDown"));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {

        graphics.drawString(Minecraft.getInstance().font,
                isOpen ? "v" : ">",
                this.getX() + (this.width / 2),
                this.getY() + (this.height / 2) - 4,
                0xFFFFFFFF, true);
        if(isOpen) {
            int cx = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
            int cy = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
            drawBox(cx + 5, cy + 10, cx + 50, cy + 50, 0xFF51A2CE, 0xBB252525,graphics);

        }
    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean DoubleClicked) {
        this.isOpen = !this.isOpen;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void drawBox(int x1, int y1, int x2, int y2, int outlineColor, int fillColor, GuiGraphics graphics) {
        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);

        // Fill inside
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, fillColor);

        // Outline
        graphics.fill(left, top, right, top + 1, outlineColor);           // top
        graphics.fill(left, bottom - 1, right, bottom, outlineColor);     // bottom
        graphics.fill(left, top + 1, left + 1, bottom - 1, outlineColor); // left
        graphics.fill(right - 1, top + 1, right, bottom - 1, outlineColor); // right
    }
}