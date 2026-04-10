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
    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                ConfigMirror data = GSON.fromJson(reader, ConfigMirror.class);

                if (data != null) {
                    ConfigInstance.OverlayEnabled = data.OverlayEnabled;

                    ConfigInstance.PumpkinOverlay = data.PumpkinOverlay;
                    ConfigInstance.FireOverlay = data.FireOverlay;
                    ConfigInstance.SpyglassOverlay = data.SpyglassOverlay;
                    ConfigInstance.PortalOverlay = data.PortalOverlay;
                    ConfigInstance.FreezeOverlay = data.FreezeOverlay;
                    ConfigInstance.BlindnessOverlay = data.BlindnessOverlay;
                    ConfigInstance.DarknessOverlay = data.DarknessOverlay;
                    ConfigInstance.Vignette = data.Vignette;
                    ConfigInstance.BossBar = data.BossBar;
                    ConfigInstance.Scoreboard = data.Scoreboard;
                    ConfigInstance.Totem = data.Totem;
                }
            } catch (Exception e) {
                System.err.println("Failed to load config!");
                e.printStackTrace();
            }
        }

        save();
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