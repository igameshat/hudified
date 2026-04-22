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

    public boolean mouseScrolled(double mouseX, double mouseY) {
        if (!this.dropped) return false;
        for (AbstractWidget child : this.children) {
            if (child.isMouseOver(mouseX, mouseY) && mouseScrolled(mouseX, mouseY)) {
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

    public void addSubMenu(DropDownWidget menu) {
        menu.isNested = true;
        this.children.add(menu);
    }

    public void addToggleButton(String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        AbstractWidget wrapper = new AbstractWidget(0, 0, this.width - 15, 20, Component.empty()) {
            private final Button btn = Button.builder(Component.literal(getter.get() ? "ON" : "OFF"), b -> {
                setter.accept(!getter.get());
                b.setMessage(Component.literal(getter.get() ? "ON" : "OFF"));
            }).bounds(0, 0, (width - 15) / 2, 20).build();

            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                // Text Left
                guiGraphics.drawString(Minecraft.getInstance().font, label, this.getX() + 5, this.getY() + 6, 0xffffffff, false);

                // Button Right
                btn.setX(this.getX() + this.width - btn.getWidth());
                btn.setY(this.getY());
                btn.render(guiGraphics, mouseX, mouseY, delta);
            }

            @Override public boolean mouseClicked(@NonNull MouseButtonEvent e, boolean b) { return btn.mouseClicked(e, b); }
            @Override public boolean mouseReleased(@NonNull MouseButtonEvent e) { return btn.mouseReleased(e); }
            @Override public boolean mouseDragged(@NonNull MouseButtonEvent e, double dx, double dy) { return btn.mouseDragged(e, dx, dy); }
            @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
            @Override public void setFocused(boolean focused) { super.setFocused(focused); btn.setFocused(focused); }
        };

        this.children.add(wrapper);
    }

    public void addButton(String text, Button.OnPress action) {
        AbstractWidget wrapper = new AbstractWidget(0, 0, this.width - 15, 20, Component.empty()) {
            private final Button btn = Button.builder(Component.literal("Click"), action)
                    .bounds(0, 0, (width - 15) / 2, 20).build();

            @Override
            protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                // Text left
                guiGraphics.drawString(Minecraft.getInstance().font, text, this.getX() + 5, this.getY() + 6, 0xffffffff, false);

                // Button right
                btn.setX(this.getX() + this.width - btn.getWidth());
                btn.setY(this.getY());
                btn.render(guiGraphics, mouseX, mouseY, delta);
            }
            @Override public boolean mouseClicked(@NonNull MouseButtonEvent e, boolean b) { return btn.mouseClicked(e, b); }
            @Override public boolean mouseReleased(@NonNull MouseButtonEvent e) { return btn.mouseReleased(e); }
            @Override public boolean mouseDragged(@NonNull MouseButtonEvent e, double dx, double dy) { return btn.mouseDragged(e, dx, dy); }
            @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
            @Override public void setFocused(boolean focused) { super.setFocused(focused); btn.setFocused(focused); }
        };
        this.children.add(wrapper);
    }

    public void addSlider(String label, double defaultValue, Consumer<Double> onValueChange) {
        boolean isPercentage = label.contains("Scale") || label.contains("Speed") || label.contains("Multiplier") || label.contains("Offset");
        int defaultMax = isPercentage ? 100 : 255;
        addSlider(label, defaultValue, defaultMax, isPercentage, onValueChange);
    }

    // New explicit slider method (Upgraded Mapping & Dragging Fix)
    public void addSlider(String label, double currentValue, int maxLimit, boolean showAsPercentage, Consumer<Double> onValueChange) {

        double displayVal = showAsPercentage ? (currentValue * 100.0) : currentValue;
        double initialHandlePos = Mth.clamp(displayVal / maxLimit, 0.0, 1.0);

        AbstractSliderButton dualWidget = new AbstractSliderButton(0, 0, this.width - 15, 20, Component.empty(), initialHandlePos) {
            private boolean isEditingText = false;
            private long lastClickTime = 0;
            private boolean isShifted = false; // <--- THE FIX FLAG
            private final EditBox editBox = new EditBox(Minecraft.getInstance().font, 0, 0, (width - 15) / 2, height, Component.empty());

            {
                editBox.setMaxLength(String.valueOf(maxLimit).length() + 1);

                editBox.setFilter(text -> {
                    if (text.isEmpty() || text.equals("-")) return true;
                    if (!text.matches("^-?\\d*$")) return false;
                    try {
                        return Integer.parseInt(text) <= maxLimit;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });

                editBox.setResponder(text -> {
                    if (!text.isEmpty() && !text.equals("-")) {
                        try {
                            int parsed = Integer.parseInt(text);
                            int clamped = Mth.clamp(parsed, 0, maxLimit);
                            this.value = (double) clamped / maxLimit;
                            this.applyValue();
                        } catch (NumberFormatException ignored) {}
                    }
                });

                this.updateMessage();
            }

            @Override
            protected void updateMessage() {
                long currentDisplayVal = Math.round(this.value * maxLimit);
                this.setMessage(Component.literal(currentDisplayVal + (showAsPercentage ? "%" : "")));
            }

            @Override
            protected void applyValue() {
                double currentDisplayVal = this.value * maxLimit;
                double realValue = showAsPercentage ? (currentDisplayVal / 100.0) : currentDisplayVal;
                onValueChange.accept(realValue);
            }

            private void toggleMode() {
                isEditingText = !isEditingText;
                if (isEditingText) {
                    long currentDisplayVal = Math.round(this.value * maxLimit);
                    editBox.setValue(String.valueOf(currentDisplayVal));
                    editBox.setFocused(true);
                } else {
                    editBox.setFocused(false);
                    this.updateMessage();
                }
            }

            @Override
            public void renderWidget(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                guiGraphics.drawString(Minecraft.getInstance().font, label, this.getX() + 5, this.getY() + 6, 0xffffffff, false);

                int visualX = this.getX() + this.width - ((this.width) / 2);
                int visualW = (this.width) / 2;

                if (isEditingText) {
                    editBox.setX(visualX);
                    editBox.setY(this.getY());
                    editBox.render(guiGraphics, mouseX, mouseY, delta);
                } else {
                    int originalX = this.getX();
                    int originalW = this.width;

                    this.setX(visualX);
                    this.setWidth(visualW);
                    this.isShifted = true; // Tell isMouseOver the bounds are currently shifted

                    super.renderWidget(guiGraphics, mouseX, mouseY, delta);

                    this.isShifted = false; // Reset it back
                    this.setX(originalX);
                    this.setWidth(originalW);
                }
            }

            @Override
            public boolean isMouseOver(double mX, double mY) {
                // If we are currently wrapped in a super call, just use normal bounds checking!
                if (isShifted) {
                    return mX >= this.getX() && mX <= this.getX() + this.width && mY >= this.getY() && mY <= this.getY() + this.height;
                }

                // Otherwise calculate the virtual half-bounds
                int visualX = this.getX() + this.width - ((this.width) / 2);
                return mX >= visualX && mX <= visualX + ((double) (this.width) / 2) && mY >= this.getY() && mY <= this.getY() + this.height;
            }

            @Override
            public boolean mouseClicked(@NonNull MouseButtonEvent mouseButtonEvent, boolean bl) {
                // Check if they clicked the unshifted visual slider
                if (!this.isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y())) return false;

                long currentTime = System.currentTimeMillis();
                boolean isDoubleClick = (currentTime - lastClickTime) < 300;
                lastClickTime = currentTime;

                if (isDoubleClick) {
                    toggleMode();
                    return true;
                }

                int originalX = this.getX();
                int originalW = this.width;
                this.setX(this.getX() + this.width - ((this.width) / 2));
                this.setWidth((this.width) / 2);
                this.isShifted = true; // PREVENT DOUBLE SHIFT

                boolean result = isEditingText ? editBox.mouseClicked(mouseButtonEvent, bl) : super.mouseClicked(mouseButtonEvent, bl);

                this.isShifted = false; // RESET
                this.setX(originalX);
                this.setWidth(originalW);
                return result;
            }

            @Override
            public boolean mouseDragged(@NonNull MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
                int originalX = this.getX();
                int originalW = this.width;
                this.setX(this.getX() + this.width - ((this.width) / 2));
                this.setWidth((this.width) / 2);
                this.isShifted = true; // PREVENT DOUBLE SHIFT

                boolean result = isEditingText ? editBox.mouseDragged(mouseButtonEvent, dragX, dragY) : super.mouseDragged(mouseButtonEvent, dragX, dragY);

                this.isShifted = false; // RESET
                this.setX(originalX);
                this.setWidth(originalW);
                return result;
            }

            @Override
            public boolean mouseReleased(@NonNull MouseButtonEvent mouseButtonEvent) {
                int originalX = this.getX();
                int originalW = this.width;
                this.setX(this.getX() + this.width - ((this.width) / 2));
                this.setWidth((this.width) / 2);
                this.isShifted = true;

                boolean result = isEditingText ? editBox.mouseReleased(mouseButtonEvent) : super.mouseReleased(mouseButtonEvent);

                this.isShifted = false;
                this.setX(originalX);
                this.setWidth(originalW);
                return result;
            }

            public boolean mouseScrolled(double mX, double mY, double scrollX, double scrollY) {
                return !isEditingText && doScroll(mX, mY, scrollY);
            }

            private boolean doScroll(double mX, double mY, double amount) {
                if (this.isMouseOver(mX, mY)) {
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
    }


    public void addIntField(String label, int currentValue, Consumer<Integer> setter) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font, 0, 0, (this.width - 15) / 2, 20, Component.empty());
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
                // Text Left
                guiGraphics.drawString(Minecraft.getInstance().font, label, this.getX() + 5, this.getY() + 6, 0xffffffff, false);

                // Box Right
                editBox.setX(this.getX() + this.width - editBox.getWidth());
                editBox.setY(this.getY());
                editBox.render(guiGraphics, mouseX, mouseY, delta);
            }

            @Override
            public boolean isMouseOver(double mX, double mY) {
                return mX >= editBox.getX() && mX <= editBox.getX() + editBox.getWidth() &&
                        mY >= editBox.getY() && mY <= editBox.getY() + editBox.getHeight();
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
            @Override public boolean mouseClicked(@NonNull MouseButtonEvent e, boolean bln) { return isMouseOver(e.x(), e.y()) && editBox.mouseClicked(e, bln); }
            @Override public boolean keyPressed(@NonNull KeyEvent keyEvent) { return editBox.keyPressed(keyEvent); }
            @Override public boolean charTyped(@NonNull CharacterEvent characterEvent) { return editBox.charTyped(characterEvent); }
            @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
        };
        this.children.add(wrapper);
    }

    @Override protected void updateWidgetNarration(@NonNull NarrationElementOutput n) {}
}