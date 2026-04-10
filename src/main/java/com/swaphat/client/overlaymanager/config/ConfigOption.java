package com.swaphat.client.overlaymanager.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigOption<T> {
    public final String id;
    public final String name;
    public final String category;
    public final int color;
    public final boolean isSlider;
    public final float min, max;

    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public ConfigOption(String id, String name, String category, int color, Supplier<T> getter, Consumer<T> setter) {
        this(id, name, category, color, false, 0, 0, getter, setter);
    }

    public ConfigOption(String id, String name, String category, int color, float min, float max, Supplier<T> getter, Consumer<T> setter) {
        this(id, name, category, color, true, min, max, getter, setter);
    }

    private ConfigOption(String id, String name, String category, int color, boolean isSlider, float min, float max, Supplier<T> getter, Consumer<T> setter) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color = color;
        this.isSlider = isSlider;
        this.min = min;
        this.max = max;
        this.getter = getter;
        this.setter = setter;
    }

    public T get() { return getter.get(); }
    public void set(T value) { setter.accept(value); }
}