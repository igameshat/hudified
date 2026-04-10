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

public class TweakManager extends Screen {

    private static final String CAT_OVERLAY = "OVERLAY";
    private static final String CAT_GUI     = "GUI";
    private static final String CAT_MISC    = "MISC";

    private String activeCategory = CAT_OVERLAY;
    private EditBox searchBox;
    private String  searchQuery = "";
    private int scrollOffset = 0;

    private static final int PANEL_TOP           = 65;
    private static final int PANEL_BOTTOM_MARGIN = 30;

    // Theme Colors
    private static final int THEME_BLUE   = 0xFF51A2CE;
    private static final int COL_OVERLAY  = 0xFFFFA726; // Orange
    private static final int COL_GUI      = 0xFF42A5F5; // Light Blue
    private static final int COL_GEN      = 0xFFFFFFFF; // White
    private static final int COL_DIM      = 0xFFAAAAAA; // Gray

    private final List<SectionWidget> sections = new ArrayList<>();
    private final Screen previousScreen;

    public TweakManager(Screen previousScreen) {
        super(Component.literal("OverlayManager Config"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        int w = this.width;

        searchBox = new EditBox(this.font, w / 2 - 120, 12, 240, 16, Component.literal("Search..."));
        searchBox.setHint(Component.literal("Search settings..."));
        searchBox.setResponder(query -> {
            searchQuery = query.toLowerCase();
            rebuildSections();
        });
        this.addRenderableWidget(searchBox);

        int tabY = 34, tabW = 60, tabGap = 4;
        int totalTabW = tabW * 3 + tabGap * 2;
        int tabStartX = w / 2 - totalTabW / 2;

        addCategoryButton(tabStartX,                        tabY, tabW, CAT_OVERLAY);
        addCategoryButton(tabStartX + tabW + tabGap,        tabY, tabW, CAT_GUI);
        addCategoryButton(tabStartX + (tabW + tabGap) * 2, tabY, tabW, CAT_MISC);

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
                case CAT_OVERLAY -> {
                    sections.add(buildPumpkinSection());
                    sections.add(buildFireSection());
                    sections.add(buildSpyglassSection());
                    sections.add(buildPortalSection());
                    sections.add(buildFreezeSection());
                    sections.add(buildBlindnessSection());
                    sections.add(buildDarknessSection());
                }
                case CAT_GUI -> {
                    sections.add(buildVignetteSection());
                    sections.add(buildBossBarSection());
                    sections.add(buildScoreboardSection());
                    sections.add(buildTotemSection());
                }
                case CAT_MISC -> {
                    sections.add(buildGlobalSection());
                }
            }
        } else {
            // Rebuild all for search
            sections.add(buildGlobalSection());
            sections.add(buildPumpkinSection());
            sections.add(buildFireSection());
            sections.add(buildSpyglassSection());
            sections.add(buildPortalSection());
            sections.add(buildFreezeSection());
            sections.add(buildBlindnessSection());
            sections.add(buildDarknessSection());
            sections.add(buildVignetteSection());
            sections.add(buildBossBarSection());
            sections.add(buildScoreboardSection());
            sections.add(buildTotemSection());
            sections.removeIf(s -> s.entries.isEmpty());
        }
    }


    private SectionWidget buildGlobalSection() {
        SectionWidget s = new SectionWidget("General", THEME_BLUE);
        s.addBoolean("Overlay Enabled", ConfigInstance.OverlayEnabled, COL_GEN, v -> ConfigInstance.OverlayEnabled = v);
        return filter(s);
    }

