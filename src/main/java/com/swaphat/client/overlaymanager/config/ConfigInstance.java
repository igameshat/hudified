package com.swaphat.client.overlaymanager.config;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.ArrayList;
import java.util.List;

public class ConfigInstance {
    public static boolean OverlayEnabled = true;
    public static InputConstants.Key menuKeybind = InputConstants.UNKNOWN;

    public static PumpkinOverlay PumpkinOverlay = new PumpkinOverlay();
    public static FireOverlay FireOverlay = new FireOverlay();
    public static SpyglassOverlay SpyglassOverlay = new SpyglassOverlay();
    public static PortalOverlay PortalOverlay = new PortalOverlay();
    public static FreezeOverlay FreezeOverlay = new FreezeOverlay();
    public static BlindnessOverlay BlindnessOverlay = new BlindnessOverlay();
    public static DarknessOverlay DarknessOverlay = new DarknessOverlay();
    public static Vignette Vignette = new Vignette();
    public static BossBar BossBar = new BossBar();
    public static Scoreboard Scoreboard = new Scoreboard();
    public static Totem Totem = new Totem();
    public static Environment Environment = new Environment();
    public static AttackIndicator AttackIndicator = new AttackIndicator();
    public static ProjectileHighlight ProjectileHighlight = new ProjectileHighlight();
    public static PieChart PieChart = new PieChart();
    public static Boat Boat = new Boat();
    public static ShieldConfig Shields = new ShieldConfig();
    public static ParticleConfig Particle = new ParticleConfig();
    public static DroppedItems DroppedItems = new DroppedItems();

    public static class PumpkinOverlay {
        public boolean enabled = true;
        public float opacity = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class FireOverlay {
        public boolean enabled = true;
        public float offsetPixels = 0;
        public float opacity = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class SpyglassOverlay {
        public boolean enabled = true;
        public float scale = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class PortalOverlay {
        public boolean enabled = true;
        public float opacity = 1;
        public float speed = 1;
        public boolean allowGuisInPortal = true;
        public boolean allowCameraShake = false;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class FreezeOverlay {
        public boolean enabled = false;
        public float opacity = 1;
        public float Xscale = 1;
        public float Yscale = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class BlindnessOverlay {
        public boolean enabled = true;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class DarknessOverlay {
        public boolean enabled = true;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class Vignette {
        public boolean enabled = true;
        public float opacity = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class BossBar {
        public boolean enabled = true;
        public int YOffset = 12;
        public int XOffset = 0;
        public float scale = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class Scoreboard {
        public boolean enabled = true;
        public int YOffset = 12;
        public int XOffset = 0;
        public float scale = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class Totem {
        public boolean enabled = true;
        public boolean showTotemAnimation = true;
        public boolean showParticles = true;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class AttackIndicator {
        public boolean enabled = true;
        public int YOffset = 12;
        public int XOffset = 0;
        public float scale = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class ProjectileHighlight {
        public boolean enabled = true;
        public int red = 0;
        public int green = 158;
        public int blue = 166;
        public float opacity = 1;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
        public List<String> supportedProjectiles = new ArrayList<>(List.of(
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
        ));
    }

    public static class PieChart {
        public boolean enabled = false;
        public int x = -1;
        public int y = 500;
        public int oldWindowWidth = -1;
        public int oldWindowHeight = -1;
        public boolean renderingPieChart = false;
        public int windowIndex = 0;
        public float scale = 1;
        public boolean ShowBackground = true;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class Boat {
        public boolean enabled = true;
        public boolean unlockBoatPov = true;
        public boolean showBoatItem = true;
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class ShieldConfig {
        public boolean enabled = true;
        public int simpleYOffset = 0;
        public HandSettings firstPersonMain = new HandSettings();
        public HandSettings firstPersonOff = new HandSettings();
        public HandSettings thirdPersonMain = new HandSettings();
        public HandSettings thirdPersonOff = new HandSettings();
        public HandSettings otherPlayersMain = new HandSettings();
        public HandSettings otherPlayersOff = new HandSettings();
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class HandSettings {
        public ShieldSettings idle = new ShieldSettings();
        public ShieldSettings blocking = new ShieldSettings();
    }

    public static class ShieldSettings {
        public double xOffset = 0;
        public double yOffset = 0;
        public double zOffset = 0;
        public float scaleX = 1;
        public float scaleY = 1;
        public float scaleZ = 1;
        public float rotX = 0;
        public float rotY = 0;
        public float rotZ = 0;
    }

    public static class ParticleConfig {
        public boolean enabled = true;
        public float globalChance = 1.0f;
        public float selfPotionChance = 0.5f;
        public float otherPotionChance = 1.0f;
        public java.util.HashMap<String, Float> customParticleChances = new java.util.HashMap<>();
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }

    public static class Environment {
        // Leaving environment without a toggle since it controls multiple distinct bools (fullbright, fog, etc.),
        // unless you want one master toggle for it too.
        public boolean fullbright = false;
        public boolean disableFog = false;
        public float fogMultiplier = 1;
        public boolean clearLava = false;
        public boolean clearWater = false;
        public float rainOpacity = 1;
        public boolean noRainParticles = false;
        public boolean noSnow = false;
        public boolean blockBreakingOverlay = true;
    }

    public static class DroppedItems {
        public boolean enabled = true;
        public float customScale = 3.0f;
        public java.util.List<String> itemList = new java.util.ArrayList<>(java.util.List.of("minecraft:golden_apple"));
        public InputConstants.Key toggleKeybind = InputConstants.UNKNOWN;
    }
}