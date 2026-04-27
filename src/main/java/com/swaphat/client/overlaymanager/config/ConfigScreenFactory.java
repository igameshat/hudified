package com.swaphat.client.overlaymanager.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import com.swaphat.client.overlaymanager.gui.screens.LayoutEditorScreen;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConfigScreenFactory {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.overlaymanager.title"))
                .setSavingRunnable(ConfigManager::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        buildGeneral(builder, eb);
        buildOverlays(builder, eb);
        buildHud(builder, eb);
        buildEnvironment(builder, eb);
        buildBoat(builder, eb);
        // We now pass the 'parent' screen into buildShields so the button can reload the UI
        buildShields(builder, eb, parent);
        buildParticles(builder, eb);

        return builder.build();
    }

    private static void buildGeneral(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.general"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.overlayEnabled"),
                        ConfigInstance.OverlayEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.overlaymanager.overlayEnabled.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.OverlayEnabled = v)
                .build());
    }

    private static void buildOverlays(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.overlays"));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.pumpkin"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PumpkinOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.PumpkinOverlay.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.opacity = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.fire"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.FireOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.FireOverlay.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.FireOverlay.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.FireOverlay.opacity = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.fire.offsetPixels"), ConfigInstance.FireOverlay.offsetPixels)
                        .setDefaultValue(0f).setSaveConsumer(v -> ConfigInstance.FireOverlay.offsetPixels = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.spyglass"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.SpyglassOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.scale"), ConfigInstance.SpyglassOverlay.scale)
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.scale = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.portal"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PortalOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.PortalOverlay.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.PortalOverlay.opacity = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.portal.speed"), ConfigInstance.PortalOverlay.speed)
                        .setDefaultValue(1f).setMin(0f).setMax(10f).setSaveConsumer(v -> ConfigInstance.PortalOverlay.speed = v).build(),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.portal.allowGuisInPortal"), ConfigInstance.PortalOverlay.allowGuisInPortal)
                        .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v).build(),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.portal.allowCameraShake"), ConfigInstance.PortalOverlay.allowCameraShake)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowCameraShake = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.freeze"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.FreezeOverlay.enabled)
                        .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.FreezeOverlay.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.opacity = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.freeze.xScale"), ConfigInstance.FreezeOverlay.Xscale)
                        .setDefaultValue(5f).setMin(0.1f).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Xscale = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.freeze.yScale"), ConfigInstance.FreezeOverlay.Yscale)
                        .setDefaultValue(2.6432338f).setMin(0.1f).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Yscale = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.blindness"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.BlindnessOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BlindnessOverlay.enabled = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.darkness"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.DarknessOverlay.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.DarknessOverlay.enabled = v).build()
        ));
    }

    private static void buildHud(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.hud"));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.vignette"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Vignette.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Vignette.enabled = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.Vignette.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.Vignette.opacity = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.bossBar"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.BossBar.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BossBar.enabled = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.bossBar.xOffset"), ConfigInstance.BossBar.bossBarXOffset)
                        .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.BossBar.bossBarXOffset = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.bossBar.yOffset"), ConfigInstance.BossBar.bossBarYOffset)
                        .setDefaultValue(12).setSaveConsumer(v -> ConfigInstance.BossBar.bossBarYOffset = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.scale"), ConfigInstance.BossBar.scale)
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.BossBar.scale = v).build(),
                new ButtonEntry(
                        Component.empty(),
                        Component.literal("Edit Boss Bar Layout"),
                        () -> {
                            Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                            net.minecraft.client.Minecraft.getInstance().setScreen(
                                    new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.BOSS_BAR)
                            );
                        }
                )
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.scoreboard"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Scoreboard.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Scoreboard.enabled = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.totem"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Totem.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.enabled = v).build(),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.totem.showAnimation"), ConfigInstance.Totem.showTotemAnimation)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showTotemAnimation = v).build(),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.totem.showParticles"), ConfigInstance.Totem.showParticles)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showParticles = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.attackIndicator"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.AttackIndicator.enabled)
                        .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.AttackIndicator.enabled = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.attackIndicator.xOffset"), ConfigInstance.AttackIndicator.hotbarXOffset)
                        .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.hotbarXOffset = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.attackIndicator.yOffset"), ConfigInstance.AttackIndicator.hotbarYOffset)
                        .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.hotbarYOffset = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.scale"), ConfigInstance.AttackIndicator.scale)
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.AttackIndicator.scale = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.arrowHighlight"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.ArrowHighlight.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.enabled = v).build(),
                eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.red"), ConfigInstance.ArrowHighlight.red, 0, 255)
                        .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.red = v).build(),
                eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.green"), ConfigInstance.ArrowHighlight.green, 0, 255)
                        .setDefaultValue(158).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.green = v).build(),
                eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.blue"), ConfigInstance.ArrowHighlight.blue, 0, 255)
                        .setDefaultValue(166).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.blue = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.opacity"), ConfigInstance.ArrowHighlight.opacity)
                        .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.opacity = v).build()
        ));

        cat.addEntry(subCategory(eb, Component.translatable("config.overlaymanager.pieChart"),
                eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PieChart.enabled)
                        .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PieChart.enabled = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.pieChart.x"), ConfigInstance.PieChart.x)
                        .setDefaultValue(-1).setSaveConsumer(v -> ConfigInstance.PieChart.x = v).build(),
                eb.startIntField(Component.translatable("config.overlaymanager.pieChart.y"), ConfigInstance.PieChart.y)
                        .setDefaultValue(500).setSaveConsumer(v -> ConfigInstance.PieChart.y = v).build(),
                eb.startFloatField(Component.translatable("config.overlaymanager.scale"), ConfigInstance.PieChart.scale)
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.PieChart.scale = v).build(),
                new ButtonEntry(
                        Component.empty(),
                        Component.literal("Edit Pie Chart Layout"),
                        () -> {
                            Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                            net.minecraft.client.Minecraft.getInstance().setScreen(
                                    new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.PIE_CHART)
                            );
                        }
                )
        ));
    }

    private static void buildEnvironment(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.environment"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.fullbright"), ConfigInstance.Environment.fullbright)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.fullbright = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.disableFog"), ConfigInstance.Environment.disableFog)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.disableFog = v).build());
        cat.addEntry(eb.startFloatField(Component.translatable("config.overlaymanager.environment.fogMultiplier"), ConfigInstance.Environment.fogMultiplier)
                .setDefaultValue(1f).setMin(0f).setMax(10f).setSaveConsumer(v -> ConfigInstance.Environment.fogMultiplier = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.clearLava"), ConfigInstance.Environment.clearLava)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearLava = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.clearWater"), ConfigInstance.Environment.clearWater)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearWater = v).build());
        cat.addEntry(eb.startFloatField(Component.translatable("config.overlaymanager.environment.rainOpacity"), ConfigInstance.Environment.rainOpacity)
                .setDefaultValue(1f).setMin(0f).setMax(1f).setSaveConsumer(v -> ConfigInstance.Environment.rainOpacity = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.noRainParticles"), ConfigInstance.Environment.noRainParticles)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noRainParticles = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.noSnow"), ConfigInstance.Environment.noSnow)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noSnow = v).build());
    }

    private static void buildBoat(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.boat"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Boat.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.enabled = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.boat.unlockBoatPov"), ConfigInstance.Boat.unlockBoatPov)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.unlockBoatPov = v).build());
    }

    private static void buildParticles(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.particles"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.particles.enabled"),
                        ConfigInstance.Particle.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Particle.enabled = v)
                .build());

        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.particles.globalChance"),
                        Math.round(ConfigInstance.Particle.globalChance * 100), 0, 100)
                .setDefaultValue(100)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.overlaymanager.particles.globalChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.globalChance = v / 100f)
                .build());

        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.particles.selfPotionChance"),
                        Math.round(ConfigInstance.Particle.selfPotionChance * 100), 0, 100)
                .setDefaultValue(50)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.overlaymanager.particles.selfPotionChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.selfPotionChance = v / 100f)
                .build());

        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.particles.otherPotionChance"),
                        Math.round(ConfigInstance.Particle.otherPotionChance * 100), 0, 100)
                .setDefaultValue(100)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.overlaymanager.particles.otherPotionChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.otherPotionChance = v / 100f)
                .build());

        List<me.shedaniel.clothconfig2.api.AbstractConfigListEntry> particleSliders = new ArrayList<>();

        List<net.minecraft.resources.Identifier> keys = new ArrayList<>(
                net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE.keySet()
        );
        keys.sort(java.util.Comparator.comparing(net.minecraft.resources.Identifier::toString));

        for (net.minecraft.resources.Identifier loc : keys) {
            String id = loc.toString();

            float currentVal = ConfigInstance.Particle.customParticleChances.getOrDefault(id, -1.0f);
            int displayVal = currentVal < 0 ? -1 : Math.round(currentVal * 100);

            particleSliders.add(eb.startIntSlider(Component.literal(id), displayVal, -1, 100)
                    .setDefaultValue(-1)
                    .setTextGetter(val -> val == -1 ? Component.literal("Default") : Component.literal(val + "%"))
                    .setTooltip(Component.translatable("config.overlaymanager.particles.specific.tooltip"))
                    .setSaveConsumer(v -> {
                        if (v == -1) {
                            ConfigInstance.Particle.customParticleChances.remove(id);
                        } else {
                            ConfigInstance.Particle.customParticleChances.put(id, v / 100f);
                        }
                    })
                    .build());
        }

        cat.addEntry(eb.startSubCategory(
                        Component.translatable("config.overlaymanager.particles.specific"),
                        particleSliders)
                .setExpanded(false)
                .build());
    }

    // Notice the updated method signature to receive the 'parent' screen
    private static void buildShields(ConfigBuilder builder, ConfigEntryBuilder eb, Screen parent) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.shields"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.enabled"),
                        ConfigInstance.Shields.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Shields.enabled = v)
                .build());

        cat.addEntry(eb.startIntField(
                        Component.translatable("config.overlaymanager.shields.simpleHeight"),
                        ConfigInstance.Shields.simpleYOffset)
                .setDefaultValue(0)
                .setTooltip(Component.translatable("config.overlaymanager.shields.simpleHeight.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Shields.simpleYOffset = v)
                .build());

        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Apply Side Shield Preset"),
                () -> {
                    applyMrOrdenadorPresets();
                    ConfigManager.save();
                    net.minecraft.client.Minecraft.getInstance().setScreen(ConfigScreenFactory.create(parent));
                }
        ));

        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Apply Default Shield Settings"),
                () -> {
                    ConfigManager.save();
                    applyVanillaOptions();
                    net.minecraft.client.Minecraft.getInstance().setScreen(ConfigScreenFactory.create(parent));
                }
        ));

        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.firstPersonMain"),
                ConfigInstance.Shields.firstPersonMain));
        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.firstPersonOff"),
                ConfigInstance.Shields.firstPersonOff));
        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.thirdPersonMain"),
                ConfigInstance.Shields.thirdPersonMain));
        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.thirdPersonOff"),
                ConfigInstance.Shields.thirdPersonOff));
        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.otherPlayersMain"),
                ConfigInstance.Shields.otherPlayersMain));
        cat.addEntry(handSettingsSubCategory(eb,
                Component.translatable("config.overlaymanager.shields.otherPlayersOff"),
                ConfigInstance.Shields.otherPlayersOff));
    }


    private static void applyMrOrdenadorPresets() {
        ConfigInstance.Shields.firstPersonMain.idle.xOffset = 27.0;
        ConfigInstance.Shields.firstPersonMain.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonMain.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleX = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleY = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleZ = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotX = 5.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotY = 90.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotZ = -5.0f;

        ConfigInstance.Shields.firstPersonMain.blocking.xOffset = -19.0;
        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = -31.0;
        ConfigInstance.Shields.firstPersonMain.blocking.zOffset = 0.0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleX = 0.7750105f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleY = 0.7752809f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleZ = 1.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotX = -5.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotY = -3.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotZ = 0.0f;

        ConfigInstance.Shields.firstPersonOff.idle.xOffset = -8.0;
        ConfigInstance.Shields.firstPersonOff.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonOff.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleX = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleY = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleZ = 0.80898875f;
        ConfigInstance.Shields.firstPersonOff.idle.rotX = 0.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotY = 90.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotZ = -5.0f;

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

    private static void applyVanillaOptions() {
        ConfigInstance.Shields.firstPersonMain.idle.xOffset = 0;
        ConfigInstance.Shields.firstPersonMain.idle.yOffset = 0;
        ConfigInstance.Shields.firstPersonMain.idle.zOffset = 0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleX = 0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleY = 0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleZ = 0;
        ConfigInstance.Shields.firstPersonMain.idle.rotX = 0;
        ConfigInstance.Shields.firstPersonMain.idle.rotY = 0;
        ConfigInstance.Shields.firstPersonMain.idle.rotZ = 0;

        ConfigInstance.Shields.firstPersonMain.blocking.xOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.zOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleX = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleY = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleZ = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.rotX = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.rotY = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.rotZ = 0;

        ConfigInstance.Shields.firstPersonOff.idle.xOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.yOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.zOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleX = 0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleY = 0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleZ = 0;
        ConfigInstance.Shields.firstPersonOff.idle.rotX = 0;
        ConfigInstance.Shields.firstPersonOff.idle.rotY = 0;
        ConfigInstance.Shields.firstPersonOff.idle.rotZ = 0;

        ConfigInstance.Shields.firstPersonOff.blocking.xOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.yOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.zOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleX = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleY = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleZ = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.rotX = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.rotY = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.rotZ = 0;
    }

    private static SubCategoryListEntry handSettingsSubCategory(
            ConfigEntryBuilder eb,
            Component label,
            ConfigInstance.HandSettings hand) {

        List<me.shedaniel.clothconfig2.api.AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startTextDescription(Component.translatable("config.overlaymanager.shields.pose.idle")).build());
        addShieldSettingsEntries(entries, eb, hand.idle);
        entries.add(eb.startTextDescription(Component.translatable("config.overlaymanager.shields.pose.blocking")).build());
        addShieldSettingsEntries(entries, eb, hand.blocking);

        SubCategoryBuilder sub = eb.startSubCategory(label, entries);
        sub.setExpanded(false);
        return sub.build();
    }

    @SuppressWarnings({"rawtypes"})
    private static void addShieldSettingsEntries(
            List<me.shedaniel.clothconfig2.api.AbstractConfigListEntry> list,
            ConfigEntryBuilder eb,
            ConfigInstance.ShieldSettings s) {

        list.add(eb.startDoubleField(Component.translatable("config.overlaymanager.shields.xOffset"), s.xOffset)
                .setDefaultValue(0.0).setSaveConsumer(v -> s.xOffset = v).build());
        list.add(eb.startDoubleField(Component.translatable("config.overlaymanager.shields.yOffset"), s.yOffset)
                .setDefaultValue(0.0).setSaveConsumer(v -> s.yOffset = v).build());
        list.add(eb.startDoubleField(Component.translatable("config.overlaymanager.shields.zOffset"), s.zOffset)
                .setDefaultValue(0.0).setSaveConsumer(v -> s.zOffset = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.scaleX"), s.scaleX)
                .setDefaultValue(1f).setMin(0f).setSaveConsumer(v -> s.scaleX = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.scaleY"), s.scaleY)
                .setDefaultValue(1f).setMin(0f).setSaveConsumer(v -> s.scaleY = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.scaleZ"), s.scaleZ)
                .setDefaultValue(1f).setMin(0f).setSaveConsumer(v -> s.scaleZ = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.rotX"), s.rotX)
                .setDefaultValue(0f).setSaveConsumer(v -> s.rotX = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.rotY"), s.rotY)
                .setDefaultValue(0f).setSaveConsumer(v -> s.rotY = v).build());
        list.add(eb.startFloatField(Component.translatable("config.overlaymanager.shields.rotZ"), s.rotZ)
                .setDefaultValue(0f).setSaveConsumer(v -> s.rotZ = v).build());
    }

    @SuppressWarnings({"rawtypes"})
    private static SubCategoryListEntry subCategory(
            ConfigEntryBuilder eb,
            Component label,
            me.shedaniel.clothconfig2.api.AbstractConfigListEntry<?>... children) {

        List<me.shedaniel.clothconfig2.api.AbstractConfigListEntry> list = new ArrayList<>(Arrays.asList(children));
        SubCategoryBuilder sub = eb.startSubCategory(label, list);
        sub.setExpanded(false);
        return sub.build();
    }

    // ==========================================
    // Custom Config Entry for the Action Buttons
    // ==========================================
    public static class ButtonEntry extends me.shedaniel.clothconfig2.api.AbstractConfigListEntry<Object> {
        private final Button button;

        public ButtonEntry(Component fieldName, Component buttonText, Runnable onClick) {
            super(fieldName, false);
            this.button = Button.builder(buttonText, btn -> onClick.run())
                    .bounds(0, 0, 150, 20)
                    .build();
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Optional<Object> getDefaultValue() {
            return Optional.empty();
        }

        @Override
        public void save() {
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
            this.button.setX(x + entryWidth / 2 - this.button.getWidth() / 2);
            this.button.setY(y);
            this.button.render(guiGraphics, mouseX, mouseY, delta);
        }

        @Override
        public @NonNull List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.button);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.button);
        }
    }
}