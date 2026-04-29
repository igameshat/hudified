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
        buildPumpkin(builder, eb);
        buildFire(builder, eb);
        buildSpyglass(builder, eb);
        buildPortal(builder, eb);
        buildFreeze(builder, eb);
        buildBlindness(builder, eb);
        buildDarkness(builder, eb);
        buildVignette(builder, eb);
        buildBossBar(builder, eb);
        buildScoreboard(builder, eb);
        buildTotem(builder, eb);
        buildAttackIndicator(builder, eb);
        buildArrowHighlight(builder, eb);
        buildPieChart(builder, eb);
        buildEnvironment(builder, eb);
        buildBoat(builder, eb);
        buildShields(builder, eb, parent);
        buildParticles(builder, eb);
        buildDroppedItems(builder, eb);

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

    private static void buildPumpkin(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.pumpkin"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PumpkinOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.PumpkinOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.opacity = v / 100f).build());
    }

    private static void buildFire(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.fire"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.FireOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.FireOverlay.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.FireOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.FireOverlay.opacity = v / 100f).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.fire.offsetPixels"), Math.round(ConfigInstance.FireOverlay.offsetPixels), -500, 500)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.FireOverlay.offsetPixels = v).build());
    }

    private static void buildSpyglass(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.spyglass"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.SpyglassOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scale"), Math.round(ConfigInstance.SpyglassOverlay.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.scale = v / 100f).build());
    }

    private static void buildPortal(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.portal"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PortalOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.PortalOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.PortalOverlay.opacity = v / 100f).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.portal.speed"), Math.round(ConfigInstance.PortalOverlay.speed * 100), 0, 1000)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.PortalOverlay.speed = v / 100f).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.portal.allowGuisInPortal"), ConfigInstance.PortalOverlay.allowGuisInPortal)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.portal.allowCameraShake"), ConfigInstance.PortalOverlay.allowCameraShake)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowCameraShake = v).build());
    }

    private static void buildFreeze(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.freeze"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.FreezeOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.FreezeOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.opacity = v / 100f).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.freeze.xScale"), Math.round(ConfigInstance.FreezeOverlay.Xscale * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Xscale = v / 100f).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.freeze.yScale"), Math.round(ConfigInstance.FreezeOverlay.Yscale * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Yscale = v / 100f).build());
    }

    private static void buildBlindness(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.blindness"));
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.BlindnessOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BlindnessOverlay.enabled = v).build());
    }

    private static void buildDarkness(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.darkness"));
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.DarknessOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.DarknessOverlay.enabled = v).build());
    }

    private static void buildVignette(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.vignette"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Vignette.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Vignette.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.Vignette.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.Vignette.opacity = v / 100f).build());
    }

    private static void buildBossBar(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.bossBar"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.BossBar.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BossBar.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.bossBar.xOffset"), ConfigInstance.BossBar.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.BossBar.XOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.bossBar.yOffset"), ConfigInstance.BossBar.YOffset, -1000, 1000)
                .setDefaultValue(12).setSaveConsumer(v -> ConfigInstance.BossBar.YOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scale"), Math.round(ConfigInstance.BossBar.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.BossBar.scale = v / 100f).build());
        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Edit Boss Bar Layout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                    net.minecraft.client.Minecraft.getInstance().setScreen(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.BOSS_BAR)
                    );
                }
        ));
    }

    private static void buildScoreboard(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.scoreboard"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Scoreboard.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Scoreboard.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scoreboard.xOffset"), ConfigInstance.Scoreboard.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.Scoreboard.XOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scoreboard.yOffset"), ConfigInstance.Scoreboard.YOffset, -1000, 1000)
                .setDefaultValue(12).setSaveConsumer(v -> ConfigInstance.Scoreboard.YOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scale"), Math.round(ConfigInstance.Scoreboard.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.Scoreboard.scale = v / 100f).build());
        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Edit Scoreboard Layout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                    net.minecraft.client.Minecraft.getInstance().setScreen(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.SCOREBOARD)
                    );
                }
        ));
    }

    private static void buildTotem(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.totem"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Totem.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.enabled = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.totem.showAnimation"), ConfigInstance.Totem.showTotemAnimation)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showTotemAnimation = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.totem.showParticles"), ConfigInstance.Totem.showParticles)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showParticles = v).build());
    }

    private static void buildAttackIndicator(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.attackIndicator"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.AttackIndicator.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.AttackIndicator.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.attackIndicator.xOffset"), ConfigInstance.AttackIndicator.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.XOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.attackIndicator.yOffset"), ConfigInstance.AttackIndicator.YOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.YOffset = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scale"), Math.round(ConfigInstance.AttackIndicator.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.AttackIndicator.scale = v / 100f).build());
        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Edit Indicator Layout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                    net.minecraft.client.Minecraft.getInstance().setScreen(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.ATTACK_INDICATOR)
                    );
                }
        ));
    }

    private static void buildArrowHighlight(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.arrowHighlight"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.ArrowHighlight.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.red"), ConfigInstance.ArrowHighlight.red, 0, 255)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.red = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.green"), ConfigInstance.ArrowHighlight.green, 0, 255)
                .setDefaultValue(158).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.green = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.arrowHighlight.blue"), ConfigInstance.ArrowHighlight.blue, 0, 255)
                .setDefaultValue(166).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.blue = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.opacity"), Math.round(ConfigInstance.ArrowHighlight.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.ArrowHighlight.opacity = v / 100f).build());
    }

    private static void buildPieChart(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.pieChart"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.PieChart.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PieChart.enabled = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.pieChart.x"), ConfigInstance.PieChart.x, -1, 2000)
                .setDefaultValue(-1).setTextGetter(val -> val == -1 ? Component.literal("Auto (-1)") : Component.literal(String.valueOf(val))).setSaveConsumer(v -> ConfigInstance.PieChart.x = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.pieChart.y"), ConfigInstance.PieChart.y, -1, 2000)
                .setDefaultValue(500).setTextGetter(val -> val == -1 ? Component.literal("Auto (-1)") : Component.literal(String.valueOf(val))).setSaveConsumer(v -> ConfigInstance.PieChart.y = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.scale"), Math.round(ConfigInstance.PieChart.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.PieChart.scale = v / 100f).build());
        cat.addEntry(new ButtonEntry(
                Component.empty(),
                Component.literal("Edit Pie Chart Layout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().screen;
                    net.minecraft.client.Minecraft.getInstance().setScreen(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.PIE_CHART)
                    );
                }
        ));
    }

    private static void buildEnvironment(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.category.environment"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.fullbright"), ConfigInstance.Environment.fullbright)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.fullbright = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.disableFog"), ConfigInstance.Environment.disableFog)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.disableFog = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.environment.fogMultiplier"), Math.round(ConfigInstance.Environment.fogMultiplier * 100), 0, 1000)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.Environment.fogMultiplier = v / 100f).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.clearLava"), ConfigInstance.Environment.clearLava)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearLava = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.clearWater"), ConfigInstance.Environment.clearWater)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearWater = v).build());
        cat.addEntry(eb.startIntSlider(Component.translatable("config.overlaymanager.environment.rainOpacity"), Math.round(ConfigInstance.Environment.rainOpacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.Environment.rainOpacity = v / 100f).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.noRainParticles"), ConfigInstance.Environment.noRainParticles)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noRainParticles = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.noSnow"), ConfigInstance.Environment.noSnow)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noSnow = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.environment.blockBreaking"), ConfigInstance.Environment.blockBreakingOverlay).setTooltip(Component.translatable("config.overlaymanager.environment.blockBreaking.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.blockBreakingOverlay = v).build());
    }

    private static void buildBoat(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.category.boat"));

        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.enabled"), ConfigInstance.Boat.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.enabled = v).build());
        cat.addEntry(eb.startBooleanToggle(Component.translatable("config.overlaymanager.boat.unlockBoatPov"), ConfigInstance.Boat.unlockBoatPov)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.unlockBoatPov = v).build());
    }

    private static void buildParticles(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.category.particles"));

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

    private static void buildShields(ConfigBuilder builder, ConfigEntryBuilder eb, Screen parent) {
        ConfigCategory cat = builder.getOrCreateCategory(Component.translatable("config.overlaymanager.category.shields"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.enabled"),
                        ConfigInstance.Shields.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Shields.enabled = v)
                .build());

        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.shields.simpleHeight"),
                        ConfigInstance.Shields.simpleYOffset, -500, 500)
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

        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.firstPersonMain"), ConfigInstance.Shields.firstPersonMain));
        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.firstPersonOff"), ConfigInstance.Shields.firstPersonOff));
        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.thirdPersonMain"), ConfigInstance.Shields.thirdPersonMain));
        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.thirdPersonOff"), ConfigInstance.Shields.thirdPersonOff));
        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.otherPlayersMain"), ConfigInstance.Shields.otherPlayersMain));
        cat.addEntry(handSettingsSubCategory(eb, Component.translatable("config.overlaymanager.shields.otherPlayersOff"), ConfigInstance.Shields.otherPlayersOff));
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
        ConfigInstance.Shields.firstPersonMain.idle.scaleX = 1;
        ConfigInstance.Shields.firstPersonMain.idle.scaleY = 1;
        ConfigInstance.Shields.firstPersonMain.idle.scaleZ = 1;
        ConfigInstance.Shields.firstPersonMain.idle.rotX = 0;
        ConfigInstance.Shields.firstPersonMain.idle.rotY = 0;
        ConfigInstance.Shields.firstPersonMain.idle.rotZ = 0;

        ConfigInstance.Shields.firstPersonMain.blocking.xOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.yOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.zOffset = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleX = 1;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleY = 1;
        ConfigInstance.Shields.firstPersonMain.blocking.scaleZ = 1;
        ConfigInstance.Shields.firstPersonMain.blocking.rotX = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.rotY = 0;
        ConfigInstance.Shields.firstPersonMain.blocking.rotZ = 0;

        ConfigInstance.Shields.firstPersonOff.idle.xOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.yOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.zOffset = 0;
        ConfigInstance.Shields.firstPersonOff.idle.scaleX = 1;
        ConfigInstance.Shields.firstPersonOff.idle.scaleY = 1;
        ConfigInstance.Shields.firstPersonOff.idle.scaleZ = 1;
        ConfigInstance.Shields.firstPersonOff.idle.rotX = 0;
        ConfigInstance.Shields.firstPersonOff.idle.rotY = 0;
        ConfigInstance.Shields.firstPersonOff.idle.rotZ = 0;

        ConfigInstance.Shields.firstPersonOff.blocking.xOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.yOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.zOffset = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleX = 1;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleY = 1;
        ConfigInstance.Shields.firstPersonOff.blocking.scaleZ = 1;
        ConfigInstance.Shields.firstPersonOff.blocking.rotX = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.rotY = 0;
        ConfigInstance.Shields.firstPersonOff.blocking.rotZ = 0;
    }

    private static void buildDroppedItems(ConfigBuilder builder, ConfigEntryBuilder eb) {
        ConfigCategory cat = builder.getOrCreateCategory(
                Component.translatable("config.overlaymanager.category.droppedItems"));

        cat.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.overlaymanager.enabled"),
                        ConfigInstance.DroppedItems.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.enabled = v)
                .build());

        cat.addEntry(eb.startIntSlider(
                        Component.translatable("config.overlaymanager.droppedItems.scale"),
                        Math.round(ConfigInstance.DroppedItems.customScale * 100), 10, 1000)
                .setDefaultValue(300)
                .setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f)))
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.customScale = v / 100f)
                .build());

        cat.addEntry(eb.startStrList(
                        Component.translatable("config.overlaymanager.droppedItems.list"),
                        ConfigInstance.DroppedItems.itemList)
                .setDefaultValue(java.util.List.of("minecraft:golden_apple"))
                .setTooltip(Component.translatable("config.overlaymanager.droppedItems.list.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.itemList = v)
                .setCellErrorSupplier(str -> {
                    net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(str);

                    if (id == null || !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(id)) {
                        return java.util.Optional.of(net.minecraft.network.chat.Component.literal("Invalid Item ID. Example: minecraft:apple"));
                    }

                    return java.util.Optional.empty();
                })
                .build());
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

        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.xOffset"), (int) Math.round(s.xOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.xOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.yOffset"), (int) Math.round(s.yOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.yOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.zOffset"), (int) Math.round(s.zOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.zOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.scaleX"), Math.round(s.scaleX * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleX = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.scaleY"), Math.round(s.scaleY * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleY = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.scaleZ"), Math.round(s.scaleZ * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleZ = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.rotX"), Math.round(s.rotX), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotX = (float) v).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.rotY"), Math.round(s.rotY), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotY = (float) v).build());
        list.add(eb.startIntSlider(Component.translatable("config.overlaymanager.shields.rotZ"), Math.round(s.rotZ), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotZ = (float) v).build());
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