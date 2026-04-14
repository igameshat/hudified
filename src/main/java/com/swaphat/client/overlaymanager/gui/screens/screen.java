package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.config.ConfigManager;
import com.swaphat.client.overlaymanager.gui.widgets.DropDownWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class screen extends Screen {
    private static Screen previousScreen;
    private static final float MAX_SCALE = 3.0f;

    public screen(Screen parent) {
        super(Component.literal("Overlay Manager Settings"));
        previousScreen = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new DropDownWidget(20, 20, 150, "Pie Chart Settings")
                .addToggleButton("Enabled",
                        () -> ConfigInstance.PieChart.enabled,
                        v -> ConfigInstance.PieChart.enabled = v)

                .addButton("Move Layout...", b -> {
                    this.minecraft.setScreen(new LayoutEditorScreen(this));
                })

                .addIntField("X Position",
                        ConfigInstance.PieChart.x,
                        v -> ConfigInstance.PieChart.x = v)

                .addIntField("Y Position",
                        ConfigInstance.PieChart.y,
                        v -> ConfigInstance.PieChart.y = v)

                .addSlider("Scale",
                        ConfigInstance.PieChart.scale / MAX_SCALE,
                        v -> ConfigInstance.PieChart.scale = (float) (v * MAX_SCALE))
        );
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.minecraft.setScreen(previousScreen);
    }
}