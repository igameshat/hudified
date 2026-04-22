package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.gui.widgets.DropDownWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final List<DropDownWidget> rootWidgets = new ArrayList<>();

    private boolean hasBackedUpSettings = false;

    private double scrollAmount = 0;
    private int maxScroll = 0;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Overlay Manager Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.rootWidgets.clear();
        this.clearWidgets();

        int widgetWidth = 240;
        int xPos = 10;

        // --- GLOBAL SETTINGS ---
        DropDownWidget global = new DropDownWidget(xPos, 0, widgetWidth, "Global Settings");
        global.addToggleButton("Overlay Enabled", () -> ConfigInstance.OverlayEnabled, v -> ConfigInstance.OverlayEnabled = v);
        global.addToggleButton("Draw Widget Backgrounds", () -> DropDownWidget.drawBackground, v -> DropDownWidget.drawBackground = v);
        rootWidgets.add(global);

        // --- OVERLAYS ---
        DropDownWidget pumpkin = new DropDownWidget(xPos, 0, widgetWidth, "Pumpkin Overlay");
        pumpkin.addToggleButton("Enabled", () -> ConfigInstance.PumpkinOverlay.enabled, v -> ConfigInstance.PumpkinOverlay.enabled = v);
        pumpkin.addSlider("Opacity", ConfigInstance.PumpkinOverlay.opacity, 255, false, v -> ConfigInstance.PumpkinOverlay.opacity = v.floatValue());
        rootWidgets.add(pumpkin);

        DropDownWidget fire = new DropDownWidget(xPos, 0, widgetWidth, "Fire Overlay");
        fire.addToggleButton("Enabled", () -> ConfigInstance.FireOverlay.enabled, v -> ConfigInstance.FireOverlay.enabled = v);
        fire.addSlider("Opacity", ConfigInstance.FireOverlay.opacity, 255, false, v -> ConfigInstance.FireOverlay.opacity = v.floatValue());
        fire.addSlider("Offset Pixels", ConfigInstance.FireOverlay.offsetPixels, 500, false, v -> ConfigInstance.FireOverlay.offsetPixels = v.floatValue());
        rootWidgets.add(fire);

        DropDownWidget spyglass = new DropDownWidget(xPos, 0, widgetWidth, "Spyglass Overlay");
        spyglass.addToggleButton("Enabled", () -> ConfigInstance.SpyglassOverlay.enabled, v -> ConfigInstance.SpyglassOverlay.enabled = v);
        spyglass.addSlider("Scale", ConfigInstance.SpyglassOverlay.scale, 500, true, v -> ConfigInstance.SpyglassOverlay.scale = v.floatValue());
        rootWidgets.add(spyglass);

        DropDownWidget portal = new DropDownWidget(xPos, 0, widgetWidth, "Portal Overlay");
        portal.addToggleButton("Enabled", () -> ConfigInstance.PortalOverlay.enabled, v -> ConfigInstance.PortalOverlay.enabled = v);
        portal.addSlider("Opacity", ConfigInstance.PortalOverlay.opacity, 255, false, v -> ConfigInstance.PortalOverlay.opacity = v.floatValue());
        portal.addSlider("Speed", ConfigInstance.PortalOverlay.speed, 500, true, v -> ConfigInstance.PortalOverlay.speed = v.floatValue());
        portal.addToggleButton("Allow GUIs in Portal", () -> ConfigInstance.PortalOverlay.allowGuisInPortal, v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v);
        portal.addToggleButton("Allow Camera Shake", () -> ConfigInstance.PortalOverlay.allowCameraShake, v -> ConfigInstance.PortalOverlay.allowCameraShake = v);
        rootWidgets.add(portal);

        DropDownWidget freeze = new DropDownWidget(xPos, 0, widgetWidth, "Freeze Overlay");
        freeze.addToggleButton("Enabled", () -> ConfigInstance.FreezeOverlay.enabled, v -> ConfigInstance.FreezeOverlay.enabled = v);
        freeze.addSlider("Opacity", ConfigInstance.FreezeOverlay.opacity, 255, false, v -> ConfigInstance.FreezeOverlay.opacity = v.intValue());
        freeze.addSlider("XScale", ConfigInstance.FreezeOverlay.Xscale, 500, true, val -> ConfigInstance.FreezeOverlay.Xscale = val.floatValue());
        freeze.addSlider("YScale", ConfigInstance.FreezeOverlay.Yscale, 500, true, val -> ConfigInstance.FreezeOverlay.Yscale = val.floatValue());        rootWidgets.add(freeze);

        DropDownWidget blindness = new DropDownWidget(xPos, 0, widgetWidth, "Blindness Overlay");
        blindness.addToggleButton("Enabled", () -> ConfigInstance.BlindnessOverlay.enabled, v -> ConfigInstance.BlindnessOverlay.enabled = v);
        rootWidgets.add(blindness);

        DropDownWidget darkness = new DropDownWidget(xPos, 0, widgetWidth, "Darkness Overlay");
        darkness.addToggleButton("Enabled", () -> ConfigInstance.DarknessOverlay.enabled, v -> ConfigInstance.DarknessOverlay.enabled = v);
        rootWidgets.add(darkness);

        DropDownWidget vignette = new DropDownWidget(xPos, 0, widgetWidth, "Vignette");
        vignette.addToggleButton("Enabled", () -> ConfigInstance.Vignette.enabled, v -> ConfigInstance.Vignette.enabled = v);
        vignette.addSlider("Opacity", ConfigInstance.Vignette.opacity, 255, false, v -> ConfigInstance.Vignette.opacity = v.floatValue());
        rootWidgets.add(vignette);

        // --- HUD ELEMENTS ---
        DropDownWidget bossBar = new DropDownWidget(xPos, 0, widgetWidth, "Boss Bar");
        bossBar.addToggleButton("Enabled", () -> ConfigInstance.BossBar.enabled, v -> ConfigInstance.BossBar.enabled = v);
        bossBar.addButton("Edit Position", b -> this.minecraft.setScreen(new LayoutEditorScreen(this, LayoutEditorScreen.EditMode.BOSS_BAR)));
        bossBar.addIntField("X Offset", ConfigInstance.BossBar.bossBarXOffset, v -> ConfigInstance.BossBar.bossBarXOffset = v);
        bossBar.addIntField("Y Offset", ConfigInstance.BossBar.bossBarYOffset, v -> ConfigInstance.BossBar.bossBarYOffset = v);
        bossBar.addSlider("Scale", ConfigInstance.BossBar.scale, 500, true, v -> ConfigInstance.BossBar.scale = v.floatValue());
        rootWidgets.add(bossBar);

        DropDownWidget scoreboard = new DropDownWidget(xPos, 0, widgetWidth, "Scoreboard");
        scoreboard.addToggleButton("Enabled", () -> ConfigInstance.Scoreboard.enabled, v -> ConfigInstance.Scoreboard.enabled = v);
        rootWidgets.add(scoreboard);

        DropDownWidget attackInd = new DropDownWidget(xPos, 0, widgetWidth, "Attack Indicator");
        attackInd.addToggleButton("Enabled", () -> ConfigInstance.AttackIndicator.enabled, v -> ConfigInstance.AttackIndicator.enabled = v);
        attackInd.addIntField("Hotbar X Offset", ConfigInstance.AttackIndicator.hotbarXOffset, v -> ConfigInstance.AttackIndicator.hotbarXOffset = v);
        attackInd.addIntField("Hotbar Y Offset", ConfigInstance.AttackIndicator.hotbarYOffset, v -> ConfigInstance.AttackIndicator.hotbarYOffset = v);
        attackInd.addSlider("Scale", ConfigInstance.AttackIndicator.scale, 500, true, v -> ConfigInstance.AttackIndicator.scale = v.floatValue());
        rootWidgets.add(attackInd);

        DropDownWidget pieChart = new DropDownWidget(xPos, 0, widgetWidth, "Pie Chart (F3)");
        pieChart.addToggleButton("Enabled", () -> ConfigInstance.PieChart.enabled, v -> ConfigInstance.PieChart.enabled = v);
        pieChart.addButton("Edit Position", b -> this.minecraft.setScreen(new LayoutEditorScreen(this, LayoutEditorScreen.EditMode.PIE_CHART)));
        pieChart.addIntField("X Pos", ConfigInstance.PieChart.x, v -> ConfigInstance.PieChart.x = v);
        pieChart.addIntField("Y Pos", ConfigInstance.PieChart.y, v -> ConfigInstance.PieChart.y = v);
        pieChart.addSlider("Scale", ConfigInstance.PieChart.scale, 500, true, v -> ConfigInstance.PieChart.scale = v.floatValue());
        rootWidgets.add(pieChart);

        // --- MISC ---
        DropDownWidget totem = new DropDownWidget(xPos, 0, widgetWidth, "Totem Pop");
        totem.addToggleButton("Enabled", () -> ConfigInstance.Totem.enabled, v -> ConfigInstance.Totem.enabled = v);
        totem.addToggleButton("Show Animation", () -> ConfigInstance.Totem.showTotemAnimation, v -> ConfigInstance.Totem.showTotemAnimation = v);
        totem.addToggleButton("Show Particles", () -> ConfigInstance.Totem.showParticles, v -> ConfigInstance.Totem.showParticles = v);
        rootWidgets.add(totem);

        DropDownWidget arrowHigh = new DropDownWidget(xPos, 0, widgetWidth, "Arrow Highlight");
        arrowHigh.addToggleButton("Enabled", () -> ConfigInstance.ArrowHighlight.enabled, v -> ConfigInstance.ArrowHighlight.enabled = v);
        arrowHigh.addIntField("Red", ConfigInstance.ArrowHighlight.red, v -> ConfigInstance.ArrowHighlight.red = v);
        arrowHigh.addIntField("Green", ConfigInstance.ArrowHighlight.green, v -> ConfigInstance.ArrowHighlight.green = v);
        arrowHigh.addIntField("Blue", ConfigInstance.ArrowHighlight.blue, v -> ConfigInstance.ArrowHighlight.blue = v);
        rootWidgets.add(arrowHigh);

        DropDownWidget boat = new DropDownWidget(xPos, 0, widgetWidth, "Boat POV");
        boat.addToggleButton("Enabled", () -> ConfigInstance.Boat.enabled, v -> ConfigInstance.Boat.enabled = v);
        boat.addToggleButton("Unlock Boat POV", () -> ConfigInstance.Boat.unlockBoatPov, v -> ConfigInstance.Boat.unlockBoatPov = v);
        rootWidgets.add(boat);

        DropDownWidget env = new DropDownWidget(xPos, 0, widgetWidth, "Environment");
        env.addToggleButton("Fullbright", () -> ConfigInstance.Environment.fullbright, v -> ConfigInstance.Environment.fullbright = v);
        env.addToggleButton("Disable Fog", () -> ConfigInstance.Environment.disableFog, v -> ConfigInstance.Environment.disableFog = v);
        env.addSlider("Fog Multiplier", ConfigInstance.Environment.fogMultiplier, 500, true, v -> ConfigInstance.Environment.fogMultiplier = v.floatValue());
        env.addToggleButton("Clear Lava", () -> ConfigInstance.Environment.clearLava, v -> ConfigInstance.Environment.clearLava = v);
        env.addToggleButton("Clear Water", () -> ConfigInstance.Environment.clearWater, v -> ConfigInstance.Environment.clearWater = v);
        env.addSlider("Rain Opacity", ConfigInstance.Environment.rainOpacity, 100, true, v -> ConfigInstance.Environment.rainOpacity = v.floatValue());
        env.addToggleButton("No Rain Particles", () -> ConfigInstance.Environment.noRainParticles, v -> ConfigInstance.Environment.noRainParticles = v);
        env.addToggleButton("No Snow", () -> ConfigInstance.Environment.noSnow, v -> ConfigInstance.Environment.noSnow = v);
        rootWidgets.add(env);

        // --- SHIELD CONFIG ---
        buildShieldCategory(xPos, widgetWidth);

        for (DropDownWidget w : rootWidgets) {
            this.addRenderableWidget(w);
        }

        this.addRenderableWidget(Button.builder(Component.literal("Save & Close"), b -> this.onClose())
                .bounds(this.width - 210, this.height - 25, 200, 20).build());

        repositionWidgets();
    }

    private void buildShieldCategory(int xPos, int widgetWidth) {
        DropDownWidget shieldCategory = new DropDownWidget(xPos, 0, widgetWidth, "Shield Settings");
        shieldCategory.addToggleButton("Shield Enabled", () -> ConfigInstance.Shields.enabled, v -> ConfigInstance.Shields.enabled = v);

        // Advanced Mode Toggle
        shieldCategory.addToggleButton("Advanced Options",
                () -> ConfigInstance.Shields.advancedOptions,
                v -> {
                    ConfigInstance.Shields.advancedOptions = v;
                    this.init(); // Refresh layout to show/hide submenus
                });

        if (ConfigInstance.Shields.advancedOptions) {
            // --- ADVANCED MODE ---
            shieldCategory.addSubMenu(createHandConfigTree("First Person Main Hand", ConfigInstance.Shields.firstPersonMain, widgetWidth - 10));
            shieldCategory.addSubMenu(createHandConfigTree("First Person Off Hand", ConfigInstance.Shields.firstPersonOff, widgetWidth - 10));
            shieldCategory.addSubMenu(createHandConfigTree("Third Person Main Hand", ConfigInstance.Shields.thirdPersonMain, widgetWidth - 10));
            shieldCategory.addSubMenu(createHandConfigTree("Third Person Off Hand", ConfigInstance.Shields.thirdPersonOff, widgetWidth - 10));
            shieldCategory.addSubMenu(createHandConfigTree("Other Players Main Hand", ConfigInstance.Shields.otherPlayersMain, widgetWidth - 10));
            shieldCategory.addSubMenu(createHandConfigTree("Other Players Off Hand", ConfigInstance.Shields.otherPlayersOff, widgetWidth - 10));
        } else {
            // --- SIMPLE MODE ---
            shieldCategory.addToggleButton("Side Shield Presets",
                    () -> ConfigInstance.ShieldConfig.MrOrdenadorPresets,
                    v -> {
                        ConfigInstance.ShieldConfig.MrOrdenadorPresets = v;
                        if (v) applyMrOrdenadorPresets();
                        else restoreUserPresets();
                        this.init();
                    });

            // Single Y-Offset slider for simple mode
            int currentDisplayY = (int)ConfigInstance.Shields.firstPersonMain.idle.yOffset;
            shieldCategory.addIntField("Shield Height (Y Offset)", currentDisplayY,
                    v -> {
                        if (ConfigInstance.ShieldConfig.MrOrdenadorPresets) {
                            // Apply offset relative to the mathematically calculated preset bases
                            ConfigInstance.Shields.firstPersonMain.idle.yOffset = v; // Base: 0
                            ConfigInstance.Shields.firstPersonMain.blocking.yOffset = -6 + v;
                            ConfigInstance.Shields.firstPersonOff.idle.yOffset = -125 + v;
                            ConfigInstance.Shields.firstPersonOff.blocking.yOffset = 6 + v;
                        } else {
                            // Standard behavior: sync all first person hands to this exact value
                            ConfigInstance.Shields.firstPersonMain.idle.yOffset = v;
                            ConfigInstance.Shields.firstPersonOff.idle.yOffset = v;
                            ConfigInstance.Shields.firstPersonMain.blocking.yOffset = v;
                            ConfigInstance.Shields.firstPersonOff.blocking.yOffset = v;
                        }
                    });
        }
        rootWidgets.add(shieldCategory);
    }

    private void applyMrOrdenadorPresets() {
        // Save the user's custom layout before overwriting, if we haven't already
        if (!hasBackedUpSettings) {
            saveUserPresets();
            hasBackedUpSettings = true;
        }

        // --- FIRST PERSON MAIN HAND (Right Hand) ---
        // Idle
        ConfigInstance.Shields.firstPersonMain.idle.xOffset = 27.0;
        ConfigInstance.Shields.firstPersonMain.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonMain.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleX = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleY = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleZ = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotX = 5.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotY = 90.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotZ = -5.0f;

        // Blocking
        ConfigInstance.Shields.firstPersonMain.blocking.xOffset = -19.0;
        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = -31.0;
        ConfigInstance.Shields.firstPersonMain.blocking.zOffset = 0.0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleX = 0.7750105f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleY = 0.7752809f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleZ = 1.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotX = -5.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotY = -3.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotZ = 0.0f;

        // --- FIRST PERSON OFF HAND (Left Hand) ---
        // Idle
        ConfigInstance.Shields.firstPersonOff.idle.xOffset = -8.0;
        ConfigInstance.Shields.firstPersonOff.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonOff.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleX = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleY = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleZ = 0.80898875f;
        ConfigInstance.Shields.firstPersonOff.idle.rotX = 0.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotY = 90.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotZ = -5.0f;

        // Blocking
        ConfigInstance.Shields.firstPersonOff.blocking.xOffset = -5.0;
        ConfigInstance.Shields.firstPersonOff.blocking.yOffset = -32.0;
        ConfigInstance.Shields.firstPersonOff.blocking.zOffset = 0.0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleX = 0.8876405f;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleY = 1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleZ = 1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotX = -5.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotY = -1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotZ = 3.0f;
    }

    private void saveUserPresets() {
        copyHandSettings(ConfigInstance.Shields.firstPersonMain, ConfigInstance.Shields.backupFirstPersonMain);
        copyHandSettings(ConfigInstance.Shields.firstPersonOff, ConfigInstance.Shields.backupFirstPersonOff);
    }

    private void restoreUserPresets() {
        if (hasBackedUpSettings) {
            copyHandSettings(ConfigInstance.Shields.backupFirstPersonMain, ConfigInstance.Shields.firstPersonMain);
            copyHandSettings(ConfigInstance.Shields.backupFirstPersonOff, ConfigInstance.Shields.firstPersonOff);
            hasBackedUpSettings = false;
        } else {
            applyVanillaDefaults();
        }
    }

    private void applyVanillaDefaults() {
        resetHandSettings(ConfigInstance.Shields.firstPersonMain);
        resetHandSettings(ConfigInstance.Shields.firstPersonOff);
        resetHandSettings(ConfigInstance.Shields.thirdPersonMain);
        resetHandSettings(ConfigInstance.Shields.thirdPersonOff);
        resetHandSettings(ConfigInstance.Shields.otherPlayersMain);
        resetHandSettings(ConfigInstance.Shields.otherPlayersOff);
    }

    private void resetHandSettings(ConfigInstance.HandSettings settings) {
        settings.idle.rotX = 0; settings.idle.rotY = 0; settings.idle.rotZ = 0;
        settings.idle.xOffset = 0; settings.idle.yOffset = 0; settings.idle.zOffset = 0;
        settings.idle.scaleX = 1.0f; settings.idle.scaleY = 1.0f; settings.idle.scaleZ = 1.0f;
        settings.blocking.rotX = 0; settings.blocking.rotY = 0; settings.blocking.rotZ = 0;
        settings.blocking.xOffset = 0; settings.blocking.yOffset = 0; settings.blocking.zOffset = 0;
        settings.blocking.scaleX = 1.0f; settings.blocking.scaleY = 1.0f; settings.blocking.scaleZ = 1.0f;
    }

    private void copyHandSettings(ConfigInstance.HandSettings src, ConfigInstance.HandSettings dest) {
        dest.idle.rotX = src.idle.rotX; dest.idle.rotY = src.idle.rotY; dest.idle.rotZ = src.idle.rotZ;
        dest.idle.xOffset = src.idle.xOffset; dest.idle.yOffset = src.idle.yOffset; dest.idle.zOffset = src.idle.zOffset;
        dest.idle.scaleX = src.idle.scaleX; dest.idle.scaleY = src.idle.scaleY; dest.idle.scaleZ = src.idle.scaleZ;
        dest.blocking.rotX = src.blocking.rotX; dest.blocking.rotY = src.blocking.rotY; dest.blocking.rotZ = src.blocking.rotZ;
        dest.blocking.xOffset = src.blocking.xOffset; dest.blocking.yOffset = src.blocking.yOffset; dest.blocking.zOffset = src.blocking.zOffset;
        dest.blocking.scaleX = src.blocking.scaleX; dest.blocking.scaleY = src.blocking.scaleY; dest.blocking.scaleZ = src.blocking.scaleZ;
    }

    private DropDownWidget createHandConfigTree(String name, ConfigInstance.HandSettings settings, int currentWidth) {
        DropDownWidget rootNode = new DropDownWidget(0, 0, currentWidth, name);
        DropDownWidget handSettingNode = new DropDownWidget(0, 0, currentWidth - 10, "Hand Settings");

        DropDownWidget idleNode = getDropDownWidget(currentWidth, "Idle", settings.idle);
        DropDownWidget blockingNode = getDropDownWidget(currentWidth, "Blocking", settings.blocking);

        handSettingNode.addSubMenu(idleNode);
        handSettingNode.addSubMenu(blockingNode);
        rootNode.addSubMenu(handSettingNode);
        return rootNode;
    }

    private static @NonNull DropDownWidget getDropDownWidget(int currentWidth, String Idle, ConfigInstance.ShieldSettings settings) {
        DropDownWidget idleNode = new DropDownWidget(0, 0, currentWidth - 20, Idle);
        idleNode.addIntField("X Offset", (int) settings.xOffset, v -> settings.xOffset = v);
        idleNode.addIntField("Y Offset", (int) settings.yOffset, v -> settings.yOffset = v);
        idleNode.addIntField("Z Offset", (int) settings.zOffset, v -> settings.zOffset = v);
        idleNode.addSlider("Scale X", settings.scaleX, 500, true, v -> settings.scaleX = v.floatValue());
        idleNode.addSlider("Scale Y", settings.scaleY, 500, true, v -> settings.scaleY = v.floatValue());
        idleNode.addSlider("Scale Z", settings.scaleZ, 500, true, v -> settings.scaleZ = v.floatValue());
        idleNode.addIntField("Rotation X", (int) settings.rotX, v -> settings.rotX = v);
        idleNode.addIntField("Rotation Y", (int) settings.rotY, v -> settings.rotY = v);
        idleNode.addIntField("Rotation Z", (int) settings.rotZ, v -> settings.rotZ = v);
        return idleNode;
    }

    private void repositionWidgets() {
        int currentY = (int) (40 - scrollAmount);
        for (DropDownWidget w : rootWidgets) {
            w.setY(currentY);
            currentY += w.getExpandedHeight() + 4;
        }
        this.maxScroll = Math.max(0, currentY + (int)scrollAmount - (this.height - 40));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        List<? extends GuiEventListener> listeners = this.children();
        for (int i = listeners.size() - 1; i >= 0; i--) {
            if (listeners.get(i).mouseScrolled(mouseX, mouseY, scrollX, scrollY)) return true;
        }
        this.scrollAmount = Mth.clamp(this.scrollAmount - (scrollY * 20), 0, maxScroll);
        repositionWidgets();
        return true;
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        repositionWidgets();
        guiGraphics.enableScissor(0, 30, this.width, this.height);
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.disableScissor();
        guiGraphics.fill(0, 0, this.width, 30, 0xAA000000);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreen(this.parent);
    }
}