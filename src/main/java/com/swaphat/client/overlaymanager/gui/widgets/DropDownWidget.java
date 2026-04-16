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
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DropDownWidget extends AbstractWidget {
    private final List<AbstractWidget> children = new ArrayList<>();
    private boolean dropped = false;
    private AbstractWidget focusedChild;
    private int selectionIndex = -1;

    public boolean isNested = false;

    public static boolean drawBackground = true;

    public DropDownWidget(int x, int y, int width, String title) {
        super(x, y, width, 20, Component.literal(title));
    }

    private void toggle() {
        this.dropped = !this.dropped;
        if (!this.dropped) {
            if (selectionIndex >= 0 && selectionIndex < children.size()) {
                children.get(selectionIndex).setFocused(false);
            }
            this.selectionIndex = -1;
        } else {
            this.selectionIndex = 0;
            if (!children.isEmpty()) children.getFirst().setFocused(true);
        }
    }

    public int getExpandedHeight() {
        if (!this.dropped) return this.height;
        int total = this.height;
        for (AbstractWidget child : this.children) {
            if (child instanceof DropDownWidget nested) {
                total += nested.getExpandedHeight() + 2;
            } else {
                total += child.getHeight() + 2;
            }
        }
        return total + 2;
    }

    @Override
    protected void renderWidget(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (this.selectionIndex == -1) {
            this.renderHighlight(guiGraphics, this.getX(), this.getY(), this.width, this.height);
        }

        String prefix = this.dropped ? (this.isNested ? "[-] " : "v ") : (this.isNested ? "[+] " : "> ");
        guiGraphics.drawString(Minecraft.getInstance().font, prefix + this.getMessage().getString(), this.getX() + 5, this.getY() + 6, 0xffffffff, false);

        if (this.dropped) {
            int startY = this.getY() + this.height;
            int currentY = startY + 2;
            int indent = this.isNested ? 10 : 2;

            if (!this.isNested && drawBackground) {
                guiGraphics.fill(this.getX(), startY, this.getX() + this.width, startY + (this.getExpandedHeight() - this.height), 0x99000000);
            }

            if (this.isNested && drawBackground) {
                guiGraphics.fill(this.getX() + 2, startY, this.getX() + 3, startY + (this.getExpandedHeight() - this.height) - 2, 0xFF555555);
            }

            for (int i = 0; i < this.children.size(); i++) {
                AbstractWidget child = this.children.get(i);
                child.setX(this.getX() + indent);
                child.setY(currentY);

                if (this.selectionIndex == i) {
                    this.renderHighlight(guiGraphics, child.getX(), child.getY(), child.getWidth(), child.getHeight());
                }

                child.render(guiGraphics, mouseX, mouseY, delta);

                if (child instanceof DropDownWidget nested) {
                    currentY += nested.getExpandedHeight() + 2;
                } else {
                    currentY += child.getHeight() + 2;
                }
            }
        }
    }

    private void renderHighlight(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        if (drawBackground) {
            guiGraphics.fill(x, y, x + w, y + h, 0x40ffffff);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!this.dropped) return false;
        for (AbstractWidget child : this.children) {
            if (child.isMouseOver(mouseX, mouseY) && child.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
                return true;
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!this.dropped) return false;
        for (AbstractWidget child : this.children) {
            if (child.isMouseOver(mouseX, mouseY) && mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (super.isMouseOver(mouseX, mouseY)) return true;
        if (this.dropped) {
            int startY = this.getY() + this.height;
            return mouseX >= this.getX() && mouseX <= this.getX() + this.width &&
                    mouseY >= startY && mouseY <= startY + (this.getExpandedHeight() - this.height);
        }
        return false;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        if (mouseX >= this.getX() && mouseX <= this.getX() + this.width &&
                mouseY >= this.getY() && mouseY <= this.getY() + this.height) {
            this.selectionIndex = -1;
            this.toggle();
            return true;
        }

        if (this.dropped) {
            for (int i = 0; i < this.children.size(); i++) {
                AbstractWidget child = this.children.get(i);
                if (child.isMouseOver(mouseX, mouseY)) {
                    if (child.mouseClicked(mouseButtonEvent, bl)) {
                        if (selectionIndex >= 0 && selectionIndex < children.size()) children.get(selectionIndex).setFocused(false);
                        this.focusedChild = child;
                        this.selectionIndex = i;
                        child.setFocused(true);
                        return true;
                    }
                }
            }
        }

        this.focusedChild = null;
        return false;
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
        if (this.dropped && this.focusedChild != null) return this.focusedChild.mouseDragged(mouseButtonEvent, dragX, dragY);
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
    public boolean keyPressed(@NonNull KeyEvent keyEvent) {
        if (!this.active || !this.visible) return false;
        if (keyEvent.isDown() || keyEvent.isUp()) {
            if (selectionIndex >= 0 && selectionIndex < children.size()) children.get(selectionIndex).setFocused(false);
            if (keyEvent.isDown()) {
                if (!this.dropped) this.toggle();
                else this.selectionIndex = Math.min(this.selectionIndex + 1, this.children.size() - 1);
            } else {
                this.selectionIndex = Math.max(this.selectionIndex - 1, -1);
            }
            if (selectionIndex >= 0 && selectionIndex < children.size()) children.get(selectionIndex).setFocused(true);
            return true;
        }
        if (this.dropped && selectionIndex >= 0) {
            if (children.get(selectionIndex).keyPressed(keyEvent)) return true;
        }
        if (selectionIndex == -1 && keyEvent.isSelection()) {
            this.toggle();
            return true;
        }
        return super.keyPressed(keyEvent);
    }

    @Override
    public boolean charTyped(@NonNull CharacterEvent characterEvent) {
        if (!this.dropped || selectionIndex < 0) return false;
        return children.get(selectionIndex).charTyped(characterEvent);
    }

    // =========================================================
    // ADDER METHODS
    // =========================================================

    public DropDownWidget addSubMenu(DropDownWidget menu) {
        menu.isNested = true;
        this.children.add(menu);
        return this;
    }

    public DropDownWidget addToggleButton(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        Button btn = Button.builder(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")), b -> {
            setter.accept(!getter.get());
            b.setMessage(Component.literal(label + ": " + (getter.get() ? "ON" : "OFF")));
        }).bounds(0, 0, this.width - 15, 20).build();
        this.children.add(btn);
        return this;
    }

    public DropDownWidget addButton(String text, Button.OnPress action) {
        Button btn = Button.builder(Component.literal(text), action).bounds(0, 0, this.width - 15, 20).build();
        this.children.add(btn);
        return this;
    }

    public DropDownWidget addSlider(String label, double defaultValue, Consumer<Double> onValueChange) {
        // Default max limits based on your original percentage/flat value logic
        int defaultMax = (label.contains("Scale") || label.contains("Speed") || label.contains("Multiplier") || label.contains("Offset")) ? 100 : 255;
        return addSlider(label, defaultValue, defaultMax, onValueChange);
    }

    public DropDownWidget addSlider(String label, double defaultValue, int maxLimit, Consumer<Double> onValueChange) {
        boolean isPercentage = label.contains("Scale") || label.contains("Speed") || label.contains("Multiplier") || label.contains("Offset");

        // We extend AbstractSliderButton directly to inherit 'value' and 'updateMessage()'
        AbstractSliderButton dualWidget = new AbstractSliderButton(0, 0, this.width - 15, 20, Component.literal(label), defaultValue) {
            private boolean isEditingText = false;
            private long lastClickTime = 0;
            private final EditBox editBox = new EditBox(Minecraft.getInstance().font, 0, 0, width - 60, height, Component.literal(label));

            { // Instance Initializer for the EditBox
                editBox.setFilter(text -> text.isEmpty() || text.matches("^-?\\d*$"));
                editBox.setResponder(text -> {
                    if (!text.isEmpty() && !text.equals("-")) {
                        try {
                            int parsed = Integer.parseInt(text);
                            int clamped = Mth.clamp(parsed, 0, maxLimit);

                            // Direct access to 'value' since we extend AbstractSliderButton
                            this.value = (double) clamped / maxLimit;
                            this.applyValue();
                        } catch (NumberFormatException ignored) {}
                    }
                });
            }

            @Override
            protected void updateMessage() {
                int displayValue = (int) (this.value * maxLimit);
                if (isPercentage) {
                    this.setMessage(Component.literal(label + ": " + displayValue + "%"));
                } else {
                    this.setMessage(Component.literal(label + ": " + displayValue));
                }
            }

            @Override
            protected void applyValue() {
                onValueChange.accept(this.value);
            }

            private void toggleMode() {
                isEditingText = !isEditingText;
                if (isEditingText) {
                    // Switch to EditBox: set text to the current calculated integer value
                    int displayValue = (int) (this.value * maxLimit);
                    editBox.setValue(String.valueOf(displayValue));
                    editBox.setFocused(true);
                } else {
                    // Switch back to Slider: unfocus box and force text update
                    editBox.setFocused(false);
                    this.updateMessage();
                }
            }

            @Override
            public void renderWidget(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                if (isEditingText) {
                    // Render the Text Box if editing
                    editBox.setX(this.getX() + 60);
                    editBox.setY(this.getY());
                    guiGraphics.drawString(Minecraft.getInstance().font, label + ":", this.getX() + 5, this.getY() + 6, 0xffffffff, false);
                    editBox.render(guiGraphics, mouseX, mouseY, delta);
                } else {
                    // Render the normal Vanilla Slider
                    super.renderWidget(guiGraphics, mouseX, mouseY, delta);
                }
            }

            @Override
            public boolean mouseClicked(@NonNull MouseButtonEvent mouseButtonEvent, boolean bl) {
                long currentTime = System.currentTimeMillis();
                boolean isDoubleClick = (currentTime - lastClickTime) < 300; // 300ms window
                lastClickTime = currentTime;

                if (isDoubleClick && this.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y())) {
                    toggleMode();
                    return true;
                }

                // Route clicks based on current mode
                return isEditingText ? editBox.mouseClicked(mouseButtonEvent, bl) : super.mouseClicked(mouseButtonEvent, bl);
            }

            @Override
            public boolean mouseReleased(@NonNull MouseButtonEvent mouseButtonEvent) {
                return isEditingText ? editBox.mouseReleased(mouseButtonEvent) : super.mouseReleased(mouseButtonEvent);
            }

            @Override
            public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
                return isEditingText ? editBox.mouseDragged(mouseButtonEvent, dragX, dragY) : super.mouseDragged(mouseButtonEvent, dragX, dragY);
            }

            public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
                return isEditingText ? false : doScroll(mouseX, mouseY, scrollY);
            }

            public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
                return isEditingText ? false : doScroll(mouseX, mouseY, amount);
            }

            private boolean doScroll(double mouseX, double mouseY, double amount) {
                if (this.isMouseOver(mouseX, mouseY)) {
                    this.value = Mth.clamp(this.value + (amount > 0 ? 0.05 : -0.05), 0.0, 1.0);
                    this.applyValue();
                    this.updateMessage();
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyPressed(@NonNull KeyEvent keyEvent) {
                if (isEditingText) {
                    // Lock in the value when pressing Enter
                    if (keyEvent.isSelection()) {
                        toggleMode();
                        return true;
                    }
                    return editBox.keyPressed(keyEvent);
                }
                return super.keyPressed(keyEvent);
            }

            @Override
            public boolean charTyped(@NonNull CharacterEvent characterEvent) {
                return isEditingText ? editBox.charTyped(characterEvent) : super.charTyped(characterEvent);
            }

            @Override
            public void setFocused(boolean focused) {
                super.setFocused(focused);
                if (isEditingText) editBox.setFocused(focused);
            }
        };

        this.children.add(dualWidget);
        return this;
    }

    public DropDownWidget addIntField(String label, int currentValue, Consumer<Integer> setter) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font, 0, 0, this.width - 70, 20, Component.literal(label));
        editBox.setValue(String.valueOf(currentValue));
        editBox.setFilter(text -> text.isEmpty() || text.matches("^-?\\d*$"));

        editBox.setResponder(text -> {
            if (!text.isEmpty() && !text.equals("-")) {
                try { setter.accept(Integer.parseInt(text)); } catch (NumberFormatException ignored) {}
            }
        });

        AbstractWidget wrapper = new AbstractWidget(0, 0, this.width - 15, 20, Component.literal(label)) {
            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                editBox.setX(this.getX() + 60);
                editBox.setY(this.getY());
                guiGraphics.drawString(Minecraft.getInstance().font, label + ":", this.getX() + 5, this.getY() + 6, 0xffffffff, false);
                editBox.render(guiGraphics, mouseX, mouseY, delta);
            }

            public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
                return doScroll(mouseX, mouseY, scrollY);
            }
            public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
                return doScroll(mouseX, mouseY, amount);
            }
            private boolean doScroll(double mouseX, double mouseY, double amount) {
                if (this.isMouseOver(mouseX, mouseY)) {
                    try {
                        int current = editBox.getValue().isEmpty() ? 0 : Integer.parseInt(editBox.getValue());
                        current += (amount > 0 ? 1 : -1);
                        editBox.setValue(String.valueOf(current));
                        setter.accept(current);
                        return true;
                    } catch (NumberFormatException ignored) {}
                }
                return false;
            }

            @Override public void setFocused(boolean focused) { super.setFocused(focused); editBox.setFocused(focused); }
            @Override public boolean mouseClicked(@NonNull MouseButtonEvent mouseButtonEvent, boolean bln) { return editBox.mouseClicked(mouseButtonEvent, bln); }
            @Override public boolean keyPressed(@NonNull KeyEvent keyEvent) { return editBox.keyPressed(keyEvent); }
            @Override public boolean charTyped(@NonNull CharacterEvent characterEvent) { return editBox.charTyped(characterEvent); }
            @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
        };
        this.children.add(wrapper);
        return this;
    }

    @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
}