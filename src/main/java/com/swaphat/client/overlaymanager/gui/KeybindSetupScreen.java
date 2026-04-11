package com.swaphat.client.overlaymanager.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class KeybindSetupScreen extends Screen {
    private final Screen parentScreen;
    private final Component titleText;
    private final Consumer<Integer> onKeyBound;

    public KeybindSetupScreen(Screen parentScreen, String actionName, Consumer<Integer> onKeyBound) {
        super(Component.literal("Setting key for: " + actionName));
        this.parentScreen = parentScreen;
        this.titleText = Component.literal("Press any key to bind '" + actionName + "'...");
        this.onKeyBound = onKeyBound;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.drawCenteredString(this.font, this.titleText, this.width / 2, this.height / 2 - 10, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal("(Press ESC to cancel)"), this.width / 2, this.height / 2 + 10, 0xFFAAAAAA);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.setScreen(parentScreen);
        } else {
            this.onKeyBound.accept(keyCode);
            this.minecraft.setScreen(parentScreen);
        }
        return true;
    }

    @Override
    public boolean isPauseScreen() { return false; }
}