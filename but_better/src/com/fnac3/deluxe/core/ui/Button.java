package com.fnac3.deluxe.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Button {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    private float frame;
    private boolean hovering;
    private boolean rightPressed;
    private boolean leftPressed;
    private int state;
    private boolean stateChanged;

    public Button(int x, int y, int width, int height, int state) {
        this(x, y, width, height, state, false);
    }

    public Button(int x, int y, int width, int height, int state, boolean anchorPoint) {
        if (anchorPoint){
            x -= width / 2;
            y -= height / 2;
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = state;
    }

    public void update(float mx, float my){
        stateChanged = false;
        hoverFunction(mx, my);
        if (hovering){
            frame += Gdx.graphics.getDeltaTime() * 4;
            if (frame > 1) frame = 1;
        } else {
            frame -= Gdx.graphics.getDeltaTime() * 4;
            if (frame < 0) frame = 0;
        }
        leftPressed = hovering && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        rightPressed = hovering && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
    }

    private void hoverFunction(float mx, float my){
        hovering = mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public float getFrame() {
        return frame;
    }

    public boolean isHovering() {
        return hovering;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public int getState() {
        return state;
    }

    public void setState(boolean condition, int state) {
        if (!condition) return;
        this.state = state;
        stateChanged = true;
    }

    public boolean isStateChanged() {
        return stateChanged;
    }
}
