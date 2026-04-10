package com.swaphat.client.overlaymanager.gui;

import com.swaphat.client.overlaymanager.automation.AutomationManager;
import com.swaphat.client.overlaymanager.config.ConfigOption;
import com.swaphat.client.overlaymanager.config.ConfigRegistry;
import com.swaphat.client.overlaymanager.gui.widgets.SectionWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class AutomationConfigScreen extends Screen {

    private final Screen previousScreen;
    private static final int THEME_GREEN = 0xFF43A047;

    private int currentRuleIndex = 0;
    private EditBox searchBox;
    private String searchQuery = "";
    private int scrollOffset = 0;

    // ── Persistence & Stability ──────────────────────────────────────────────
    private String draggingId = null;   // Keeps the slider active across rebuilds
    private boolean needsRebuild = false; // Prevents ConcurrentModificationException

    private final List<SectionWidget> sections = new ArrayList<>();

    public AutomationConfigScreen(Screen previousScreen) {
        super(Component.literal("Automation Rules"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        if (AutomationManager.RULES.isEmpty()) {
            AutomationManager.RULES.add(new AutomationManager.Rule());
        }
        AutomationManager.Rule activeRule = AutomationManager.RULES.get(currentRuleIndex);

        int w = this.width;

        // ── Rule Navigation ──────────────────────────────────────────────────
        this.addRenderableWidget(Button.builder(Component.literal("<"), b -> switchRule(-1))
                .pos(w / 2 - 155, 30).size(20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Rule " + (currentRuleIndex + 1)), b -> {})
                .pos(w / 2 - 130, 30).size(85, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), b -> switchRule(1))
                .pos(w / 2 - 40, 30).size(20, 20).build());

        // ── Add New Rule ─────────────────────────────────────────────────────
        this.addRenderableWidget(Button.builder(Component.literal("+ New"), b -> {
            AutomationManager.RULES.add(new AutomationManager.Rule());
            currentRuleIndex = AutomationManager.RULES.size() - 1;
            rebuild();
        }).pos(w / 2 - 15, 30).size(65, 20).build());

        // ── Remove Current Rule ──────────────────────────────────────────────
        Button removeBtn = Button.builder(Component.literal("- Remove"), b -> {
            if (AutomationManager.RULES.size() > 1) {
                AutomationManager.RULES.remove(currentRuleIndex);
                currentRuleIndex = Math.max(0, currentRuleIndex - 1);
                rebuild();
            }
        }).pos(w / 2 + 55, 30).size(65, 20).build();

        removeBtn.active = AutomationManager.RULES.size() > 1;
        this.addRenderableWidget(removeBtn);

        // ── Trigger Selector ─────────────────────────────────────────────────
        this.addRenderableWidget(CycleButton.builder((AutomationManager.TriggerEvent val) -> Component.literal(val.displayName()), activeRule.trigger)
                .withValues(AutomationManager.TriggerEvent.values())
                .create(w / 2 - 155, 55, 310, 20, Component.literal("Trigger: "), (btn, val) -> activeRule.trigger = val));

        // ── Search Bar ───────────────────────────────────────────────────────
        searchBox = new EditBox(this.font, w / 2 - 155, 80, 310, 16, Component.literal("Search..."));
        searchBox.setResponder(q -> {
            searchQuery = q.toLowerCase();
            buildUnifiedList();
        });
        this.addRenderableWidget(searchBox);

        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.minecraft.setScreen(previousScreen))
                .pos(w / 2 - 40, this.height - 24).size(80, 16).build());

        buildUnifiedList();
    }

    private void switchRule(int dir) {
        currentRuleIndex = Math.clamp(currentRuleIndex + dir, 0, AutomationManager.RULES.size() - 1);
        rebuild();
    }

    private void rebuild() {
        this.minecraft.setScreen(this);
    }

    @SuppressWarnings("unchecked")
    private void buildUnifiedList() {
        needsRebuild = false; // Reset flag
        sections.clear();
        AutomationManager.Rule rule = AutomationManager.RULES.get(currentRuleIndex);
        Map<String, SectionWidget> categoryMap = new LinkedHashMap<>();

        for (ConfigOption opt : ConfigRegistry.ALL_OPTIONS) {
            if (!searchQuery.isEmpty() && !opt.name.toLowerCase().contains(searchQuery) && !opt.category.toLowerCase().contains(searchQuery)) {
                continue;
            }

            categoryMap.putIfAbsent(opt.category, new SectionWidget(opt.category, THEME_GREEN));
            SectionWidget widget = categoryMap.get(opt.category);

            boolean isOverridden = rule.overrides.containsKey(opt.id);
            Object displayValue = isOverridden ? rule.overrides.get(opt.id) : opt.get();

            Runnable clearOverride = () -> {
                rule.overrides.remove(opt.id);
                needsRebuild = true;
            };

            if (opt.isSlider) {
                widget.addSlider(opt.id, opt.name, (Float) displayValue, opt.min, opt.max, opt.color, isOverridden,
                        v -> {
                            rule.overrides.put(opt.id, v);
                            draggingId = opt.id;
                            needsRebuild = true; // REBUILD ON NEXT RENDER
                        }, clearOverride);

                // Re-apply dragging state to the newly created widget
                if (opt.id.equals(draggingId)) {
                    SectionWidget.SliderEntry lastEntry = (SectionWidget.SliderEntry) widget.entries.get(widget.entries.size() - 1);
                    lastEntry.dragging = true;
                }
            } else {
                widget.addBoolean(opt.name, (Boolean) displayValue, opt.color, isOverridden,
                        v -> {
                            rule.overrides.put(opt.id, v);
                            needsRebuild = true;
                        }, clearOverride);
            }
        }
        sections.addAll(categoryMap.values());
    }

    @Override
    public void render(@NonNull GuiGraphics g, int mx, int my, float delta) {
        // SAFE ZONE: Rebuild list before rendering if a slider moved
        if (needsRebuild) {
            buildUnifiedList();
        }

        this.renderBackground(g, mx, my, delta);
        g.drawCenteredString(this.font, "Automation Rules", this.width / 2, 8, THEME_GREEN);
        g.drawCenteredString(this.font, "Right-click setting to clear override.", this.width / 2, 18, 0xFFAAAAAA);
        super.render(g, mx, my, delta);

        int panelX = this.width / 2 - 155, panelW = 310, panelTop = 105, panelBottom = this.height - 30;

        g.fill(panelX, panelTop, panelX + panelW, panelBottom, 0xBB1A1A1A);
        g.enableScissor(panelX, panelTop, panelX + panelW, panelBottom);

        int y = panelTop + 4 - scrollOffset;
        for (SectionWidget section : sections) {
            y = section.render(g, font, panelX + 4, y, panelW - 8, mx, my, delta) + 4;
        }
        g.disableScissor();
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double sx, double sy) {
        scrollOffset = (int) Math.max(0, scrollOffset - sy * 20);
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int panelX = this.width / 2 - 155, panelTop = 105, panelBottom = this.height - 30;
        if (event.x() >= panelX && event.x() <= panelX + 310 && event.y() >= panelTop && event.y() <= panelBottom) {
            int y = panelTop + 4 - scrollOffset;
            for (SectionWidget s : sections) {
                y = s.mouseClicked(event.x(), event.y(), event.button(), panelX + 4, y, 302);
                y += 4;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent event) {
        draggingId = null; // Drag finished
        for (SectionWidget s : sections) {
            s.mouseReleased(event.x(), event.y(), event.button());
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        int panelX = this.width / 2 - 155, panelTop = 105, panelBottom = this.height - 30;
        if (event.x() >= panelX && event.x() <= panelX + 310 && event.y() >= panelTop && event.y() <= panelBottom) {
            int y = panelTop + 4 - scrollOffset;
            for (SectionWidget s : sections) {
                y = s.mouseDragged(event.x(), event.y(), event.button(), dx, dy, panelX + 4, y, 302);
                y += 4;
            }
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override public boolean isPauseScreen() { return false; }
}