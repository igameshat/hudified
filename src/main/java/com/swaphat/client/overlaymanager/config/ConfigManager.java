package com.swaphat.client.overlaymanager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(InputConstants.Key.class, new TypeAdapter<InputConstants.Key>() {
                @Override
                public void write(JsonWriter out, InputConstants.Key value) throws IOException {
                    out.value(value != null ? value.getName() : InputConstants.UNKNOWN.getName());
                }

                @Override
                public InputConstants.Key read(JsonReader in) throws IOException {
                    return InputConstants.getKey(in.nextString());
                }
            })
            .create();

    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve("overlay-manager.json").toFile();

    private static class ConfigMirror {
        public boolean OverlayEnabled;
        public InputConstants.Key menuKeybind;

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
        public ConfigInstance.ProjectileHighlight ProjectileHighlight;
        public ConfigInstance.AttackIndicator AttackIndicator;
        public ConfigInstance.PieChart PieChart;
        public ConfigInstance.Boat Boat;
        public ConfigInstance.ShieldConfig Shields;
        public ConfigInstance.ParticleConfig Particle;
        public ConfigInstance.DroppedItems DroppedItems;
    }

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                ConfigMirror data = GSON.fromJson(reader, ConfigMirror.class);

                if (data != null) {
                    ConfigInstance.OverlayEnabled = data.OverlayEnabled;
                    if (data.menuKeybind != null) ConfigInstance.menuKeybind = data.menuKeybind;

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
                    if (data.ProjectileHighlight != null) ConfigInstance.ProjectileHighlight = data.ProjectileHighlight;
                    if (data.AttackIndicator != null) ConfigInstance.AttackIndicator = data.AttackIndicator;
                    if (data.PieChart != null) ConfigInstance.PieChart = data.PieChart;
                    if (data.Boat != null) ConfigInstance.Boat = data.Boat;
                    if (data.Shields != null) ConfigInstance.Shields = data.Shields;
                    if (data.Particle != null) ConfigInstance.Particle = data.Particle;
                    if (data.DroppedItems != null) ConfigInstance.DroppedItems = data.DroppedItems;
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
            data.menuKeybind = ConfigInstance.menuKeybind;

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
            data.ProjectileHighlight = ConfigInstance.ProjectileHighlight;
            data.AttackIndicator = ConfigInstance.AttackIndicator;
            data.PieChart = ConfigInstance.PieChart;
            data.Boat = ConfigInstance.Boat;
            data.Shields = ConfigInstance.Shields;
            data.Particle = ConfigInstance.Particle;
            data.DroppedItems = ConfigInstance.DroppedItems;

            GSON.toJson(data, writer);

        } catch (IOException e) {
            System.err.println("Failed to save config!");
            e.printStackTrace();
        }
    }

    public static class ConfigHelper {
        public static float pixelsToFloat(float pixels) {
            float screenHeight = (float) net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledHeight();
            return screenHeight <= 0 ? 0 : pixels / screenHeight;
        }
    }
}