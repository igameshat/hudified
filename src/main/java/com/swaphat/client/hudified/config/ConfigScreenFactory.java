package com.swaphat.client.hudified.config;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import com.swaphat.client.hudified.gui.screens.LayoutEditorScreen;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class ConfigScreenFactory {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.hudified.title"))
                .setSavingRunnable(ConfigManager::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory visualOverlaysCat = builder.getOrCreateCategory(
                Component.translatable("config.hudified.category.visualOverlays"));
        visualOverlaysCat.addEntry(buildGeneral(eb));
        visualOverlaysCat.addEntry(buildPumpkin(eb));
        visualOverlaysCat.addEntry(buildFire(eb));
        visualOverlaysCat.addEntry(buildSpyglass(eb));
        visualOverlaysCat.addEntry(buildPortal(eb));
        visualOverlaysCat.addEntry(buildFreeze(eb));
        visualOverlaysCat.addEntry(buildBlindness(eb));
        visualOverlaysCat.addEntry(buildDarkness(eb));
        visualOverlaysCat.addEntry(buildVignette(eb));
        visualOverlaysCat.addEntry(buildprojectileHighlight(eb));

        ConfigCategory hudUiCat = builder.getOrCreateCategory(
                Component.translatable("config.hudified.category.hudUi"));
        hudUiCat.addEntry(buildBossBar(eb));
        hudUiCat.addEntry(buildScoreboard(eb));
        hudUiCat.addEntry(buildAttackIndicator(eb));
        hudUiCat.addEntry(buildTotem(eb));
        hudUiCat.addEntry(buildPieChart(eb));

        ConfigCategory entityWorldCat = builder.getOrCreateCategory(
                Component.translatable("config.hudified.category.entityWorld"));
        entityWorldCat.addEntry(buildBoat(eb));
        entityWorldCat.addEntry(buildDroppedItems(eb));
        entityWorldCat.addEntry(buildParticles(eb));
        entityWorldCat.addEntry(buildShields(eb, parent));

        ConfigCategory environmentCat = builder.getOrCreateCategory(
                Component.translatable("config.hudified.category.environmentGameplay"));
        environmentCat.addEntry(buildEnvironment(eb));

        return builder.build();
    }

    private static SubCategoryListEntry buildGeneral(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(
                        Component.translatable("config.hudified.overlayEnabled"),
                        ConfigInstance.OverlayEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.hudified.overlayEnabled.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.OverlayEnabled = v)
                .build());

        entries.add(eb.startKeyCodeField(
                        Component.translatable("config.hudified.menuKeybind"),
                        ConfigInstance.menuKeybind)
                .setDefaultValue(InputConstants.UNKNOWN)
                .setKeySaveConsumer(v -> ConfigInstance.menuKeybind = v)
                .build());

        return eb.startSubCategory(Component.translatable("config.hudified.category.general"), entries).setExpanded(true).build();
    }

    private static SubCategoryListEntry buildPumpkin(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.PumpkinOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.PumpkinOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.PumpkinOverlay.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.PumpkinOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.PumpkinOverlay.opacity = v / 100f).build());
        return eb.startSubCategory(Component.translatable("config.hudified.pumpkin"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildFire(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.FireOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.FireOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.FireOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.FireOverlay.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.FireOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.FireOverlay.opacity = v / 100f).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.fire.offsetPixels"), Math.round(ConfigInstance.FireOverlay.offsetPixels), -500, 500)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.FireOverlay.offsetPixels = v).build());
        return eb.startSubCategory(Component.translatable("config.hudified.fire"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildSpyglass(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.SpyglassOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.SpyglassOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.SpyglassOverlay.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scale"), Math.round(ConfigInstance.SpyglassOverlay.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.SpyglassOverlay.scale = v / 100f).build());
        return eb.startSubCategory(Component.translatable("config.hudified.spyglass"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildPortal(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.PortalOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.PortalOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.PortalOverlay.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.PortalOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.PortalOverlay.opacity = v / 100f).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.portal.speed"), Math.round(ConfigInstance.PortalOverlay.speed * 100), 0, 1000)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.PortalOverlay.speed = v / 100f).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.portal.allowGuisInPortal"), ConfigInstance.PortalOverlay.allowGuisInPortal)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.portal.allowCameraShake"), ConfigInstance.PortalOverlay.allowCameraShake)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PortalOverlay.allowCameraShake = v).build());
        return eb.startSubCategory(Component.translatable("config.hudified.portal"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildFreeze(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.FreezeOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.FreezeOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.FreezeOverlay.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.FreezeOverlay.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.opacity = v / 100f).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.freeze.xScale"), Math.round(ConfigInstance.FreezeOverlay.Xscale * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Xscale = v / 100f).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.freeze.yScale"), Math.round(ConfigInstance.FreezeOverlay.Yscale * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.FreezeOverlay.Yscale = v / 100f).build());
        return eb.startSubCategory(Component.translatable("config.hudified.freeze"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildBlindness(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.BlindnessOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BlindnessOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.BlindnessOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.BlindnessOverlay.toggleKeybind = v).build());

        return eb.startSubCategory(Component.translatable("config.hudified.blindness"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildDarkness(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.DarknessOverlay.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.DarknessOverlay.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.DarknessOverlay.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.DarknessOverlay.toggleKeybind = v).build());

        return eb.startSubCategory(Component.translatable("config.hudified.darkness"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildVignette(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.Vignette.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Vignette.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Vignette.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Vignette.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.Vignette.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.Vignette.opacity = v / 100f).build());
        return eb.startSubCategory(Component.translatable("config.hudified.vignette"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildBossBar(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.BossBar.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.BossBar.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.BossBar.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.BossBar.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.bossBar.xOffset"), ConfigInstance.BossBar.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.BossBar.XOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.bossBar.yOffset"), ConfigInstance.BossBar.YOffset, -1000, 1000)
                .setDefaultValue(12).setSaveConsumer(v -> ConfigInstance.BossBar.YOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scale"), Math.round(ConfigInstance.BossBar.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.BossBar.scale = v / 100f).build());
        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.editBossBarLayout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().gui.screen();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.BOSS_BAR)
                    );
                }
        ));
        return eb.startSubCategory(Component.translatable("config.hudified.bossBar"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildScoreboard(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.Scoreboard.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Scoreboard.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Scoreboard.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Scoreboard.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scoreboard.xOffset"), ConfigInstance.Scoreboard.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.Scoreboard.XOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scoreboard.yOffset"), ConfigInstance.Scoreboard.YOffset, -1000, 1000)
                .setDefaultValue(12).setSaveConsumer(v -> ConfigInstance.Scoreboard.YOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scale"), Math.round(ConfigInstance.Scoreboard.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.Scoreboard.scale = v / 100f).build());
        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.editScoreboardLayout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().gui.screen();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.SCOREBOARD)
                    );
                }
        ));
        return eb.startSubCategory(Component.translatable("config.hudified.scoreboard"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildTotem(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.Totem.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Totem.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Totem.toggleKeybind = v).build());

        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.totem.showAnimation"), ConfigInstance.Totem.showTotemAnimation)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showTotemAnimation = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.totem.showParticles"), ConfigInstance.Totem.showParticles)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Totem.showParticles = v).build());
        return eb.startSubCategory(Component.translatable("config.hudified.totem"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildAttackIndicator(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.AttackIndicator.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.AttackIndicator.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.AttackIndicator.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.AttackIndicator.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.attackIndicator.xOffset"), ConfigInstance.AttackIndicator.XOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.XOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.attackIndicator.yOffset"), ConfigInstance.AttackIndicator.YOffset, -1000, 1000)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.AttackIndicator.YOffset = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scale"), Math.round(ConfigInstance.AttackIndicator.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.AttackIndicator.scale = v / 100f).build());
        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.editAttackIndicatorLayout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().gui.screen();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.ATTACK_INDICATOR)
                    );
                }
        ));
        return eb.startSubCategory(Component.translatable("config.hudified.attackIndicator"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildprojectileHighlight(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.ProjectileHighlight.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.ProjectileHighlight.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.ProjectileHighlight.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.ProjectileHighlight.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.projectileHighlight.red"), ConfigInstance.ProjectileHighlight.red, 0, 255)
                .setDefaultValue(0).setSaveConsumer(v -> ConfigInstance.ProjectileHighlight.red = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.projectileHighlight.green"), ConfigInstance.ProjectileHighlight.green, 0, 255)
                .setDefaultValue(158).setSaveConsumer(v -> ConfigInstance.ProjectileHighlight.green = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.projectileHighlight.blue"), ConfigInstance.ProjectileHighlight.blue, 0, 255)
                .setDefaultValue(166).setSaveConsumer(v -> ConfigInstance.ProjectileHighlight.blue = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.opacity"), Math.round(ConfigInstance.ProjectileHighlight.opacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.ProjectileHighlight.opacity = v / 100f).build());

        entries.add(eb.startStrList(
                        Component.translatable("config.hudified.projectileHighlight.whitelisted"),
                        ConfigInstance.ProjectileHighlight.supportedProjectiles)
                .setDefaultValue(new java.util.ArrayList<>(java.util.List.of(
                        "minecraft:arrow",
                        "minecraft:spectral_arrow",
                        "minecraft:snowball",
                        "minecraft:egg",
                        "minecraft:ender_pearl",
                        "minecraft:potion",
                        "minecraft:trident",
                        "minecraft:fireball",
                        "minecraft:small_fireball",
                        "minecraft:shulker_bullet"
                )))
                .setTooltip(
                        Component.translatable("config.hudified.projectileHighlight.whitelisted.tooltip"),
                        Component.literal(" "),
                        Component.literal("§eFormatting Tips:"),
                        Component.literal("§7- Vanilla: Type 'arrow'"),
                        Component.literal("§7- Modded: Type 'tonk' (Auto-detects namespace)"),
                        Component.literal("§7- Specific: Type 'blurk:tonk' if multiple mods use the same name.")
                )
                .setSaveConsumer(v -> {
                    java.util.List<String> formattedList = new java.util.ArrayList<>();
                    for (String str : v) {
                        if (str.contains(":")) {
                            formattedList.add(str);
                            continue;
                        }

                        // --- SMART NAMESPACE DETECTOR ---
                        String foundId = null;
                        int matchCount = 0;
                        for (net.minecraft.resources.Identifier id : net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.keySet()) {
                            if (id.getPath().equals(str)) {
                                foundId = id.toString();
                                matchCount++;
                            }
                        }

                        // If exactly ONE mod adds a projectile with this name, use its namespace!
                        if (matchCount == 1) {
                            formattedList.add(foundId);
                        } else {
                            // If 0 found (mod not loaded) or multiple found (conflict), default to minecraft:
                            formattedList.add("minecraft:" + str);
                        }
                    }
                    ConfigInstance.ProjectileHighlight.supportedProjectiles = formattedList;
                })
                .setCellErrorSupplier(str -> {
                    String formattedStr = str;

                    if (!str.contains(":")) {
                        int matchCount = 0;
                        String foundId = null;
                        for (net.minecraft.resources.Identifier id : net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.keySet()) {
                            if (id.getPath().equals(str)) {
                                foundId = id.toString();
                                matchCount++;
                            }
                        }
                        formattedStr = (matchCount == 1) ? foundId : "minecraft:" + str;
                    }

                    net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(formattedStr);

                    // We still allow it to save even if the mod isn't currently loaded,
                    // we just verify it has legal characters.
                    if (id == null) {
                        return java.util.Optional.of(net.minecraft.network.chat.Component.translatable("config.hudified.generic.invalidEntity"));
                    }

                    return java.util.Optional.empty();
                })
                .build());

        return eb.startSubCategory(Component.translatable("config.hudified.projectileHighlight"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildPieChart(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.PieChart.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.PieChart.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.PieChart.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.PieChart.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(Component.translatable("config.hudified.pieChart.x"), ConfigInstance.PieChart.x, -1, 2000)
                .setDefaultValue(-1).setTextGetter(val -> val == -1 ? Component.translatable("config.hudified.generic.auto") : Component.literal(String.valueOf(val))).setSaveConsumer(v -> ConfigInstance.PieChart.x = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.pieChart.y"), ConfigInstance.PieChart.y, -1, 2000)
                .setDefaultValue(500).setTextGetter(val -> val == -1 ? Component.translatable("config.hudified.generic.auto") : Component.literal(String.valueOf(val))).setSaveConsumer(v -> ConfigInstance.PieChart.y = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.scale"), Math.round(ConfigInstance.PieChart.scale * 100), 10, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.PieChart.scale = v / 100f).build());
        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.editPieChartLayout"),
                () -> {
                    Screen currentScreen = net.minecraft.client.Minecraft.getInstance().gui.screen();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(
                            new LayoutEditorScreen(currentScreen, LayoutEditorScreen.EditMode.PIE_CHART)
                    );
                }
        ));
        return eb.startSubCategory(Component.translatable("config.hudified.pieChart"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildEnvironment(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        // Environment doesn't have a single "enabled" toggle in your config, so no keybind is added here.
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.fullbright"), ConfigInstance.Environment.fullbright)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.fullbright = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.disableFog"), ConfigInstance.Environment.disableFog)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.disableFog = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.environment.fogMultiplier"), Math.round(ConfigInstance.Environment.fogMultiplier * 100), 0, 1000)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> ConfigInstance.Environment.fogMultiplier = v / 100f).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.clearLava"), ConfigInstance.Environment.clearLava)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearLava = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.clearWater"), ConfigInstance.Environment.clearWater)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.clearWater = v).build());
        entries.add(eb.startIntSlider(Component.translatable("config.hudified.environment.rainOpacity"), Math.round(ConfigInstance.Environment.rainOpacity * 100), 0, 100)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(val + "%")).setSaveConsumer(v -> ConfigInstance.Environment.rainOpacity = v / 100f).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.noRainParticles"), ConfigInstance.Environment.noRainParticles)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noRainParticles = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.noSnow"), ConfigInstance.Environment.noSnow)
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.noSnow = v).build());
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.environment.blockBreaking"), ConfigInstance.Environment.blockBreakingOverlay).setTooltip(Component.translatable("config.hudified.environment.blockBreaking.tooltip"))
                .setDefaultValue(false).setSaveConsumer(v -> ConfigInstance.Environment.blockBreakingOverlay = v).build());
        return eb.startSubCategory(Component.translatable("config.hudified.category.environmentGameplay"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildBoat(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.enabled"), ConfigInstance.Boat.enabled)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.enabled = v).build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Boat.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Boat.toggleKeybind = v).build());

        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.boat.showBoatItem"), ConfigInstance.Boat.showBoatItem)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.showBoatItem = v).build());

        entries.add(eb.startBooleanToggle(Component.translatable("config.hudified.boat.unlockBoatPov"), ConfigInstance.Boat.unlockBoatPov)
                .setDefaultValue(true).setSaveConsumer(v -> ConfigInstance.Boat.unlockBoatPov = v).build());
        return eb.startSubCategory(Component.translatable("config.hudified.category.boat"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildParticles(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();

        entries.add(eb.startBooleanToggle(
                        Component.translatable("config.hudified.particles.enabled"),
                        ConfigInstance.Particle.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Particle.enabled = v)
                .build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Particle.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Particle.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(
                        Component.translatable("config.hudified.particles.globalChance"),
                        Math.round(ConfigInstance.Particle.globalChance * 100), 0, 100)
                .setDefaultValue(100)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.hudified.particles.globalChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.globalChance = v / 100f)
                .build());

        entries.add(eb.startIntSlider(
                        Component.translatable("config.hudified.particles.selfPotionChance"),
                        Math.round(ConfigInstance.Particle.selfPotionChance * 100), 0, 100)
                .setDefaultValue(50)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.hudified.particles.selfPotionChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.selfPotionChance = v / 100f)
                .build());

        entries.add(eb.startIntSlider(
                        Component.translatable("config.hudified.particles.otherPotionChance"),
                        Math.round(ConfigInstance.Particle.otherPotionChance * 100), 0, 100)
                .setDefaultValue(100)
                .setTextGetter(val -> Component.literal(val + "%"))
                .setTooltip(Component.translatable("config.hudified.particles.otherPotionChance.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Particle.otherPotionChance = v / 100f)
                .build());

        List<AbstractConfigListEntry> particleSliders = new ArrayList<>();

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
                    .setTextGetter(val -> val == -1 ? Component.translatable("config.hudified.generic.default") : Component.literal(val + "%"))
                    .setTooltip(Component.translatable("config.hudified.particles.specific.tooltip"))
                    .setSaveConsumer(v -> {
                        if (v == -1) {
                            ConfigInstance.Particle.customParticleChances.remove(id);
                        } else {
                            ConfigInstance.Particle.customParticleChances.put(id, v / 100f);
                        }
                    })
                    .build());
        }

        entries.add(eb.startSubCategory(
                        Component.translatable("config.hudified.particles.specific"),
                        particleSliders)
                .setExpanded(false)
                .build());

        return eb.startSubCategory(Component.translatable("config.hudified.category.particles"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildShields(ConfigEntryBuilder eb, Screen parent) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();

        entries.add(eb.startBooleanToggle(
                        Component.translatable("config.hudified.enabled"),
                        ConfigInstance.Shields.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.Shields.enabled = v)
                .build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.Shields.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.Shields.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(
                        Component.translatable("config.hudified.shields.simpleHeight"),
                        ConfigInstance.Shields.simpleYOffset, -500, 500)
                .setDefaultValue(0)
                .setTooltip(Component.translatable("config.hudified.shields.simpleHeight.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.Shields.simpleYOffset = v)
                .build());

        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.applySideShieldPreset"),
                () -> {
                    applyMrOrdenadorPresets();
                    ConfigManager.save();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(ConfigScreenFactory.create(parent));
                }
        ));

        entries.add(new ButtonEntry(
                Component.empty(),
                Component.translatable("config.hudified.button.applyDefaultShieldSettings"),
                () -> {
                    ConfigManager.save();
                    applyVanillaOptions();
                    net.minecraft.client.Minecraft.getInstance().setScreenAndShow(ConfigScreenFactory.create(parent));
                }
        ));

        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.firstPersonMain"), ConfigInstance.Shields.firstPersonMain));
        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.firstPersonOff"), ConfigInstance.Shields.firstPersonOff));
        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.thirdPersonMain"), ConfigInstance.Shields.thirdPersonMain));
        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.thirdPersonOff"), ConfigInstance.Shields.thirdPersonOff));
        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.otherPlayersMain"), ConfigInstance.Shields.otherPlayersMain));
        entries.add(handSettingsSubCategory(eb, Component.translatable("config.hudified.shields.otherPlayersOff"), ConfigInstance.Shields.otherPlayersOff));

        return eb.startSubCategory(Component.translatable("config.hudified.category.shields"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry buildDroppedItems(ConfigEntryBuilder eb) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();

        entries.add(eb.startBooleanToggle(
                        Component.translatable("config.hudified.enabled"),
                        ConfigInstance.DroppedItems.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.enabled = v)
                .build());

        entries.add(eb.startKeyCodeField(Component.translatable("config.hudified.toggleKeybind"), ConfigInstance.DroppedItems.toggleKeybind)
                .setDefaultValue(InputConstants.UNKNOWN).setKeySaveConsumer(v -> ConfigInstance.DroppedItems.toggleKeybind = v).build());

        entries.add(eb.startIntSlider(
                        Component.translatable("config.hudified.droppedItems.scale"),
                        Math.round(ConfigInstance.DroppedItems.customScale * 100), 10, 1000)
                .setDefaultValue(300)
                .setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f)))
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.customScale = v / 100f)
                .build());

        entries.add(eb.startStrList(
                        Component.translatable("config.hudified.droppedItems.list"),
                        ConfigInstance.DroppedItems.itemList)
                .setDefaultValue(List.of("minecraft:golden_apple"))
                .setTooltip(Component.translatable("config.hudified.droppedItems.list.tooltip"))
                .setSaveConsumer(v -> ConfigInstance.DroppedItems.itemList = v)
                .setCellErrorSupplier(str -> {
                    net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(str);

                    if (id == null || !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(id)) {
                        return Optional.of(net.minecraft.network.chat.Component.translatable("config.hudified.generic.invalidItem"));
                    }

                    return java.util.Optional.empty();
                })
                .build());
        return eb.startSubCategory(Component.translatable("config.hudified.category.droppedItems"), entries).setExpanded(false).build();
    }

    private static SubCategoryListEntry handSettingsSubCategory(
            ConfigEntryBuilder eb,
            Component label,
            ConfigInstance.HandSettings hand) {

        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(eb.startTextDescription(Component.translatable("config.hudified.shields.pose.idle")).build());
        addShieldSettingsEntries(entries, eb, hand.idle);
        entries.add(eb.startTextDescription(Component.translatable("config.hudified.shields.pose.blocking")).build());
        addShieldSettingsEntries(entries, eb, hand.blocking);

        SubCategoryBuilder sub = eb.startSubCategory(label, entries);
        sub.setExpanded(false);
        return sub.build();
    }

    private static void addShieldSettingsEntries(
            List<AbstractConfigListEntry> list,
            ConfigEntryBuilder eb,
            ConfigInstance.ShieldSettings s) {

        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.xOffset"), (int) Math.round(s.xOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.xOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.yOffset"), (int) Math.round(s.yOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.yOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.zOffset"), (int) Math.round(s.zOffset * 10), -1000, 1000)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(String.format("%.1f", val / 10.0))).setSaveConsumer(v -> s.zOffset = v / 10.0).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.scaleX"), Math.round(s.scaleX * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleX = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.scaleY"), Math.round(s.scaleY * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleY = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.scaleZ"), Math.round(s.scaleZ * 100), 0, 500)
                .setDefaultValue(100).setTextGetter(val -> Component.literal(String.format("%.2fx", val / 100f))).setSaveConsumer(v -> s.scaleZ = v / 100f).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.rotX"), Math.round(s.rotX), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotX = (float) v).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.rotY"), Math.round(s.rotY), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotY = (float) v).build());
        list.add(eb.startIntSlider(Component.translatable("config.hudified.shields.rotZ"), Math.round(s.rotZ), -180, 180)
                .setDefaultValue(0).setTextGetter(val -> Component.literal(val + "°")).setSaveConsumer(v -> s.rotZ = (float) v).build());
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
        public void extractRenderState(GuiGraphicsExtractor guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
            this.button.setX(x + entryWidth / 2 - this.button.getWidth() / 2);
            this.button.setY(y);
            this.button.extractRenderState(guiGraphics, mouseX, mouseY, delta);
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

    // =========================================================================
    // INJECTED KEYBIND LOGIC
    // =========================================================================

    private static final java.util.Set<InputConstants.Key> previousKeys = new java.util.HashSet<>();
    private static final java.util.Set<InputConstants.Key> currentKeys = new java.util.HashSet<>();

    public static void registerKeybinds() {
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.gui.screen() != null) {
                previousKeys.clear();
                currentKeys.clear();
                return;
            }

            final boolean[] configChanged = {false};

            // 2. FIX: Using your exact mapping (client.getWindow())
            java.util.function.Predicate<InputConstants.Key> justPressed = (key) -> {
                if (key == null || key == InputConstants.UNKNOWN) return false;

                boolean isDown = InputConstants.isKeyDown(client.getWindow(), key.getValue());
                boolean wasDown = previousKeys.contains(key);

                if (isDown) currentKeys.add(key);

                return isDown && !wasDown;
            };

            // 3. FIX: Main Keybind now toggles the Master Boolean instead of opening the menu
            if (justPressed.test(ConfigInstance.menuKeybind)) {
                ConfigInstance.OverlayEnabled = !ConfigInstance.OverlayEnabled;
                configChanged[0] = true;
            }

            // Category Toggles
            if (justPressed.test(ConfigInstance.PumpkinOverlay.toggleKeybind)) { ConfigInstance.PumpkinOverlay.enabled = !ConfigInstance.PumpkinOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.FireOverlay.toggleKeybind)) { ConfigInstance.FireOverlay.enabled = !ConfigInstance.FireOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.SpyglassOverlay.toggleKeybind)) { ConfigInstance.SpyglassOverlay.enabled = !ConfigInstance.SpyglassOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.PortalOverlay.toggleKeybind)) { ConfigInstance.PortalOverlay.enabled = !ConfigInstance.PortalOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.FreezeOverlay.toggleKeybind)) { ConfigInstance.FreezeOverlay.enabled = !ConfigInstance.FreezeOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.BlindnessOverlay.toggleKeybind)) { ConfigInstance.BlindnessOverlay.enabled = !ConfigInstance.BlindnessOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.DarknessOverlay.toggleKeybind)) { ConfigInstance.DarknessOverlay.enabled = !ConfigInstance.DarknessOverlay.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Vignette.toggleKeybind)) { ConfigInstance.Vignette.enabled = !ConfigInstance.Vignette.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.BossBar.toggleKeybind)) { ConfigInstance.BossBar.enabled = !ConfigInstance.BossBar.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Scoreboard.toggleKeybind)) { ConfigInstance.Scoreboard.enabled = !ConfigInstance.Scoreboard.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Totem.toggleKeybind)) { ConfigInstance.Totem.enabled = !ConfigInstance.Totem.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.AttackIndicator.toggleKeybind)) { ConfigInstance.AttackIndicator.enabled = !ConfigInstance.AttackIndicator.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.ProjectileHighlight.toggleKeybind)) { ConfigInstance.ProjectileHighlight.enabled = !ConfigInstance.ProjectileHighlight.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.PieChart.toggleKeybind)) { ConfigInstance.PieChart.enabled = !ConfigInstance.PieChart.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Boat.toggleKeybind)) { ConfigInstance.Boat.enabled = !ConfigInstance.Boat.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Particle.toggleKeybind)) { ConfigInstance.Particle.enabled = !ConfigInstance.Particle.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.Shields.toggleKeybind)) { ConfigInstance.Shields.enabled = !ConfigInstance.Shields.enabled; configChanged[0] = true; }
            if (justPressed.test(ConfigInstance.DroppedItems.toggleKeybind)) { ConfigInstance.DroppedItems.enabled = !ConfigInstance.DroppedItems.enabled; configChanged[0] = true; }

            // Cleanup & Saving
            previousKeys.clear();
            previousKeys.addAll(currentKeys);
            currentKeys.clear();

            if (configChanged[0]) ConfigManager.save();
        });
    }
}