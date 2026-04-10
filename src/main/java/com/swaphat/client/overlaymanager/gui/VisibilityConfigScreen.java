package com.swaphat.client.overlaymanager.gui;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.gui.widgets.SectionWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VisibilityConfigScreen extends Screen {

    private static final String CAT_LIGHT   = "LIGHTING";
    private static final String CAT_FOG     = "FOG";
    private static final String CAT_WEATHER = "WEATHER";

    private String activeCategory = CAT_LIGHT;
    private EditBox searchBox;
    private String  searchQuery = "";
    private int scrollOffset = 0;

    private static final int PANEL_TOP           = 65;
    private static final int PANEL_BOTTOM_MARGIN = 30;

    // Theme Colors
    private static final int THEME_RED    = 0xFFE53935;
    private static final int COL_WORLD    = 0xFFFFEE58; // Yellow for Lighting/Fog
    private static final int COL_WEATHER  = 0xFF26C6DA; // Cyan for Weather

    private final List<SectionWidget> sections = new ArrayList<>();
    private final Screen previousScreen;

    public VisibilityConfigScreen(Screen previousScreen) {
        super(Component.literal("Visibility Config"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        int w = this.width;

        searchBox = new EditBox(this.font, w / 2 - 120, 12, 240, 16, Component.literal("Search..."));
        searchBox.setHint(Component.literal("Search visibility settings..."));
        searchBox.setResponder(query -> {
            searchQuery = query.toLowerCase();
            rebuildSections();
        });
        this.addRenderableWidget(searchBox);

        int tabY = 34, tabW = 65, tabGap = 4;
        int totalTabW = tabW * 3 + tabGap * 2;
        int tabStartX = w / 2 - totalTabW / 2;

        addCategoryButton(tabStartX,                             tabY, tabW, CAT_LIGHT);
        addCategoryButton(tabStartX + tabW + tabGap,             tabY, tabW, CAT_FOG);
        addCategoryButton(tabStartX + (tabW + tabGap) * 2,       tabY, tabW, CAT_WEATHER);

        this.addRenderableWidget(Button.builder(
                        Component.literal("Done"),
                        btn -> this.minecraft.setScreen(previousScreen))
                .pos(w / 2 - 40, this.height - 24).size(80, 16).build());

        rebuildSections();
    }

    private void addCategoryButton(int x, int y, int w, String category) {
        this.addRenderableWidget(Button.builder(Component.literal(category), btn -> {
            activeCategory = category;
            scrollOffset = 0;
            rebuildSections();
        }).pos(x, y).size(w, 14).build());
    }

    private void rebuildSections() {
        sections.clear();

        if (searchQuery.isEmpty()) {
            switch (activeCategory) {
                case CAT_LIGHT -> sections.add(buildLightSection());
                case CAT_FOG -> sections.add(buildFogSection());
                case CAT_WEATHER -> sections.add(buildWeatherSection());
            }
        } else {
            // Build all for search results
            sections.add(buildLightSection());
            sections.add(buildFogSection());
            sections.add(buildWeatherSection());
            sections.removeIf(s -> s.entries.isEmpty());
        }
    }

    private void addIfNotEmpty(SectionWidget s) {
        if (!s.entries.isEmpty()) sections.add(s);
    }

    private SectionWidget buildLightSection() {
        SectionWidget s = new SectionWidget("Lighting Features", THEME_RED);
        s.addBoolean("Fullbright", ConfigInstance.Environment.fullbright, COL_WORLD, v -> ConfigInstance.Environment.fullbright = v);
        return filter(s);
    }

    private SectionWidget buildFogSection() {
        SectionWidget s = new SectionWidget("Fog Control", THEME_RED);
        s.addBoolean("Disable Fog Completely", ConfigInstance.Environment.disableFog, COL_WORLD, v -> ConfigInstance.Environment.disableFog = v);
        s.addSlider("Fog Distance Multiplier", ConfigInstance.Environment.fogMultiplier, 0.1f, 5.0f, COL_WORLD, v -> ConfigInstance.Environment.fogMultiplier = v);
        s.addBoolean("Clear Lava Visibility", ConfigInstance.Environment.clearLava, COL_WORLD, v -> ConfigInstance.Environment.clearLava = v);
        s.addBoolean("Clear Water Visibility", ConfigInstance.Environment.clearWater, COL_WORLD, v -> ConfigInstance.Environment.clearWater = v);
        return filter(s);
    }

    private SectionWidget buildWeatherSection() {
        SectionWidget s = new SectionWidget("Weather Tweaks", THEME_RED);
        s.addSlider("Rain Opacity", ConfigInstance.Environment.rainOpacity, 0f, 1f, COL_WEATHER, v -> ConfigInstance.Environment.rainOpacity = v);
        s.addBoolean("Disable Rain Particles", ConfigInstance.Environment.noRainParticles, COL_WEATHER, v -> ConfigInstance.Environment.noRainParticles = v);
        s.addBoolean("Disable Snow Particles", ConfigInstance.Environment.noSnow, COL_WEATHER, v -> ConfigInstance.Environment.noSnow = v);
        return filter(s);
    }

    private SectionWidget filter(SectionWidget s) {
        if (searchQuery.isEmpty()) return s;
        s.entries.removeIf(e -> !e.label.toLowerCase().contains(searchQuery) && !s.title.toLowerCase().contains(searchQuery));
        return s;
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, Component.literal("Visibility Configuration"), this.width / 2, 2, 0xFFFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);

        int tabW = 65, tabGap = 4, totalTabW = tabW * 3 + tabGap * 2, tabStartX = this.width / 2 - totalTabW / 2, tabY = 34;
        int idx = switch (activeCategory) { case CAT_LIGHT -> 0; case CAT_FOG -> 1; case CAT_WEATHER -> 2; default -> 0; };
        graphics.fill(tabStartX + idx * (tabW + tabGap), tabY + 14, tabStartX + idx * (tabW + tabGap) + tabW, tabY + 15, THEME_RED);

        int panelX = this.width / 2 - 155, panelW = 310, panelBottom = this.height - PANEL_BOTTOM_MARGIN;
        graphics.fill(panelX, PANEL_TOP, panelX + panelW, panelBottom, 0xBB1A1A1A);
        drawBox(graphics, panelX, PANEL_TOP, panelX + panelW, panelBottom, THEME_RED, 0x00000000);

        graphics.enableScissor(panelX + 1, PANEL_TOP + 1, panelX + panelW - 1, panelBottom - 1);
        int y = PANEL_TOP + 4 - scrollOffset;
        for (SectionWidget section : sections) {
            y = section.render(graphics, font, panelX + 6, y, panelW - 12, mouseX, mouseY, delta);
            y += 4;
        }
        graphics.disableScissor();

        int totalHeight = getTotalContentHeight();
        if (totalHeight > panelBottom - PANEL_TOP) {
            float ratio = (float) (panelBottom - PANEL_TOP) / totalHeight;
            int barH = Math.max(10, (int) ((panelBottom - PANEL_TOP) * ratio));
            int barY = PANEL_TOP + (int) (((panelBottom - PANEL_TOP) - barH) * ((float) scrollOffset / (totalHeight - (panelBottom - PANEL_TOP))));
            graphics.fill(panelX + panelW - 3, barY, panelX + panelW - 1, barY + barH, THEME_RED);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int maxScroll = Math.max(0, getTotalContentHeight() - (this.height - PANEL_BOTTOM_MARGIN - PANEL_TOP));
        scrollOffset = (int) Math.max(0, Math.min(maxScroll, scrollOffset - scrollY * 20));
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int panelX = this.width / 2 - 155, panelW = 310, panelBottom = this.height - PANEL_BOTTOM_MARGIN;
        if (event.x() >= panelX && event.x() <= panelX + 310 && event.y() >= PANEL_TOP && event.y() <= panelBottom) {
            int y = PANEL_TOP + 4 - scrollOffset;
            for (SectionWidget section : sections) {
                y = section.mouseClicked(event.x(), event.y(), event.button(), panelX + 6, y, panelW - 12);
                y += 4;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        int panelX = this.width / 2 - 155, panelW = 310, panelBottom = this.height - PANEL_BOTTOM_MARGIN;
        if (event.x() >= panelX && event.x() <= panelX + 310 && event.y() >= PANEL_TOP && event.y() <= panelBottom) {
            int y = PANEL_TOP + 4 - scrollOffset;
            for (SectionWidget section : sections) {
                y = section.mouseDragged(event.x(), event.y(), event.button(), dragX, dragY, panelX + 6, y, panelW - 12);
                y += 4;
            }
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    private int getTotalContentHeight() {
        int h = 0;
        for (SectionWidget s : sections) h += s.getHeight() + 4;
        return h;
    }

    private void drawBox(GuiGraphics g, int x1, int y1, int x2, int y2, int outline, int fill) {
        int l = Math.min(x1, x2), r = Math.max(x1, x2), t = Math.min(y1, y2), b = Math.max(y1, y2);
        if (fill != 0x00000000) g.fill(l + 1, t + 1, r - 1, b - 1, fill);
        g.fill(l, t, r, t + 1, outline);
        g.fill(l, b - 1, r, b, outline);
        g.fill(l, t + 1, l + 1, b - 1, outline);
        g.fill(r - 1, t + 1, r, b - 1, outline);
    }

    @Override public boolean isPauseScreen() { return false; }
}