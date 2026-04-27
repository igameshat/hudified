package com.swaphat.client.overlaymanager.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Cloth Config 2 screen factory for OverlayManager.
 * 1.21.1 Fabric · Mojang mappings · cloth-config2
 */
public class ConfigScreenFactory {

    private static boolean hasBackedUpSettings = false;


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
        buildShields(builder, eb);
        buildParticles(builder, eb); // Added Particles

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
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.BossBar.scale = v).build()
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
                        .setDefaultValue(1f).setMin(0.1f).setMax(5f).setSaveConsumer(v -> ConfigInstance.PieChart.scale = v).build()
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

        // 1. Master Toggle
        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.particles.enabled"),
                        ConfigInstance.Particle.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Particle.enabled = v)
                .build());

        // 2. Global Chance (Converted to Int Slider 0-100 for cleaner UI)
        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.particles.globalChance"),
                        Math.round(ConfigInstance.Particle.globalChance * 100), 0, 100)
                .setDefaultValue(100)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.overlaymanager.particles.globalChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.globalChance = v / 100f)
                .build());

        // 3. Potion Swirls
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

        // 4. Dynamic List of ALL Registered Particles
        List<me.shedaniel.clothconfig2.api.AbstractConfigListEntry> particleSliders = new ArrayList<>();

        // Grab all particle IDs and sort them alphabetically
        List<net.minecraft.resources.Identifier> keys = new ArrayList<>(
                net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE.keySet()
        );
        keys.sort(java.util.Comparator.comparing(net.minecraft.resources.Identifier::toString));

        for (net.minecraft.resources.Identifier loc : keys) {
            String id = loc.toString();

            // Fetch current custom value, or default to -1.0
            float currentVal = ConfigInstance.Particle.customParticleChances.getOrDefault(id, -1.0f);
            int displayVal = currentVal < 0 ? -1 : Math.round(currentVal * 100);

            particleSliders.add(eb.startIntSlider(Component.literal(id), displayVal, -1, 100)
                    .setDefaultValue(-1)
                    // -1 shows "Default", otherwise shows "X%"
                    .setTextGetter(val -> val == -1 ? Component.literal("Default") : Component.literal(val + "%"))
                    .setTooltip(Component.translatable("config.overlaymanager.particles.specific.tooltip"))
                    .setSaveConsumer(v -> {
                        // If set back to Default (-1), remove from the JSON map so it doesn't bloat the file
                        if (v == -1) {
                            ConfigInstance.Particle.customParticleChances.remove(id);
                        } else {
                            ConfigInstance.Particle.customParticleChances.put(id, v / 100f);
                        }
                    })
                    .build());
        }

        // Pack all those dynamically generated sliders into a sub-category
        cat.addEntry(eb.startSubCategory(
                        Component.translatable("config.overlaymanager.particles.specific"),
                        particleSliders)
                .setExpanded(false)
                .build());
    }


    private static void buildShields(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.shields"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.enabled"),
                        ConfigInstance.Shields.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Shields.enabled = v)
                .build());

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.shields.advancedOptions"),
                        ConfigInstance.Shields.advancedOptions)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.overlaymanager.shields.advancedOptions.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Shields.advancedOptions = v)
                .build());

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.shields.mrOrdenadorPresets"),
                        ConfigInstance.ShieldConfig.MrOrdenadorPresets)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.overlaymanager.shields.mrOrdenadorPresets.tooltip"))
                .setSaveConsumer(v -> {
                    ConfigInstance.ShieldConfig.MrOrdenadorPresets = v;
                    if (v) {
                        if (!hasBackedUpSettings) {
                            saveUserPresets();
                            hasBackedUpSettings = true;
                        }
                        applyMrOrdenadorPresets();
                    } else {
                        restoreUserPresets();
                    }
                })
                .build());

        cat.addEntry(eb.startIntField(
                        Component.translatable("config.overlaymanager.shields.simpleHeight"),
                        (int) ConfigInstance.Shields.firstPersonMain.idle.yOffset)
                .setDefaultValue(0)
                .setTooltip(Component.translatable("config.overlaymanager.shields.simpleHeight.tooltip"))
                .setSaveConsumer(v -> {
                    if (ConfigInstance.ShieldConfig.MrOrdenadorPresets) {
                        ConfigInstance.Shields.firstPersonMain.idle.yOffset     = v;
                        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = -6.0 + v;
                        ConfigInstance.Shields.firstPersonOff.idle.yOffset      = -125.0 + v;
                        ConfigInstance.Shields.firstPersonOff.blocking.yOffset  = 6.0 + v;
                    } else {
                        ConfigInstance.Shields.firstPersonMain.idle.yOffset     = v;
                        ConfigInstance.Shields.firstPersonOff.idle.yOffset      = v;
                        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = v;
                        ConfigInstance.Shields.firstPersonOff.blocking.yOffset  = v;
                    }
                })
                .build());

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
        // First Person Main Hand — Idle
        ConfigInstance.Shields.firstPersonMain.idle.xOffset = 27.0;
        ConfigInstance.Shields.firstPersonMain.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonMain.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonMain.idle.scaleX  = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleY  = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.scaleZ  = 1.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotX    = 5.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotY    = 90.0f;
        ConfigInstance.Shields.firstPersonMain.idle.rotZ    = -5.0f;
        // First Person Main Hand — Blocking
        ConfigInstance.Shields.firstPersonMain.blocking.xOffset = -19.0;
        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = -31.0;
        ConfigInstance.Shields.firstPersonMain.blocking.zOffset = 0.0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleX  = 0.7750105f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleY  = 0.7752809f;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleZ  = 1.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotX    = -5.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotY    = -3.0f;
        ConfigInstance.Shields.firstPersonMain.blocking.rotZ    = 0.0f;
        // First Person Off Hand — Idle
        ConfigInstance.Shields.firstPersonOff.idle.xOffset = -8.0;
        ConfigInstance.Shields.firstPersonOff.idle.yOffset = -27.0;
        ConfigInstance.Shields.firstPersonOff.idle.zOffset = 8.0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleX  = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleY  = 1.0f;
        ConfigInstance.Shields.firstPersonOff.idle.scaleZ  = 0.80898875f;
        ConfigInstance.Shields.firstPersonOff.idle.rotX    = 0.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotY    = 90.0f;
        ConfigInstance.Shields.firstPersonOff.idle.rotZ    = -5.0f;
        // First Person Off Hand — Blocking
        ConfigInstance.Shields.firstPersonOff.blocking.xOffset = -5.0;
        ConfigInstance.Shields.firstPersonOff.blocking.yOffset = -32.0;
        ConfigInstance.Shields.firstPersonOff.blocking.zOffset = 0.0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleX  = 0.8876405f;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleY  = 1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleZ  = 1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotX    = -5.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotY    = -1.0f;
        ConfigInstance.Shields.firstPersonOff.blocking.rotZ    = 3.0f;
    }

    private static void saveUserPresets() {
        copyHandSettings(ConfigInstance.Shields.firstPersonMain, ConfigInstance.Shields.backupFirstPersonMain);
        copyHandSettings(ConfigInstance.Shields.firstPersonOff,  ConfigInstance.Shields.backupFirstPersonOff);
    }

    private static void restoreUserPresets() {
        if (hasBackedUpSettings) {
            copyHandSettings(ConfigInstance.Shields.backupFirstPersonMain, ConfigInstance.Shields.firstPersonMain);
            copyHandSettings(ConfigInstance.Shields.backupFirstPersonOff,  ConfigInstance.Shields.firstPersonOff);
            hasBackedUpSettings = false;
        } else {
            applyVanillaDefaults();
        }
    }

    private static void applyVanillaDefaults() {
        resetHandSettings(ConfigInstance.Shields.firstPersonMain);
        resetHandSettings(ConfigInstance.Shields.firstPersonOff);
        resetHandSettings(ConfigInstance.Shields.thirdPersonMain);
        resetHandSettings(ConfigInstance.Shields.thirdPersonOff);
        resetHandSettings(ConfigInstance.Shields.otherPlayersMain);
        resetHandSettings(ConfigInstance.Shields.otherPlayersOff);
    }

    private static void resetHandSettings(ConfigInstance.HandSettings h) {
        h.idle.xOffset = 0;    h.idle.yOffset = 0;    h.idle.zOffset = 0;
        h.idle.scaleX  = 1f;   h.idle.scaleY  = 1f;   h.idle.scaleZ  = 1f;
        h.idle.rotX    = 0f;   h.idle.rotY    = 0f;   h.idle.rotZ    = 0f;
        h.blocking.xOffset = 0;    h.blocking.yOffset = 0;    h.blocking.zOffset = 0;
        h.blocking.scaleX  = 1f;   h.blocking.scaleY  = 1f;   h.blocking.scaleZ  = 1f;
        h.blocking.rotX    = 0f;   h.blocking.rotY    = 0f;   h.blocking.rotZ    = 0f;
    }

    private static void copyHandSettings(ConfigInstance.HandSettings src, ConfigInstance.HandSettings dst) {
        dst.idle.xOffset = src.idle.xOffset; dst.idle.yOffset = src.idle.yOffset; dst.idle.zOffset = src.idle.zOffset;
        dst.idle.scaleX  = src.idle.scaleX;  dst.idle.scaleY  = src.idle.scaleY;  dst.idle.scaleZ  = src.idle.scaleZ;
        dst.idle.rotX    = src.idle.rotX;    dst.idle.rotY    = src.idle.rotY;    dst.idle.rotZ    = src.idle.rotZ;
        dst.blocking.xOffset = src.blocking.xOffset; dst.blocking.yOffset = src.blocking.yOffset; dst.blocking.zOffset = src.blocking.zOffset;
        dst.blocking.scaleX  = src.blocking.scaleX;  dst.blocking.scaleY  = src.blocking.scaleY;  dst.blocking.scaleZ  = src.blocking.scaleZ;
        dst.blocking.rotX    = src.blocking.rotX;    dst.blocking.rotY    = src.blocking.rotY;    dst.blocking.rotZ    = src.blocking.rotZ;
    }


    private static SubCategoryListEntry handSettingsSubCategory(
            ConfigEntryBuilder eb,
            Component label,
            ConfigInstance.HandSettings hand) {

        List<   me.shedaniel.clothconfig2.api.AbstractConfigListEntry> entries = new ArrayList<>();
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
}