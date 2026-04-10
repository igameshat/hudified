package com.swaphat.client.overlaymanager.automation;

import com.swaphat.client.overlaymanager.config.ConfigOption;
import com.swaphat.client.overlaymanager.config.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

import java.util.*;

public class AutomationManager {

    public enum TriggerEvent {
        NONE("Always Off"),
        COMBAT("In Combat (Hurt Recently)"),
        INVISIBILITY("Has Invisibility"),
        NETHER("In The Nether"),
        END("In The End"),
        LOW_HEALTH("Low Health (< 30%)");

        public final String displayName;
        TriggerEvent(String displayName) { this.displayName = displayName; }
        public String displayName() { return displayName; }
    }

    public static class Rule {
        public boolean enabled = true;
        public TriggerEvent trigger = TriggerEvent.NONE;
        // Key: ConfigOption ID, Value: Override Value
        public final Map<String, Object> overrides = new HashMap<>();
    }

    public static final List<Rule> RULES = new ArrayList<>();
    private static Rule activeRule = null;
    private static final Map<String, Object> originalStates = new HashMap<>();

    static {
        RULES.add(new Rule()); // Default empty rule for GUI initialization
    }

    public static void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        Rule triggeredRule = null;
        for (Rule rule : RULES) {
            if (!rule.enabled) continue;
            if (evaluateTrigger(rule.trigger, mc)) {
                triggeredRule = rule; // Priority: Last matching rule in the list wins
            }
        }

        if (triggeredRule != activeRule) {
            revertOverrides();
            if (triggeredRule != null) applyOverrides(triggeredRule);
            activeRule = triggeredRule;
        }
    }

    private static boolean evaluateTrigger(TriggerEvent trigger, Minecraft mc) {
        return switch (trigger) {
            case NONE -> false;
            case COMBAT -> mc.player.getLastHurtByMobTimestamp() > mc.player.tickCount - 100; // 5 seconds
            case INVISIBILITY -> mc.player.hasEffect(MobEffects.INVISIBILITY);
            case NETHER -> mc.level.dimension() == Level.NETHER;
            case END -> mc.level.dimension() == Level.END;
            case LOW_HEALTH -> mc.player.getHealth() / mc.player.getMaxHealth() < 0.3f;
        };
    }

    @SuppressWarnings("unchecked")
    private static void applyOverrides(Rule rule) {
        for (ConfigOption option : ConfigRegistry.ALL_OPTIONS) {
            if (rule.overrides.containsKey(option.id)) {
                originalStates.put(option.id, option.get());
                option.set(rule.overrides.get(option.id));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void revertOverrides() {
        for (ConfigOption option : ConfigRegistry.ALL_OPTIONS) {
            if (originalStates.containsKey(option.id)) {
                option.set(originalStates.get(option.id));
            }
        }
        originalStates.clear();
    }
}