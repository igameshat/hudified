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

    // all opacity has to be between 0 and 255
    public static class PumpkinOverlay {
        public boolean enabled = true;
        public float opacity = 255;

    }

    public static class FireOverlay {
        public boolean enabled = true;
        public float offsetPixels = 200f;
        public float opacity = 255;
    }

    public static class SpyglassOverlay {
        public boolean enabled = true;
        public float scale = 0.1f;
    }

    public static class PortalOverlay {
        public boolean enabled = true;
        public float opacity = 255;
        public float speed = 1f;
        public boolean allowGuisInPortal = true;
        public boolean allowCameraShake = true;
    }

    public static class FreezeOverlay {
        public boolean enabled = true;
        public int opacity = 128;
        public float scale = 1f;
    }

    public static class BlindnessOverlay {
        public boolean enabled = true;
        public boolean enableSlowdown = true;
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
        public int bossBarYOffset = 12;
        public float scale = .5f;
    }

    public static class Scoreboard {
        public boolean enabled = true;
    }

    public static class Totem {
        public boolean enabled = true;
        public boolean showTotemAnimation = true;
        public boolean showParticles = true;
    }
}