    private SectionWidget buildPumpkinSection() {
        SectionWidget s = new SectionWidget("Pumpkin Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.PumpkinOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.PumpkinOverlay.enabled = v);
        s.addSlider("Opacity", ConfigInstance.PumpkinOverlay.opacity, 0, 255, COL_OVERLAY, v -> ConfigInstance.PumpkinOverlay.opacity = v);
        return filter(s);
    }

    private SectionWidget buildFireSection() {
        SectionWidget s = new SectionWidget("Fire Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.FireOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.FireOverlay.enabled = v);
        s.addSlider("Offset Pixels", ConfigInstance.FireOverlay.offsetPixels, 0, 500, COL_OVERLAY, v -> ConfigInstance.FireOverlay.offsetPixels = v);
        s.addSlider("Opacity", ConfigInstance.FireOverlay.opacity, 0, 255, COL_OVERLAY, v -> ConfigInstance.FireOverlay.opacity = v);
        return filter(s);
    }

    private SectionWidget buildSpyglassSection() {
        SectionWidget s = new SectionWidget("Spyglass Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.SpyglassOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.SpyglassOverlay.enabled = v);
        s.addSlider("Scale", ConfigInstance.SpyglassOverlay.scale, 0, 2, COL_OVERLAY, v -> ConfigInstance.SpyglassOverlay.scale = v);
        return filter(s);
    }

    private SectionWidget buildPortalSection() {
        SectionWidget s = new SectionWidget("Portal Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.PortalOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.PortalOverlay.enabled = v);
        s.addSlider("Opacity", ConfigInstance.PortalOverlay.opacity, 0, 255, COL_OVERLAY, v -> ConfigInstance.PortalOverlay.opacity = v);
        s.addSlider("Speed", ConfigInstance.PortalOverlay.speed, 0, 5, COL_OVERLAY, v -> ConfigInstance.PortalOverlay.speed = v);
        s.addBoolean("Allow GUIs", ConfigInstance.PortalOverlay.allowGuisInPortal, COL_OVERLAY, v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v);
        return filter(s);
    }

    private SectionWidget buildFreezeSection() {
        SectionWidget s = new SectionWidget("Freeze Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.FreezeOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.FreezeOverlay.enabled = v);
        s.addSlider("Opacity", (float)ConfigInstance.FreezeOverlay.opacity, 0, 255, COL_OVERLAY, v -> ConfigInstance.FreezeOverlay.opacity = Math.round(v));
        return filter(s);
    }

    private SectionWidget buildBlindnessSection() {
        SectionWidget s = new SectionWidget("Blindness Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.BlindnessOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.BlindnessOverlay.enabled = v);
        s.addBoolean("Slowdown", ConfigInstance.BlindnessOverlay.enableSlowdown, COL_OVERLAY, v -> ConfigInstance.BlindnessOverlay.enableSlowdown = v);
        return filter(s);
    }

    private SectionWidget buildDarknessSection() {
        SectionWidget s = new SectionWidget("Darkness Overlay", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.DarknessOverlay.enabled, COL_OVERLAY, v -> ConfigInstance.DarknessOverlay.enabled = v);
        return filter(s);
    }

    private SectionWidget buildVignetteSection() {
        SectionWidget s = new SectionWidget("Vignette", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.Vignette.enabled, COL_GUI, v -> ConfigInstance.Vignette.enabled = v);
        s.addSlider("Opacity", ConfigInstance.Vignette.opacity, 0, 255, COL_GUI, v -> ConfigInstance.Vignette.opacity = v);
        return filter(s);
    }

    private SectionWidget buildBossBarSection() {
        SectionWidget s = new SectionWidget("Boss Bar", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.BossBar.enabled, COL_GUI, v -> ConfigInstance.BossBar.enabled = v);
        s.addSlider("Y Offset", (float)ConfigInstance.BossBar.bossBarYOffset, 0, 100, COL_GUI, v -> ConfigInstance.BossBar.bossBarYOffset = Math.round(v));
        s.addSlider("Scale", ConfigInstance.BossBar.scale, 0, 2, COL_GUI, v -> ConfigInstance.BossBar.scale = v);
        return filter(s);
    }

    private SectionWidget buildScoreboardSection() {
        SectionWidget s = new SectionWidget("Scoreboard", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.Scoreboard.enabled, COL_GUI, v -> ConfigInstance.Scoreboard.enabled = v);
        return filter(s);
    }

    private SectionWidget buildTotemSection() {
        SectionWidget s = new SectionWidget("Totem", THEME_BLUE);
        s.addBoolean("Enabled", ConfigInstance.Totem.enabled, COL_GUI, v -> ConfigInstance.Totem.enabled = v);
        s.addBoolean("Animation", ConfigInstance.Totem.showTotemAnimation, COL_GUI, v -> ConfigInstance.Totem.showTotemAnimation = v);
        s.addBoolean("Particles", ConfigInstance.Totem.showParticles, COL_GUI, v -> ConfigInstance.Totem.showParticles = v);
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
        graphics.drawCenteredString(this.font, Component.literal("TweakManager Config"), this.width / 2, 2, 0xFFFFFFFF);
        super.render(graphics, mouseX, mouseY, delta);

        int tabW = 60, tabGap = 4, totalTabW = tabW * 3 + tabGap * 2, tabStartX = this.width / 2 - totalTabW / 2, tabY = 34;
        int idx = switch (activeCategory) { case CAT_OVERLAY -> 0; case CAT_GUI -> 1; case CAT_MISC -> 2; default -> 0; };
        graphics.fill(tabStartX + idx * (tabW + tabGap), tabY + 14, tabStartX + idx * (tabW + tabGap) + tabW, tabY + 15, THEME_BLUE);

        int panelX = this.width / 2 - 155, panelW = 310, panelBottom = this.height - PANEL_BOTTOM_MARGIN;
        graphics.fill(panelX, PANEL_TOP, panelX + panelW, panelBottom, 0xBB1A1A1A);
        drawBox(graphics, panelX, PANEL_TOP, panelX + panelW, panelBottom, THEME_BLUE, 0x00000000);

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
            graphics.fill(panelX + panelW - 3, barY, panelX + panelW - 1, barY + barH, THEME_BLUE);
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
                y = section.mouseClicked(event.x(), event.y(), event.button(), panelX + 4, y, panelW - 12);
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