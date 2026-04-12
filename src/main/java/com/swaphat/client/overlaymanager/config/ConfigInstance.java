package com.swaphat.client.overlaymanager.config;

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
        public int bossBarYOffset = 0;
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
    }

    public static class ArrowHighlight {
        public boolean enabled = true;
        public int red = 100;
        public int green = 100;
        public int blue = 100;
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