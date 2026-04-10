package com.swaphat.client.overlaymanager.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SectionWidget {
    public final String title;
    public final List<Entry> entries = new ArrayList<>();
    public boolean collapsed = false;

    // The dynamic theme color assigned per-screen (Blue, Red, or Green)
    private final int themeColor;

    // ── Constants ────────────────────────────────────────────────────────────
    private static final int HEADER_H = 14;
    private static final int ENTRY_H = 14;
    private static final int ENTRY_GAP = 2;
    private static final int COLOR_HEADER_BG = 0xCC2A2A2A;
    private static final int COLOR_ENTRY_BG = 0x88181818;
    private static final int COLOR_TEXT = 0xFFEEEEEE;
    private static final int COLOR_LABEL_DIM = 0xFFAAAAAA;

    public SectionWidget(String title, int themeColor) {
        this.title = title;
        this.themeColor = themeColor;
    }

    // ── Standard Registration (For TweakManager & Visibility) ────────────────
    public void addBoolean(String label, boolean initial, int labelColor, Consumer<Boolean> setter) {
        entries.add(new BooleanEntry(label, initial, labelColor, false, setter, null));
    }

    public void addSlider(String label, float initial, float min, float max, int labelColor, Consumer<Float> setter) {
        // Passes empty ID for standard sliders
        entries.add(new SliderEntry("", label, initial, min, max, labelColor, false, setter, null));
    }

    public void addButton(String label, Runnable onClick) {
        entries.add(new ButtonEntry(label, onClick));
    }

    // ── Automation Registration (Supports Overrides & Right-Click) ───────────
    public void addBoolean(String label, boolean initial, int labelColor, boolean isOverridden, Consumer<Boolean> setter, Runnable onRightClick) {
        entries.add(new BooleanEntry(label, initial, labelColor, isOverridden, setter, onRightClick));
    }

    // FIX: Updated to accept 9 arguments (added String id)
    public void addSlider(String id, String label, float initial, float min, float max, int labelColor, boolean isOverridden, Consumer<Float> setter, Runnable onRightClick) {
        entries.add(new SliderEntry(id, label, initial, min, max, labelColor, isOverridden, setter, onRightClick));
    }

    // ── Layout & Rendering ───────────────────────────────────────────────────
    public int getHeight() {
        return HEADER_H + (collapsed ? 0 : entries.size() * (ENTRY_H + ENTRY_GAP));
    }

    public int render(GuiGraphics g, Font font, int x, int y, int w, int mouseX, int mouseY, float delta) {
        g.fill(x, y, x + w, y + HEADER_H, COLOR_HEADER_BG);
        g.fill(x, y + HEADER_H - 1, x + w, y + HEADER_H, themeColor);

        g.drawString(font, collapsed ? "▶" : "▼", x + 2, y + 3, themeColor, false);
        g.drawString(font, title, x + 12, y + 3, COLOR_TEXT, false);

        y += HEADER_H;
        if (collapsed) return y;

        for (Entry entry : entries) {
            g.fill(x, y, x + w, y + ENTRY_H, COLOR_ENTRY_BG);
            entry.render(g, font, x + 4, y, w - 8, ENTRY_H, mouseX, mouseY, themeColor);
            y += ENTRY_H + ENTRY_GAP;
        }
        return y;
    }

    // ── Input Handling ───────────────────────────────────────────────────────
    public int mouseClicked(double mouseX, double mouseY, int button, int x, int y, int w) {
        if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY < y + HEADER_H) {
            if (button == 0) collapsed = !collapsed;
            return y + getHeight();
        }

        y += HEADER_H;
        if (collapsed) return y;

        for (Entry entry : entries) {
            if (mouseY >= y && mouseY < y + ENTRY_H) {
                if (button == 1 && entry.onRightClick != null) {
                    entry.onRightClick.run();
                } else if (button == 0) {
                    entry.mouseClicked(mouseX, mouseY, button, x + 4, y, w - 8, ENTRY_H);
                }
            }
            y += ENTRY_H + ENTRY_GAP;
        }
        return y;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (Entry entry : entries) {
            entry.mouseReleased(mouseX, mouseY, button);
        }
    }

    public int mouseDragged(double mx, double my, int btn, double dx, double dy, int x, int y, int w) {
        y += HEADER_H;
        if (collapsed) return y;
        for (Entry entry : entries) {
            entry.mouseDragged(mx, my, btn, dx, dy, x + 4, y, w - 8, ENTRY_H);
            y += ENTRY_H + ENTRY_GAP;
        }
        return y;
    }

    // ── Entry Subclasses ─────────────────────────────────────────────────────
    public abstract static class Entry {
        public final String label;
        public final int labelColor;
        public final boolean isOverridden;
        public final Runnable onRightClick;

        protected Entry(String label, int labelColor, boolean isOverridden, Runnable onRightClick) {
            this.label = label;
            this.labelColor = labelColor;
            this.isOverridden = isOverridden;
            this.onRightClick = onRightClick;
        }

        public abstract void render(GuiGraphics g, Font font, int x, int y, int w, int h, int mx, int my, int themeCol);
        public void mouseClicked(double mx, double my, int btn, int x, int y, int w, int h) {}
        public void mouseReleased(double mx, double my, int btn) {}
        public void mouseDragged(double mx, double my, int btn, double dx, double dy, int x, int y, int w, int h) {}
    }

    public static class BooleanEntry extends Entry {
        private boolean value;
        private final Consumer<Boolean> setter;

        public BooleanEntry(String label, boolean value, int labelColor, boolean isOverridden, Consumer<Boolean> setter, Runnable onRC) {
            super(label, labelColor, isOverridden, onRC);
            this.value = value;
            this.setter = setter;
        }

        @Override
        public void render(GuiGraphics g, Font font, int x, int y, int w, int h, int mx, int my, int themeCol) {
            int currentTextColor = isOverridden ? themeCol : labelColor;
            g.drawString(font, label, x, y + (h - 8) / 2, currentTextColor, false);

            int pW = 28, pX = x + w - pW, pY = y + 2, pH = h - 4;
            g.fill(pX, pY, pX + pW, pY + pH, value ? 0xFF5BAD5B : 0xFFAD5B5B);
            g.drawCenteredString(font, value ? "ON" : "OFF", pX + pW / 2, pY + (pH - 8) / 2, COLOR_TEXT);
        }

        @Override
        public void mouseClicked(double mx, double my, int btn, int x, int y, int w, int h) {
            if (mx >= x + w - 28 && mx <= x + w) { value = !value; setter.accept(value); }
        }
    }

    public static class SliderEntry extends Entry {
        private float value;
        private final float min, max;
        private final Consumer<Float> setter;
        public boolean dragging = false;
        public final String id;

        public SliderEntry(String id, String label, float value, float min, float max, int labelColor, boolean isOverridden, Consumer<Float> setter, Runnable onRC) {
            super(label, labelColor, isOverridden, onRC);
            this.id = id;
            this.value = value; this.min = min; this.max = max; this.setter = setter;
        }

        @Override
        public void render(GuiGraphics g, Font font, int x, int y, int w, int h, int mx, int my, int themeCol) {
            int currentTextColor = isOverridden ? themeCol : labelColor;
            String valStr = (value == (int) value) ? String.valueOf((int) value) : String.format("%.2f", value);
            g.drawString(font, label + ": " + valStr, x, y + (h - 8) / 2, currentTextColor, false);

            int trackW = 80, trackX = x + w - trackW, trackY = y + h / 2 - 2;
            g.fill(trackX, trackY, trackX + trackW, trackY + 4, 0xFF333333);

            float pct = Mth.clamp((value - min) / (max - min), 0, 1);
            int fillW = (int) (trackW * pct);
            g.fill(trackX, trackY, trackX + fillW, trackY + 4, themeCol);
            g.fill(trackX + fillW - 2, trackY - 1, trackX + fillW + 2, trackY + 5, 0xFFFFFFFF);
        }

        @Override
        public void mouseClicked(double mx, double my, int btn, int x, int y, int w, int h) {
            int tx = x + w - 80;
            if (mx >= tx && mx <= tx + 80) {
                this.dragging = true;
                apply(mx, tx, 80);
            }
        }

        @Override
        public void mouseReleased(double mx, double my, int btn) {
            this.dragging = false;
        }

        @Override
        public void mouseDragged(double mx, double my, int btn, double dx, double dy, int x, int y, int w, int h) {
            if (this.dragging) apply(mx, x + w - 80, 80);
        }

        private void apply(double mx, int tx, int tw) {
            value = Mth.clamp(min + (float) ((mx - tx) / tw) * (max - min), min, max);
            setter.accept(value);
        }
    }

    public static class ButtonEntry extends Entry {
        private final Runnable onClick;

        public ButtonEntry(String label, Runnable onClick) {
            super(label, COLOR_LABEL_DIM, false, null);
            this.onClick = onClick;
        }

        @Override
        public void render(GuiGraphics g, Font font, int x, int y, int w, int h, int mx, int my, int themeCol) {
            g.drawString(font, label, x, y + (h - 8) / 2, labelColor, false);
            int btnW = 40, btnX = x + w - btnW, btnY = y + 2, btnH = h - 4;
            boolean hovered = mx >= btnX && mx <= btnX + btnW && my >= btnY && my <= btnY + btnH;

            g.fill(btnX, btnY, btnX + btnW, btnY + btnH, hovered ? 0xFF666666 : 0xFF444444);
            g.drawCenteredString(font, "SET", btnX + btnW / 2, btnY + (btnH - 8) / 2, 0xFFFFFFFF);
        }

        @Override
        public void mouseClicked(double mx, double my, int btn, int x, int y, int w, int h) {
            int btnW = 40, btnX = x + w - btnW, btnY = y + 2, btnH = h - 4;
            if (mx >= btnX && mx <= btnX + btnW && my >= btnY && my <= btnY + btnH) onClick.run();
        }
    }
}