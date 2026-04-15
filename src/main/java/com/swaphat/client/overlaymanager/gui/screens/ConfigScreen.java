package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.gui.widgets.DropDownWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Overlay Manager Config"));
        this.parent = parent;
    }

    private void addHandConfig(DropDownWidget widget, String prefix, ConfigInstance.HandSettings settings) {
        // Idle Settings
        widget.addIntField(prefix + " Idl X", (int)settings.idle.xOffset, v -> settings.idle.xOffset = v);
        widget.addIntField(prefix + " Idl Y", (int)settings.idle.yOffset, v -> settings.idle.yOffset = v);
        widget.addIntField(prefix + " Idl Z", (int)settings.idle.zOffset, v -> settings.idle.zOffset = v);
        widget.addSlider(prefix + " Idl Scl", settings.idle.scale, v -> settings.idle.scale = v.floatValue());
        widget.addIntField(prefix + " Idl RotX", (int)settings.idle.rotX, v -> settings.idle.rotX = v);
        widget.addIntField(prefix + " Idl RotY", (int)settings.idle.rotY, v -> settings.idle.rotY = v);
        widget.addIntField(prefix + " Idl RotZ", (int)settings.idle.rotZ, v -> settings.idle.rotZ = v);

        // Blocking Settings
        widget.addIntField(prefix + " Blk X", (int)settings.blocking.xOffset, v -> settings.blocking.xOffset = v);
        widget.addIntField(prefix + " Blk Y", (int)settings.blocking.yOffset, v -> settings.blocking.yOffset = v);
        widget.addIntField(prefix + " Blk Z", (int)settings.blocking.zOffset, v -> settings.blocking.zOffset = v);
        widget.addSlider(prefix + " Blk Scl", settings.blocking.scale, v -> settings.blocking.scale = v.floatValue());
        widget.addIntField(prefix + " Blk RotX", (int)settings.blocking.rotX, v -> settings.blocking.rotX = v);
        widget.addIntField(prefix + " Blk RotY", (int)settings.blocking.rotY, v -> settings.blocking.rotY = v);
        widget.addIntField(prefix + " Blk RotZ", (int)settings.blocking.rotZ, v -> settings.blocking.rotZ = v);
    }

    @Override
    protected void init() {
        this.clearWidgets();

        List<DropDownWidget> widgets = new ArrayList<>();
        int widgetWidth = 95;

        // ==========================================
        // ROW 0: Largest Menus
        // ==========================================

        // Split Category 1: First Person Settings
        DropDownWidget shield1st = new DropDownWidget(0, 0, widgetWidth, "Shield 1st");
        shield1st.addToggleButton("Enabled", () -> ConfigInstance.Shields.enabled, v -> ConfigInstance.Shields.enabled = v);
        this.addHandConfig(shield1st, "M", ConfigInstance.Shields.firstPersonMain);
        this.addHandConfig(shield1st, "O", ConfigInstance.Shields.firstPersonOff);
        widgets.add(shield1st);

        // Split Category 2: Third Person Settings (Self F5 & Others)
        DropDownWidget shield3rd = new DropDownWidget(0, 0, widgetWidth, "Shield 3rd");
        this.addHandConfig(shield3rd, "Self M", ConfigInstance.Shields.thirdPersonMain);
        this.addHandConfig(shield3rd, "Self O", ConfigInstance.Shields.thirdPersonOff);
        this.addHandConfig(shield3rd, "Oth M", ConfigInstance.Shields.otherPlayersMain);
        this.addHandConfig(shield3rd, "Oth O", ConfigInstance.Shields.otherPlayersOff);
        widgets.add(shield3rd);

        DropDownWidget env = new DropDownWidget(0, 0, widgetWidth, "Environment");
        env.addToggleButton("Fullbright", () -> ConfigInstance.Environment.fullbright, v -> ConfigInstance.Environment.fullbright = v);
        env.addToggleButton("Disable Fog", () -> ConfigInstance.Environment.disableFog, v -> ConfigInstance.Environment.disableFog = v);
        env.addSlider("Fog Multiplier", ConfigInstance.Environment.fogMultiplier, v -> ConfigInstance.Environment.fogMultiplier = v.floatValue());
        env.addToggleButton("Clear Lava", () -> ConfigInstance.Environment.clearLava, v -> ConfigInstance.Environment.clearLava = v);
        env.addToggleButton("Clear Water", () -> ConfigInstance.Environment.clearWater, v -> ConfigInstance.Environment.clearWater = v);
        env.addSlider("Rain Opacity", ConfigInstance.Environment.rainOpacity, v -> ConfigInstance.Environment.rainOpacity = v.floatValue());
        env.addToggleButton("No Rain Parts", () -> ConfigInstance.Environment.noRainParticles, v -> ConfigInstance.Environment.noRainParticles = v);
        env.addToggleButton("No Snow", () -> ConfigInstance.Environment.noSnow, v -> ConfigInstance.Environment.noSnow = v);
        widgets.add(env);

        DropDownWidget portal = new DropDownWidget(0, 0, widgetWidth, "Portal");
        portal.addToggleButton("Enabled", () -> ConfigInstance.PortalOverlay.enabled, v -> ConfigInstance.PortalOverlay.enabled = v);
        portal.addSlider("Opacity", ConfigInstance.PortalOverlay.opacity / 255f, v -> ConfigInstance.PortalOverlay.opacity = (float)(v * 255));
        portal.addSlider("Speed", ConfigInstance.PortalOverlay.speed, v -> ConfigInstance.PortalOverlay.speed = v.floatValue());
        portal.addToggleButton("Allow GUIs", () -> ConfigInstance.PortalOverlay.allowGuisInPortal, v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v);
        portal.addToggleButton("Camera Shake", () -> ConfigInstance.PortalOverlay.allowCameraShake, v -> ConfigInstance.PortalOverlay.allowCameraShake = v);
        widgets.add(portal);

        // ==========================================
        // ROW 1
        // ==========================================

        DropDownWidget arrow = new DropDownWidget(0, 0, widgetWidth, "Arrows");
        arrow.addToggleButton("Enabled", () -> ConfigInstance.ArrowHighlight.enabled, v -> ConfigInstance.ArrowHighlight.enabled = v);
        arrow.addToggleButton("X-Ray Mode", () -> ConfigInstance.ArrowHighlight.xrayMode, v -> ConfigInstance.ArrowHighlight.xrayMode = v);
        arrow.addSlider("Red", ConfigInstance.ArrowHighlight.red / 255f, v -> ConfigInstance.ArrowHighlight.red = (int)(v * 255));
        arrow.addSlider("Green", ConfigInstance.ArrowHighlight.green / 255f, v -> ConfigInstance.ArrowHighlight.green = (int)(v * 255));
        arrow.addSlider("Blue", ConfigInstance.ArrowHighlight.blue / 255f, v -> ConfigInstance.ArrowHighlight.blue = (int)(v * 255));
        widgets.add(arrow);

        DropDownWidget boss = new DropDownWidget(0, 0, widgetWidth, "BossBar");
        boss.addToggleButton("Enabled", () -> ConfigInstance.BossBar.enabled, v -> ConfigInstance.BossBar.enabled = v);
        boss.addIntField("X Offset", ConfigInstance.BossBar.bossBarXOffset, v -> ConfigInstance.BossBar.bossBarXOffset = v);
        boss.addIntField("Y Offset", ConfigInstance.BossBar.bossBarYOffset, v -> ConfigInstance.BossBar.bossBarYOffset = v);
        boss.addSlider("Scale", ConfigInstance.BossBar.scale, v -> ConfigInstance.BossBar.scale = v.floatValue());
        widgets.add(boss);

        DropDownWidget fire = new DropDownWidget(0, 0, widgetWidth, "Fire");
        fire.addToggleButton("Enabled", () -> ConfigInstance.FireOverlay.enabled, v -> ConfigInstance.FireOverlay.enabled = v);
        fire.addIntField("Offset Px", (int)ConfigInstance.FireOverlay.offsetPixels, v -> ConfigInstance.FireOverlay.offsetPixels = v);
        fire.addSlider("Opacity", ConfigInstance.FireOverlay.opacity / 255f, v -> ConfigInstance.FireOverlay.opacity = (float)(v * 255));
        widgets.add(fire);

        DropDownWidget totem = new DropDownWidget(0, 0, widgetWidth, "Totem");
        totem.addToggleButton("Enabled", () -> ConfigInstance.Totem.enabled, v -> ConfigInstance.Totem.enabled = v);
        totem.addToggleButton("Animations", () -> ConfigInstance.Totem.showTotemAnimation, v -> ConfigInstance.Totem.showTotemAnimation = v);
        totem.addToggleButton("Particles", () -> ConfigInstance.Totem.showParticles, v -> ConfigInstance.Totem.showParticles = v);
        widgets.add(totem);

        // ==========================================
        // ROW 2
        // ==========================================

        DropDownWidget pie = new DropDownWidget(0, 0, widgetWidth, "PieChart");
        pie.addToggleButton("Enabled", () -> ConfigInstance.PieChart.enabled, v -> ConfigInstance.PieChart.enabled = v);
        pie.addSlider("Scale", ConfigInstance.PieChart.scale, v -> ConfigInstance.PieChart.scale = v.floatValue());
        pie.addButton("Edit Layout", b -> {
            if (this.minecraft != null) this.minecraft.setScreen(new LayoutEditorScreen(this));
        });
        widgets.add(pie);

        DropDownWidget pumpkin = new DropDownWidget(0, 0, widgetWidth, "Pumpkin");
        pumpkin.addToggleButton("Enabled", () -> ConfigInstance.PumpkinOverlay.enabled, v -> ConfigInstance.PumpkinOverlay.enabled = v);
        pumpkin.addSlider("Opacity", ConfigInstance.PumpkinOverlay.opacity / 255f, v -> ConfigInstance.PumpkinOverlay.opacity = (float)(v * 255));
        widgets.add(pumpkin);

        DropDownWidget freeze = new DropDownWidget(0, 0, widgetWidth, "Freeze");
        freeze.addToggleButton("Enabled", () -> ConfigInstance.FreezeOverlay.enabled, v -> ConfigInstance.FreezeOverlay.enabled = v);
        freeze.addSlider("Opacity", ConfigInstance.FreezeOverlay.opacity / 255f, v -> ConfigInstance.FreezeOverlay.opacity = (int)(v * 255));
        freeze.addSlider("Scale", ConfigInstance.FreezeOverlay.scale, v -> ConfigInstance.FreezeOverlay.scale = v.floatValue());
        widgets.add(freeze);

        DropDownWidget attack = new DropDownWidget(0, 0, widgetWidth, "Attack Ind.");
        attack.addToggleButton("Enabled", () -> ConfigInstance.AttackIndicator.enabled, v -> ConfigInstance.AttackIndicator.enabled = v);
        attack.addIntField("X Offset", ConfigInstance.AttackIndicator.hotbarXOffset, v -> ConfigInstance.AttackIndicator.hotbarXOffset = v);
        attack.addIntField("Y Offset", ConfigInstance.AttackIndicator.hotbarYOffset, v -> ConfigInstance.AttackIndicator.hotbarYOffset = v);
        attack.addSlider("Scale", ConfigInstance.AttackIndicator.scale / 255f, v -> ConfigInstance.AttackIndicator.scale = v.floatValue());
        widgets.add(attack);

        // ==========================================
        // ROW 3
        // ==========================================

        DropDownWidget vision = new DropDownWidget(0, 0, widgetWidth, "Vision FX");
        vision.addToggleButton("Blindness", () -> ConfigInstance.BlindnessOverlay.enabled, v -> ConfigInstance.BlindnessOverlay.enabled = v);
        vision.addToggleButton("Darkness", () -> ConfigInstance.DarknessOverlay.enabled, v -> ConfigInstance.DarknessOverlay.enabled = v);
        widgets.add(vision);

        DropDownWidget vignette = new DropDownWidget(0, 0, widgetWidth, "Vignette");
        vignette.addToggleButton("Enabled", () -> ConfigInstance.Vignette.enabled, v -> ConfigInstance.Vignette.enabled = v);
        vignette.addSlider("Opacity", ConfigInstance.Vignette.opacity / 255f, v -> ConfigInstance.Vignette.opacity = (float)(v * 255));
        widgets.add(vignette);

        DropDownWidget global = new DropDownWidget(0, 0, widgetWidth, "Global");
        global.addToggleButton("Master Switch", () -> ConfigInstance.OverlayEnabled, v -> ConfigInstance.OverlayEnabled = v);
        widgets.add(global);

        DropDownWidget spyglass = new DropDownWidget(0, 0, widgetWidth, "Spyglass");
        spyglass.addToggleButton("Enabled", () -> ConfigInstance.SpyglassOverlay.enabled, v -> ConfigInstance.SpyglassOverlay.enabled = v);
        spyglass.addSlider("Scale", ConfigInstance.SpyglassOverlay.scale, v -> ConfigInstance.SpyglassOverlay.scale = v.floatValue());
        widgets.add(spyglass);

        // ==========================================
        // ROW 4
        // ==========================================

        DropDownWidget score = new DropDownWidget(0, 0, widgetWidth, "Scoreboard");
        score.addToggleButton("Enabled", () -> ConfigInstance.Scoreboard.enabled, v -> ConfigInstance.Scoreboard.enabled = v);
        widgets.add(score);

        DropDownWidget boat = new DropDownWidget(0, 0, widgetWidth, "Boat");
        boat.addToggleButton("Enabled", () -> ConfigInstance.Boat.enabled, v -> ConfigInstance.Boat.enabled = v);
        boat.addToggleButton("Unlock POV", () -> ConfigInstance.Boat.unlockBoatPov, v -> ConfigInstance.Boat.unlockBoatPov = v);
        widgets.add(boat);

        // ==========================================
        // LAYOUT & POSITIONING
        // ==========================================

        int columns = 4;
        int paddingX = 8;
        int paddingY = 24;

        int totalWidth = (columns * widgetWidth) + ((columns - 1) * paddingX);
        int startX = (this.width - totalWidth) / 2;
        int startY = 40;

        for (int i = 0; i < widgets.size(); i++) {
            DropDownWidget w = widgets.get(i);
            int col = i % columns;
            int row = i / columns;
            w.setX(startX + col * (widgetWidth + paddingX));
            w.setY(startY + row * paddingY);
        }

        for (int i = widgets.size() - 1; i >= 0; i--) {
            this.addRenderableWidget(widgets.get(i));
        }

        this.addRenderableWidget(Button.builder(Component.literal("Close"), b -> this.onClose())
                .bounds(this.width / 2 - 100, this.height - 25, 200, 20).build());
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}