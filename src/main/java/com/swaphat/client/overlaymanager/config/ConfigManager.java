package com.swaphat.client.overlaymanager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve("overlay-manager.json").toFile();

    private static class ConfigMirror {
        public boolean OverlayEnabled;

        public ConfigInstance.PumpkinOverlay PumpkinOverlay;
        public ConfigInstance.FireOverlay FireOverlay;
        public ConfigInstance.SpyglassOverlay SpyglassOverlay;
        public ConfigInstance.PortalOverlay PortalOverlay;
        public ConfigInstance.FreezeOverlay FreezeOverlay;
        public ConfigInstance.BlindnessOverlay BlindnessOverlay;
        public ConfigInstance.DarknessOverlay DarknessOverlay;
        public ConfigInstance.Vignette Vignette;
        public ConfigInstance.BossBar BossBar;
        public ConfigInstance.Scoreboard Scoreboard;
        public ConfigInstance.Totem Totem;
        public ConfigInstance.Environment Environment;
        public ConfigInstance.ArrowHighlight ArrowHighlight;
        public ConfigInstance.AttackIndicator AttackIndicator;

    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                ConfigMirror data = GSON.fromJson(reader, ConfigMirror.class);

                if (data != null) {ConfigInstance.OverlayEnabled = data.OverlayEnabled;

                    if (data.PumpkinOverlay != null) ConfigInstance.PumpkinOverlay = data.PumpkinOverlay;
                    if (data.FireOverlay != null) ConfigInstance.FireOverlay = data.FireOverlay;
                    if (data.SpyglassOverlay != null) ConfigInstance.SpyglassOverlay = data.SpyglassOverlay;
                    if (data.PortalOverlay != null) ConfigInstance.PortalOverlay = data.PortalOverlay;
                    if (data.FreezeOverlay != null) ConfigInstance.FreezeOverlay = data.FreezeOverlay;
                    if (data.BlindnessOverlay != null) ConfigInstance.BlindnessOverlay = data.BlindnessOverlay;
                    if (data.DarknessOverlay != null) ConfigInstance.DarknessOverlay = data.DarknessOverlay;
                    if (data.Vignette != null) ConfigInstance.Vignette = data.Vignette;
                    if (data.BossBar != null) ConfigInstance.BossBar = data.BossBar;
                    if (data.Scoreboard != null) ConfigInstance.Scoreboard = data.Scoreboard;
                    if (data.Totem != null) ConfigInstance.Totem = data.Totem;
                    if (data.Environment != null) ConfigInstance.Environment = data.Environment;
                    if (data.ArrowHighlight != null) ConfigInstance.ArrowHighlight = data.ArrowHighlight;
                    if (data.AttackIndicator != null) ConfigInstance.AttackIndicator = data.AttackIndicator;
                }
            } catch (Exception e) {
                System.err.println("Failed to load config!");
                e.printStackTrace();
            }
        }

        save(); // keep this
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            ConfigMirror data = new ConfigMirror();

            data.OverlayEnabled = ConfigInstance.OverlayEnabled;

            data.PumpkinOverlay = ConfigInstance.PumpkinOverlay;
            data.FireOverlay = ConfigInstance.FireOverlay;
            data.SpyglassOverlay = ConfigInstance.SpyglassOverlay;
            data.PortalOverlay = ConfigInstance.PortalOverlay;
            data.FreezeOverlay = ConfigInstance.FreezeOverlay;
            data.BlindnessOverlay = ConfigInstance.BlindnessOverlay;
            data.DarknessOverlay = ConfigInstance.DarknessOverlay;
            data.Vignette = ConfigInstance.Vignette;
            data.BossBar = ConfigInstance.BossBar;
            data.Scoreboard = ConfigInstance.Scoreboard;
            data.Totem = ConfigInstance.Totem;
            data.Environment = ConfigInstance.Environment;
            data.ArrowHighlight = ConfigInstance.ArrowHighlight;
            data.AttackIndicator = ConfigInstance.AttackIndicator;

            GSON.toJson(data, writer);

        } catch (IOException e) {
            System.err.println("Failed to save config!");
            e.printStackTrace();
        }
    }

    public static class ConfigHelper {
        public static float pixelsToFloat(float pixels) {
            float screenHeight = (float) net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledHeight();
            if (screenHeight <= 0) return 0.0f;
            return pixels / screenHeight;
        }
    }
}