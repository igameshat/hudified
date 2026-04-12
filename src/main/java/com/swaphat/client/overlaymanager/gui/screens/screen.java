package com.swaphat.client.overlaymanager.gui.screens;

import com.swaphat.client.overlaymanager.config.ConfigInstance;
import com.swaphat.client.overlaymanager.gui.widgets.DropDownWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class screen extends Screen {
    private static Screen previousScreen;
    public screen(Screen parent) {
        previousScreen = parent;
        super(Component.literal("imTesting"));
    }

    @Override
    protected void init() {
        int startX = 20;
        int columnWidth = 100;
        int rowHeight = 30;
        this.addRenderableWidget(new DropDownWidget(20, 40, columnWidth, "Vanilla Settings")
                .addToggleButton("Subtitles",
                        // GETTER: Ask Minecraft if subtitles are currently on
                        () -> Minecraft.getInstance().options.showSubtitles().get(),

                        // SETTER: Tell Minecraft to change the setting and save it to options.txt
                        v -> {
                            Minecraft.getInstance().options.showSubtitles().set(v);
                            Minecraft.getInstance().options.save();
                        }
                )
        );
        this.addRenderableWidget(new DropDownWidget(20, 160, columnWidth, "Vanilla Settings")
                .addToggleButton("Subtitles",
                        // GETTER: Ask Minecraft if subtitles are currently on
                        () -> Minecraft.getInstance().options.showSubtitles().get(),

                        // SETTER: Tell Minecraft to change the setting and save it to options.txt
                        v -> {
                            Minecraft.getInstance().options.showSubtitles().set(v);
                            Minecraft.getInstance().options.save();
                        }
                )
        );
    }

    @Override
    public void render(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(previousScreen);
    }
}