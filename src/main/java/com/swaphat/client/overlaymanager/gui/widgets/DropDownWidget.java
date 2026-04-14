package com.swaphat.client.overlaymanager.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DropDownWidget extends AbstractWidget {
    private final List<AbstractWidget> children = new ArrayList<>();
    private boolean dropped = false;
    private AbstractWidget focusedChild;

    // -1 is the Header, 0+ are the children
    private int selectionIndex = -1;

    public DropDownWidget(int x, int y, int width, String title) {
        super(x, y, width, 20, Component.literal(title));
    }

    /**
     * Handles opening/closing the menu safely.
     * Prevents NullPointerExceptions by not passing fake events.
     */
    private void toggle() {
        this.dropped = !this.dropped;

        // If we close the menu, unfocus any children and reset selection to header
        if (!this.dropped) {
            if (selectionIndex >= 0 && selectionIndex < children.size()) {
                children.get(selectionIndex).setFocused(false);
            }
            this.selectionIndex = -1;
        } else {
            // If we open via keyboard, default to the first child
            this.selectionIndex = 0;
            if (!children.isEmpty()) {
                children.getFirst().setFocused(true);
            }
        }
    }

    @Override
    protected void renderWidget(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // Render keyboard focus highlight for the header
        if (this.selectionIndex == -1) {
            this.renderHighlight(guiGraphics, this.getX(), this.getY(), this.width, this.height);
        }

        String prefix = this.dropped ? "v " : "> ";
        guiGraphics.drawString(Minecraft.getInstance().font, prefix + this.getMessage().getString(), this.getX() + 5, this.getY() + 6, 0xffffffff, false);

        if (this.dropped) {
            for (int i = 0; i < this.children.size(); i++) {
                AbstractWidget child = this.children.get(i);

                // Render keyboard focus highlight for children
                if (this.selectionIndex == i) {
                    this.renderHighlight(guiGraphics, child.getX(), child.getY(), child.getWidth(), child.getHeight());
                }

                child.render(guiGraphics, mouseX, mouseY, delta);
            }
        }
    }

    private void renderHighlight(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        // A subtle white overlay to show which element is selected via keyboard
        guiGraphics.fill(x, y, x + w, y + h, 0x40ffffff);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent keyEvent) {
        if (!this.active || !this.visible) return false;

        // 1. Navigation: UP / DOWN
        if (keyEvent.isDown() || keyEvent.isUp()) {
            // Unfocus the current child before moving focus
            if (selectionIndex >= 0 && selectionIndex < children.size()) {
                children.get(selectionIndex).setFocused(false);
            }

            if (keyEvent.isDown()) {
                if (!this.dropped) {
                    this.toggle(); // Auto-open on down press
                } else {
                    this.selectionIndex = Math.min(this.selectionIndex + 1, this.children.size() - 1);
                }
            } else {
                this.selectionIndex = Math.max(this.selectionIndex - 1, -1);
            }

            // Apply focus to the new selection (crucial for slider logic)
            if (selectionIndex >= 0 && selectionIndex < children.size()) {
                children.get(selectionIndex).setFocused(true);
            }
            return true;
        }

        // 2. Routing: Pass key events to the focused child
        // This allows sliders to handle their own Left/Right arrow logic natively
        if (this.dropped && selectionIndex >= 0) {
            if (children.get(selectionIndex).keyPressed(keyEvent)) {
                return true;
            }
        }

        // 3. Selection: ENTER / SPACE on Header
        if (selectionIndex == -1 && keyEvent.isSelection()) {
            this.toggle();
            return true;
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (super.isMouseOver(mouseX, mouseY)) return true;
        if (this.dropped) {
            for (AbstractWidget child : this.children) {
                if (child.isMouseOver(mouseX, mouseY)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        // Sync keyboard selection with mouse click
        if (super.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y())) {
            this.selectionIndex = -1;
        }

        if (this.dropped) {
            for (int i = 0; i < this.children.size(); i++) {
                AbstractWidget child = this.children.get(i);
                if (child.mouseClicked(mouseButtonEvent, bl)) {
                    // Unfocus previous selection
                    if (selectionIndex >= 0 && selectionIndex < children.size()) {
                        children.get(selectionIndex).setFocused(false);
                    }

                    this.focusedChild = child;
                    this.selectionIndex = i;
                    child.setFocused(true);
                    return true;
                }
            }
        }

        this.focusedChild = null;
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
        if (this.dropped && this.focusedChild != null) {
            return this.focusedChild.mouseDragged(mouseButtonEvent, dragX, dragY);
        }
        return super.mouseDragged(mouseButtonEvent, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent mouseButtonEvent) {
        if (this.dropped && this.focusedChild != null) {
            boolean result = this.focusedChild.mouseReleased(mouseButtonEvent);
            this.focusedChild = null;
            return result;
        }
        return super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        this.toggle();
    }

    @Override
    public boolean charTyped(@NonNull CharacterEvent characterEvent) {
        // If the menu isn't open, we don't swallow characters
        if (!this.dropped || selectionIndex < 0) {
            return false;
        }

        // Pass to the selected child (the wrapper)
        return children.get(selectionIndex).charTyped(characterEvent);
    }

    // =========================================================
    // ADDER METHODS
    // =========================================================

    public DropDownWidget addToggleButton(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        int nextY = this.getY() + this.height + 2 + (this.children.size() * 22);

        Button btn = Button.builder(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")), b -> {
            setter.accept(!getter.get());
            b.setMessage(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")));
        }).bounds(this.getX() + 2, nextY, this.width - 4, 20).build();

        this.children.add(btn);
        return this;
    }

    public DropDownWidget addButton(String text, Button.OnPress action) {
        int nextY = this.getY() + this.height + 2 + (this.children.size() * 22);

        Button btn = Button.builder(Component.literal(text), action)
                .bounds(this.getX() + 2, nextY, this.width - 4, 20).build();

        this.children.add(btn);
        return this;
    }

    public DropDownWidget addSlider(String label, double defaultValue, Consumer<Double> onValueChange) {
        int nextY = this.getY() + this.height + 2 + (this.children.size() * 22);

        AbstractSliderButton slider = new AbstractSliderButton(this.getX() + 2, nextY, this.width - 4, 20, Component.literal(label), defaultValue) {
            @Override
            protected void updateMessage() {
                int displayValue = (int) (this.value * 255);
                if (label.contains("Scale") || label.contains("Speed") || label.contains("Multiplier") || label.contains("Offset")) {
                    displayValue = (int) (this.value * 100);
                    this.setMessage(Component.literal(label + ": " + displayValue + "%"));
                } else {
                    this.setMessage(Component.literal(label + ": " + displayValue));
                }
            }

            @Override
            protected void applyValue() {
                onValueChange.accept(this.value);
            }
        };



        this.children.add(slider);
        return this;
    }

    public DropDownWidget addIntField(String label, int currentValue, Consumer<Integer> setter) {
        int nextY = this.getY() + this.height + 2 + (this.children.size() * 22);

        EditBox editBox = new EditBox(Minecraft.getInstance().font, this.getX() + 60, nextY, this.width - 65, 20, Component.literal(label));
        editBox.setValue(String.valueOf(currentValue));
        editBox.setFilter(text -> text.isEmpty() || text.matches("^-?\\d*$"));

        editBox.setResponder(text -> {
            if (!text.isEmpty() && !text.equals("-")) {
                try {
                    setter.accept(Integer.parseInt(text));
                } catch (NumberFormatException ignored) {}
            }
        });

        AbstractWidget wrapper = new AbstractWidget(this.getX(), nextY, this.width, 20, Component.literal(label)) {
            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                guiGraphics.drawString(Minecraft.getInstance().font, label + ":", this.getX() + 5, this.getY() + 6, 0xffffffff, false);
                editBox.render(guiGraphics, mouseX, mouseY, delta);
            }

            @Override
            public void setFocused(boolean focused) {
                super.setFocused(focused);
                editBox.setFocused(focused);
            }

            @Override
            public boolean mouseClicked(@NonNull MouseButtonEvent mouseButtonEvent, boolean bln) {
                // STRICT MAPPING
                return editBox.mouseClicked(mouseButtonEvent, bln);
            }

            @Override
            public boolean keyPressed(@NonNull KeyEvent keyEvent) {
                // STRICT MAPPING
                return editBox.keyPressed(keyEvent);
            }

            @Override
            public boolean charTyped(@NonNull CharacterEvent characterEvent) {
                // STRICT MAPPING
                return editBox.charTyped(characterEvent);
            }

            @Override
            protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {}
        };

        this.children.add(wrapper);
        return this;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
    }
}