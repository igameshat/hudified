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


        DropDownWidget global = new DropDownWidget(xPos, 0, widgetWidth, "Global Settings");
        global.addToggleButton("Overlay Enabled", () -> ConfigInstance.OverlayEnabled, v -> ConfigInstance.OverlayEnabled = v);
        global.addToggleButton("Draw Widget Backgrounds", () -> DropDownWidget.drawBackground, v -> DropDownWidget.drawBackground = v);
        rootWidgets.add(global);


        DropDownWidget pumpkin = new DropDownWidget(xPos, 0, widgetWidth, "Pumpkin Overlay");
        pumpkin.addToggleButton("Enabled", () -> ConfigInstance.PumpkinOverlay.enabled, v -> ConfigInstance.PumpkinOverlay.enabled = v);
        pumpkin.addSlider("Opacity", ConfigInstance.PumpkinOverlay.opacity / 255f, v -> ConfigInstance.PumpkinOverlay.opacity = (float)(v * 255));
        rootWidgets.add(pumpkin);

        DropDownWidget fire = new DropDownWidget(xPos, 0, widgetWidth, "Fire Overlay");
        fire.addToggleButton("Enabled", () -> ConfigInstance.FireOverlay.enabled, v -> ConfigInstance.FireOverlay.enabled = v);
        fire.addSlider("Opacity", ConfigInstance.FireOverlay.opacity / 255f, v -> ConfigInstance.FireOverlay.opacity = (float)(v * 255));
        fire.addSlider("Offset Pixels", ConfigInstance.FireOverlay.offsetPixels, v -> ConfigInstance.FireOverlay.offsetPixels = v.floatValue());
        rootWidgets.add(fire);

        DropDownWidget spyglass = new DropDownWidget(xPos, 0, widgetWidth, "Spyglass Overlay");
        spyglass.addToggleButton("Enabled", () -> ConfigInstance.SpyglassOverlay.enabled, v -> ConfigInstance.SpyglassOverlay.enabled = v);
        spyglass.addSlider("Scale", ConfigInstance.SpyglassOverlay.scale, v -> ConfigInstance.SpyglassOverlay.scale = v.floatValue());
        rootWidgets.add(spyglass);

        DropDownWidget portal = new DropDownWidget(xPos, 0, widgetWidth, "Portal Overlay");
        portal.addToggleButton("Enabled", () -> ConfigInstance.PortalOverlay.enabled, v -> ConfigInstance.PortalOverlay.enabled = v);
        portal.addSlider("Opacity", ConfigInstance.PortalOverlay.opacity / 255f, v -> ConfigInstance.PortalOverlay.opacity = (float)(v * 255));
        portal.addSlider("Speed", ConfigInstance.PortalOverlay.speed, v -> ConfigInstance.PortalOverlay.speed = v.floatValue());
        portal.addToggleButton("Allow GUIs in Portal", () -> ConfigInstance.PortalOverlay.allowGuisInPortal, v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v);
        portal.addToggleButton("Allow Camera Shake", () -> ConfigInstance.PortalOverlay.allowCameraShake, v -> ConfigInstance.PortalOverlay.allowCameraShake = v);
        rootWidgets.add(portal);

        DropDownWidget freeze = new DropDownWidget(xPos, 0, widgetWidth, "Freeze Overlay");
        freeze.addToggleButton("Enabled", () -> ConfigInstance.FreezeOverlay.enabled, v -> ConfigInstance.FreezeOverlay.enabled = v);
        freeze.addSlider("Opacity", ConfigInstance.FreezeOverlay.opacity / 255f, v -> ConfigInstance.FreezeOverlay.opacity = (int)(v * 255));
        freeze.addSlider("Scale", ConfigInstance.FreezeOverlay.scale, v -> ConfigInstance.FreezeOverlay.scale = v.floatValue());
        rootWidgets.add(freeze);

        DropDownWidget blindness = new DropDownWidget(xPos, 0, widgetWidth, "Blindness Overlay");
        blindness.addToggleButton("Enabled", () -> ConfigInstance.BlindnessOverlay.enabled, v -> ConfigInstance.BlindnessOverlay.enabled = v);
        rootWidgets.add(blindness);

        DropDownWidget darkness = new DropDownWidget(xPos, 0, widgetWidth, "Darkness Overlay");
        darkness.addToggleButton("Enabled", () -> ConfigInstance.DarknessOverlay.enabled, v -> ConfigInstance.DarknessOverlay.enabled = v);
        rootWidgets.add(darkness);

        DropDownWidget vignette = new DropDownWidget(xPos, 0, widgetWidth, "Vignette");
        vignette.addToggleButton("Enabled", () -> ConfigInstance.Vignette.enabled, v -> ConfigInstance.Vignette.enabled = v);
        vignette.addSlider("Opacity", ConfigInstance.Vignette.opacity / 255f, v -> ConfigInstance.Vignette.opacity = (float)(v * 255));
        rootWidgets.add(vignette);


        DropDownWidget bossBar = new DropDownWidget(xPos, 0, widgetWidth, "Boss Bar");
        bossBar.addToggleButton("Enabled", () -> ConfigInstance.BossBar.enabled, v -> ConfigInstance.BossBar.enabled = v);

        bossBar.addButton("Edit Position", b -> {
            if (this.minecraft != null) this.minecraft.setScreen(new LayoutEditorScreen(this, LayoutEditorScreen.EditMode.BOSS_BAR));
        });
        bossBar.addIntField("X Offset", ConfigInstance.BossBar.bossBarXOffset, v -> ConfigInstance.BossBar.bossBarXOffset = v);
        bossBar.addIntField("Y Offset", ConfigInstance.BossBar.bossBarYOffset, v -> ConfigInstance.BossBar.bossBarYOffset = v);
        bossBar.addSlider("Scale", ConfigInstance.BossBar.scale, v -> ConfigInstance.BossBar.scale = v.floatValue());
        rootWidgets.add(bossBar);

        DropDownWidget scoreboard = new DropDownWidget(xPos, 0, widgetWidth, "Scoreboard");
        scoreboard.addToggleButton("Enabled", () -> ConfigInstance.Scoreboard.enabled, v -> ConfigInstance.Scoreboard.enabled = v);
        rootWidgets.add(scoreboard);

        DropDownWidget attackInd = new DropDownWidget(xPos, 0, widgetWidth, "Attack Indicator");
        attackInd.addToggleButton("Enabled", () -> ConfigInstance.AttackIndicator.enabled, v -> ConfigInstance.AttackIndicator.enabled = v);
        attackInd.addIntField("Hotbar X Offset", ConfigInstance.AttackIndicator.hotbarXOffset, v -> ConfigInstance.AttackIndicator.hotbarXOffset = v);
        attackInd.addIntField("Hotbar Y Offset", ConfigInstance.AttackIndicator.hotbarYOffset, v -> ConfigInstance.AttackIndicator.hotbarYOffset = v);
        attackInd.addSlider("Scale", ConfigInstance.AttackIndicator.scale, v -> ConfigInstance.AttackIndicator.scale = v.floatValue());
        rootWidgets.add(attackInd);

        DropDownWidget pieChart = new DropDownWidget(xPos, 0, widgetWidth, "Pie Chart (F3)");
        pieChart.addToggleButton("Enabled", () -> ConfigInstance.PieChart.enabled, v -> ConfigInstance.PieChart.enabled = v);
        pieChart.addButton("Edit Position", b -> {
            if (this.minecraft != null) this.minecraft.setScreen(new LayoutEditorScreen(this, LayoutEditorScreen.EditMode.PIE_CHART));
        });
        pieChart.addIntField("X Pos", ConfigInstance.PieChart.x, v -> ConfigInstance.PieChart.x = v);
        pieChart.addIntField("Y Pos", ConfigInstance.PieChart.y, v -> ConfigInstance.PieChart.y = v);
        pieChart.addSlider("Scale", ConfigInstance.PieChart.scale, v -> ConfigInstance.PieChart.scale = v.floatValue());
        rootWidgets.add(pieChart);


        DropDownWidget totem = new DropDownWidget(xPos, 0, widgetWidth, "Totem Pop");
        totem.addToggleButton("Enabled", () -> ConfigInstance.Totem.enabled, v -> ConfigInstance.Totem.enabled = v);
        totem.addToggleButton("Show Animation", () -> ConfigInstance.Totem.showTotemAnimation, v -> ConfigInstance.Totem.showTotemAnimation = v);
        totem.addToggleButton("Show Particles", () -> ConfigInstance.Totem.showParticles, v -> ConfigInstance.Totem.showParticles = v);
        rootWidgets.add(totem);

        DropDownWidget arrowHigh = new DropDownWidget(xPos, 0, widgetWidth, "Arrow Highlight");
        arrowHigh.addToggleButton("Enabled", () -> ConfigInstance.ArrowHighlight.enabled, v -> ConfigInstance.ArrowHighlight.enabled = v);
        arrowHigh.addToggleButton("X-Ray Mode", () -> ConfigInstance.ArrowHighlight.xrayMode, v -> ConfigInstance.ArrowHighlight.xrayMode = v);
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
        env.addSlider("Fog Multiplier", ConfigInstance.Environment.fogMultiplier, v -> ConfigInstance.Environment.fogMultiplier = v.floatValue());
        env.addToggleButton("Clear Lava", () -> ConfigInstance.Environment.clearLava, v -> ConfigInstance.Environment.clearLava = v);
        env.addToggleButton("Clear Water", () -> ConfigInstance.Environment.clearWater, v -> ConfigInstance.Environment.clearWater = v);
        env.addSlider("Rain Opacity", ConfigInstance.Environment.rainOpacity, v -> ConfigInstance.Environment.rainOpacity = v.floatValue());
        env.addToggleButton("No Rain Particles", () -> ConfigInstance.Environment.noRainParticles, v -> ConfigInstance.Environment.noRainParticles = v);
        env.addToggleButton("No Snow", () -> ConfigInstance.Environment.noSnow, v -> ConfigInstance.Environment.noSnow = v);
        rootWidgets.add(env);


        DropDownWidget shieldCategory = new DropDownWidget(xPos, 0, widgetWidth, "Shield Settings");
        shieldCategory.addToggleButton("Shield Enabled", () -> ConfigInstance.Shields.enabled, v -> ConfigInstance.Shields.enabled = v);

        shieldCategory.addSubMenu(createHandConfigTree("First Person Main Hand", ConfigInstance.Shields.firstPersonMain, widgetWidth - 10));
        shieldCategory.addSubMenu(createHandConfigTree("First Person Off Hand", ConfigInstance.Shields.firstPersonOff, widgetWidth - 10));
        shieldCategory.addSubMenu(createHandConfigTree("Third Person Main Hand", ConfigInstance.Shields.thirdPersonMain, widgetWidth - 10));
        shieldCategory.addSubMenu(createHandConfigTree("Third Person Off Hand", ConfigInstance.Shields.thirdPersonOff, widgetWidth - 10));
        shieldCategory.addSubMenu(createHandConfigTree("Other Players Main Hand", ConfigInstance.Shields.otherPlayersMain, widgetWidth - 10));
        shieldCategory.addSubMenu(createHandConfigTree("Other Players Off Hand", ConfigInstance.Shields.otherPlayersOff, widgetWidth - 10));

        rootWidgets.add(shieldCategory);

        for (DropDownWidget w : rootWidgets) {
            this.addRenderableWidget(w);
        }

        this.addRenderableWidget(Button.builder(Component.literal("Save & Close"), b -> this.onClose())
                .bounds(this.width - 210, this.height - 25, 200, 20).build());

        repositionWidgets();
    }

    private DropDownWidget createHandConfigTree(String name, ConfigInstance.HandSettings settings, int currentWidth) {
        DropDownWidget rootNode = new DropDownWidget(0, 0, currentWidth, name);
        DropDownWidget handSettingNode = new DropDownWidget(0, 0, currentWidth - 10, "Hand Settings");

        DropDownWidget idleNode = new DropDownWidget(0, 0, currentWidth - 20, "Idle");
        idleNode.addIntField("X Offset", (int)settings.idle.xOffset, v -> settings.idle.xOffset = v);
        idleNode.addIntField("Y Offset", (int)settings.idle.yOffset, v -> settings.idle.yOffset = v);
        idleNode.addIntField("Z Offset", (int)settings.idle.zOffset, v -> settings.idle.zOffset = v);
        idleNode.addSlider("Scale X", settings.idle.scaleX, v -> settings.idle.scaleX = v.floatValue());
        idleNode.addSlider("Scale Y", settings.idle.scaleY, v -> settings.idle.scaleY = v.floatValue());
        idleNode.addSlider("Scale Z", settings.idle.scaleZ, v -> settings.idle.scaleZ = v.floatValue());
        idleNode.addIntField("Rotation X", (int)settings.idle.rotX, v -> settings.idle.rotX = v);
        idleNode.addIntField("Rotation Y", (int)settings.idle.rotY, v -> settings.idle.rotY = v);
        idleNode.addIntField("Rotation Z", (int)settings.idle.rotZ, v -> settings.idle.rotZ = v);


        DropDownWidget blockingNode = new DropDownWidget(0, 0, currentWidth - 20, "Blocking");
        blockingNode.addIntField("X Offset", (int)settings.blocking.xOffset, v -> settings.blocking.xOffset = v);
        blockingNode.addIntField("Y Offset", (int)settings.blocking.yOffset, v -> settings.blocking.yOffset = v);
        blockingNode.addIntField("Z Offset", (int)settings.blocking.zOffset, v -> settings.blocking.zOffset = v);
        blockingNode.addSlider("Scale X", settings.blocking.scaleX, v -> settings.blocking.scaleX = v.floatValue());
        blockingNode.addSlider("Scale Y", settings.blocking.scaleY, v -> settings.blocking.scaleY = v.floatValue());
        blockingNode.addSlider("Scale Z", settings.blocking.scaleZ, v -> settings.blocking.scaleZ = v.floatValue());
        blockingNode.addIntField("Rotation X", (int)settings.blocking.rotX, v -> settings.blocking.rotX = v);
        blockingNode.addIntField("Rotation Y", (int)settings.blocking.rotY, v -> settings.blocking.rotY = v);
        blockingNode.addIntField("Rotation Z", (int)settings.blocking.rotZ, v -> settings.blocking.rotZ = v);

        handSettingNode.addSubMenu(idleNode);
        handSettingNode.addSubMenu(blockingNode);
        rootNode.addSubMenu(handSettingNode);

        return rootNode;
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
            GuiEventListener listener = listeners.get(i);
            if (listener.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
                return true;
            }
        }

        this.scrollAmount = Mth.clamp(this.scrollAmount - (scrollY * 20), 0, maxScroll);
        repositionWidgets();
        return true;
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        repositionWidgets();

        this.renderBackground(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.enableScissor(0, 30, this.width, this.height);
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.disableScissor();

        guiGraphics.fill(0, 0, this.width, 30, 0xAA000000);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }
}