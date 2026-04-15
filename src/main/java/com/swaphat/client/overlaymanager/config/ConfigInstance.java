package com.swaphat.client.overlaymanager.config;

import net.minecraft.client.Minecraft;

public class ConfigInstance {
    public static boolean OverlayEnabled = true;

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
    public static ArrowHighlight ArrowHighlight = new ArrowHighlight();
    public static PieChart PieChart = new PieChart();
    public static Boat Boat = new Boat();
    public static ShieldConfig Shields = new ShieldConfig();

    public static class PumpkinOverlay {
        public boolean enabled = true;
        public float opacity = 255;
    }

    public static class FireOverlay {
        public boolean enabled = true;
        public float offsetPixels = 0f;
        public float opacity = 255;
    }

    public static class SpyglassOverlay {
        public boolean enabled = true;
        public float scale = 1f;
    }

    public static class PortalOverlay {
        public boolean enabled = true;
        public float opacity = 255;
        public float speed = 1f;
        public boolean allowGuisInPortal = false;
        public boolean allowCameraShake = true;
    }

    public static class FreezeOverlay {
        public boolean enabled = false;
        public int opacity = 255;
        public float scale = 1f;
    }

    public static class BlindnessOverlay {
        public boolean enabled = true;
    }

    public static class DarknessOverlay {
        public boolean enabled = true;
    }

    public static class Vignette {
        public boolean enabled = true;
        public float opacity = 255;
    }

    public static class BossBar {
        public boolean enabled = true;
        public int bossBarYOffset = 10;
        public int bossBarXOffset = 0;
        public float scale = 1f;
    }

    public static class Scoreboard {
        public boolean enabled = true;
    }

    public static class Totem {
        public boolean enabled = true;
        public boolean showTotemAnimation = true;
        public boolean showParticles = true;
    }

    public static class AttackIndicator {
        public boolean enabled = false;
        public int hotbarXOffset = 0;
        public int hotbarYOffset = 0;
        public float scale = 1f;
    }

    public static class ArrowHighlight {
        public boolean enabled = true;
        public boolean xrayMode = false;
        public int red = 100;
        public int green = 100;
        public int blue = 100;
    }

    public static class PieChart {
        public boolean enabled = true;

        public int x = -1;
        public int y = 500;

        public int oldWindowWidth = -1;
        public int oldWindowHeight = -1;

        public boolean renderingPieChart = false;
        public int windowIndex = 0;

        public float scale = 1f;
    }

    public static class Boat {
        public boolean enabled = true;
        public boolean unlockBoatPov = true;
    }

    public static class ShieldConfig {
        public boolean enabled = true;

        // First Person
        public HandSettings firstPersonMain = new HandSettings();
        public HandSettings firstPersonOff = new HandSettings();

        // Third Person / F5
        public HandSettings thirdPersonMain = new HandSettings();
        public HandSettings thirdPersonOff = new HandSettings();

        // Other Players
        public HandSettings otherPlayersMain = new HandSettings();
        public HandSettings otherPlayersOff = new HandSettings();
    }

    public static class HandSettings {
        public ShieldSettings idle = new ShieldSettings();
        public ShieldSettings blocking = new ShieldSettings();
    }

    public static class ShieldSettings {
        public double xOffset = 0.0;
        public double yOffset = 0.0;
        public float scale = 1.0f;

        public float rotX = 0.0f;
        public float rotY = 0.0f;
        public float rotZ = 0.0f;
    }

    public static class Environment {
        public boolean fullbright = false;
        public boolean disableFog = false;
        public float fogMultiplier = 1.0f;
        public boolean clearLava = false;
        public boolean clearWater = false;
        public float rainOpacity = 1.0f;
        public boolean noRainParticles = false;
        public boolean noSnow = false;
    }
}