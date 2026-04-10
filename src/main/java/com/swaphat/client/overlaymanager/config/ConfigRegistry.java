package com.swaphat.client.overlaymanager.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigRegistry {
    public static final List<ConfigOption<?>> ALL_OPTIONS = new ArrayList<>();

    private static final int COLOR_TWEAK = 0xFF51A2CE;
    private static final int COLOR_VISIBILITY  = 0xFFE53935;

    static {
        // ─── GENERAL (Blue) ──────────────────────────────────────────────────
        registerBool("overlay_enabled", "Master Overlay Enabled", "General", COLOR_TWEAK,
                () -> ConfigInstance.OverlayEnabled, v -> ConfigInstance.OverlayEnabled = v);

        // ─── TWEAK MANAGER OPTIONS (Blue Labels) ────────────────────────────

        // Pumpkin Overlay
        registerBool("pumpkin_enabled", "Enabled", "Pumpkin Overlay", COLOR_TWEAK,
                () -> ConfigInstance.PumpkinOverlay.enabled, v -> ConfigInstance.PumpkinOverlay.enabled = v);
        registerSlider("pumpkin_opacity", "Opacity", "Pumpkin Overlay", COLOR_TWEAK, 0, 255,
                () -> ConfigInstance.PumpkinOverlay.opacity, v -> ConfigInstance.PumpkinOverlay.opacity = v);

        // Fire Overlay
        registerBool("fire_enabled", "Enabled", "Fire Overlay", COLOR_TWEAK,
                () -> ConfigInstance.FireOverlay.enabled, v -> ConfigInstance.FireOverlay.enabled = v);
        registerSlider("fire_offset", "Offset Pixels", "Fire Overlay", COLOR_TWEAK, 0, 500,
                () -> ConfigInstance.FireOverlay.offsetPixels, v -> ConfigInstance.FireOverlay.offsetPixels = v);
        registerSlider("fire_opacity", "Opacity", "Fire Overlay", COLOR_TWEAK, 0, 255,
                () -> ConfigInstance.FireOverlay.opacity, v -> ConfigInstance.FireOverlay.opacity = v);

        // Spyglass Overlay
        registerBool("spyglass_enabled", "Enabled", "Spyglass Overlay", COLOR_TWEAK,
                () -> ConfigInstance.SpyglassOverlay.enabled, v -> ConfigInstance.SpyglassOverlay.enabled = v);
        registerSlider("spyglass_scale", "Scale", "Spyglass Overlay", COLOR_TWEAK, 0, 2,
                () -> ConfigInstance.SpyglassOverlay.scale, v -> ConfigInstance.SpyglassOverlay.scale = v);

        // Portal Overlay
        registerBool("portal_enabled", "Enabled", "Portal Overlay", COLOR_TWEAK,
                () -> ConfigInstance.PortalOverlay.enabled, v -> ConfigInstance.PortalOverlay.enabled = v);
        registerSlider("portal_opacity", "Opacity", "Portal Overlay", COLOR_TWEAK, 0, 255,
                () -> ConfigInstance.PortalOverlay.opacity, v -> ConfigInstance.PortalOverlay.opacity = v);
        registerSlider("portal_speed", "Speed", "Portal Overlay", COLOR_TWEAK, 0, 5,
                () -> ConfigInstance.PortalOverlay.speed, v -> ConfigInstance.PortalOverlay.speed = v);
        registerBool("portal_gui", "Allow GUIs in Portal", "Portal Overlay", COLOR_TWEAK,
                () -> ConfigInstance.PortalOverlay.allowGuisInPortal, v -> ConfigInstance.PortalOverlay.allowGuisInPortal = v);
        registerBool("portal_shake", "Allow Camera Shake", "Portal Overlay", COLOR_TWEAK,
                () -> ConfigInstance.PortalOverlay.allowCameraShake, v -> ConfigInstance.PortalOverlay.allowCameraShake = v);

        // Freeze Overlay
        registerBool("freeze_enabled", "Enabled", "Freeze Overlay", COLOR_TWEAK,
                () -> ConfigInstance.FreezeOverlay.enabled, v -> ConfigInstance.FreezeOverlay.enabled = v);
        registerSlider("freeze_opacity", "Opacity", "Freeze Overlay", COLOR_TWEAK, 0, 255,
                () -> (float)ConfigInstance.FreezeOverlay.opacity, v -> ConfigInstance.FreezeOverlay.opacity = Math.round(v));
        registerSlider("freeze_scale", "Scale", "Freeze Overlay", COLOR_TWEAK, 0, 2,
                () -> ConfigInstance.FreezeOverlay.scale, v -> ConfigInstance.FreezeOverlay.scale = v);

        // Effects Overlays
        registerBool("blindness_enabled", "Enabled", "Blindness Overlay", COLOR_TWEAK,
                () -> ConfigInstance.BlindnessOverlay.enabled, v -> ConfigInstance.BlindnessOverlay.enabled = v);
        registerBool("blindness_slow", "Enable Slowdown", "Blindness Overlay", COLOR_TWEAK,
                () -> ConfigInstance.BlindnessOverlay.enableSlowdown, v -> ConfigInstance.BlindnessOverlay.enableSlowdown = v);
        registerBool("darkness_enabled", "Enabled", "Darkness Overlay", COLOR_TWEAK,
                () -> ConfigInstance.DarknessOverlay.enabled, v -> ConfigInstance.DarknessOverlay.enabled = v);

        // GUI Components
        registerBool("vignette_enabled", "Enabled", "Vignette", COLOR_TWEAK,
                () -> ConfigInstance.Vignette.enabled, v -> ConfigInstance.Vignette.enabled = v);
        registerSlider("vignette_opacity", "Opacity", "Vignette", COLOR_TWEAK, 0, 255,
                () -> ConfigInstance.Vignette.opacity, v -> ConfigInstance.Vignette.opacity = v);

        registerBool("bossbar_enabled", "Enabled", "BossBar", COLOR_TWEAK,
                () -> ConfigInstance.BossBar.enabled, v -> ConfigInstance.BossBar.enabled = v);
        registerSlider("bossbar_offset", "Y Offset", "BossBar", COLOR_TWEAK, 0, 100,
                () -> (float)ConfigInstance.BossBar.bossBarYOffset, v -> ConfigInstance.BossBar.bossBarYOffset = Math.round(v));
        registerSlider("bossbar_scale", "Scale", "BossBar", COLOR_TWEAK, 0, 2,
                () -> ConfigInstance.BossBar.scale, v -> ConfigInstance.BossBar.scale = v);

        registerBool("scoreboard_enabled", "Enabled", "Scoreboard", COLOR_TWEAK,
                () -> ConfigInstance.Scoreboard.enabled, v -> ConfigInstance.Scoreboard.enabled = v);

        registerBool("totem_enabled", "Enabled", "Totem", COLOR_TWEAK,
                () -> ConfigInstance.Totem.enabled, v -> ConfigInstance.Totem.enabled = v);
        registerBool("totem_anim", "Show Animation", "Totem", COLOR_TWEAK,
                () -> ConfigInstance.Totem.showTotemAnimation, v -> ConfigInstance.Totem.showTotemAnimation = v);
        registerBool("totem_parts", "Show Particles", "Totem", COLOR_TWEAK,
                () -> ConfigInstance.Totem.showParticles, v -> ConfigInstance.Totem.showParticles = v);

        // ─── VISIBILITY OPTIONS (Red Labels) ────────────────────────────────

        registerBool("fullbright", "Fullbright", "Lighting", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.fullbright, v -> ConfigInstance.Environment.fullbright = v);

        registerBool("disable_fog", "Disable Fog Completely", "Fog", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.disableFog, v -> ConfigInstance.Environment.disableFog = v);
        registerSlider("fog_multiplier", "Fog Multiplier", "Fog", COLOR_VISIBILITY, 0.1f, 5.0f,
                () -> ConfigInstance.Environment.fogMultiplier, v -> ConfigInstance.Environment.fogMultiplier = v);
        registerBool("clear_lava", "Clear Lava", "Fog", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.clearLava, v -> ConfigInstance.Environment.clearLava = v);
        registerBool("clear_water", "Clear Water", "Fog", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.clearWater, v -> ConfigInstance.Environment.clearWater = v);

        registerSlider("rain_opacity", "Rain Opacity", "Weather", COLOR_VISIBILITY, 0, 1,
                () -> ConfigInstance.Environment.rainOpacity, v -> ConfigInstance.Environment.rainOpacity = v);
        registerBool("no_rain_parts", "Disable Rain Particles", "Weather", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.noRainParticles, v -> ConfigInstance.Environment.noRainParticles = v);
        registerBool("no_snow", "Disable Snow", "Weather", COLOR_VISIBILITY,
                () -> ConfigInstance.Environment.noSnow, v -> ConfigInstance.Environment.noSnow = v);
    }

    private static void registerBool(String id, String name, String category, int col, java.util.function.Supplier<Boolean> g, java.util.function.Consumer<Boolean> s) {
        ALL_OPTIONS.add(new ConfigOption<>(id, name, category, col, g, s));
    }

    private static void registerSlider(String id, String name, String cat, int col, float min, float max, java.util.function.Supplier<Float> g, java.util.function.Consumer<Float> s) {
        ALL_OPTIONS.add(new ConfigOption<>(id, name, cat, col, min, max, g, s));
    }
}