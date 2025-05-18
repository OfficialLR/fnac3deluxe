package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;

public class Cat extends AbstractEnemy {

    @Override
    public void reset(Data data, int state, int side, int type, int difficulty){
        super.reset(data, state, side, type, difficulty);
        if (isActive()) resetBed(1.5f, 8, 0, 0);
    }

    @Override
    public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;
        int logic;
        if (state == 1) {
            var time = Gdx.graphics.getDeltaTime();
            var speed = type == 0 ? time * 15 : time * 18;
            logic = attackUpdate();

            var killTimer = timer1;
            var flashTimer = timer2;
            var healthBar = timer3;

            int multiplier = Game.hourOfGame / 2;
            if (Game.hourOfGame == 12) multiplier = 0;

            if (logic == 1){
                if (frame == targetFrame) {
                    boolean clockwise = Math.random() < 0.5f;
                    audioClass.play("dodge");
                    setHitbox(data);
                    if (frame == 1){
                        if (clockwise) targetFrame = 3;
                        else {
                            frame = 7;
                            targetFrame = 5;
                        }
                    } else if (frame == 3) {
                        if (clockwise) targetFrame = 5;
                        else targetFrame = 1;
                    } else if (frame == 5){
                        if (clockwise) targetFrame = 7;
                        else targetFrame = 3;
                    }
                }

                if (frame < targetFrame){
                    frame += speed;
                    if (frame >= targetFrame) frame = targetFrame;
                } else if (frame > targetFrame){
                    frame -= speed;
                    if (frame <= targetFrame) frame = targetFrame;
                }

                if (frame == targetFrame){
                    if (frame == 7) frame = 1;
                    if (targetFrame == 7) targetFrame = 1;
                    if ((int) frame == 1) position = 0;
                    else if ((int) frame == 3) position = 1;
                    else if ((int) frame == 5) position = 2;

                    move = false;
                    setHitbox(data);
                    if (getInterval1() == 0 || Math.random() < 0.5f) {
                        if (Math.random() < 0.5f) {
                            flashTimer = 0.125f * (int) (Math.random() * 3) + 1;
                            setInterval1(3);
                        }
                    } else {
                        flashTimer = 0.01f;
                        setInterval1(getInterval1() - 1);
                    }
                }
            } else if (logic == 2){
                audioClass.play("thunder");
                blackout();
            } else if (logic == 3){
                setJumpscare();
            } else if (logic == 4){
                Player.lastCharacterAttack = "Shadow";
                killTimer = 0.5f;
            }

            timer1 = killTimer;
            timer2 = flashTimer;
            timer3 = healthBar;

            if (healthBar > 0 || lock || Player.blacknessTimes > 0) return;

            if (getInterval2() == 0) {
                state = 2;
                Player.setBlackness(Player.blacknessTimes, 1, Player.blacknessDelay);
                audioClass.play("crawl");
                side = (int) (Math.random() * 2) * 2;
                resetBed(1.5f, 10, 1.5f, 12);
                setHitbox(data);
            } else {
                setInterval2(getInterval2() - 1);
                int chance = (int) (Math.random() * 2);
                if (side == 0){
                    side = 1 + chance;
                } else {
                    side = chance;
                }
                resetAttack(2, 0.4f, 3.5f);
                setInterval1(3);
                setInterval2(1);
                setHitbox(data);
            }
        } else if (state == 2) {
            logic = bedUpdate(data, audioClass);

            if (logic == 1) {
                audioClass.play("peek");
                boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);
                if (timer1 < 1.5f && lookingAway) {
                    state = 3;
                    resetCrouch(1.75f, 2.25f);
                    setHitbox(data);
                } else {
                    logic = 2;
                }
            }
            if (logic == 2){
                setJumpscare();
            }
        } else if (state == 3) {
            var healthBar = timer2;
            Player.overlayTransparency = healthBar / 2.25f;
            logic = crouchUpdate();
            if (logic == 1) {
                setJumpscare();
            } else if (logic == 2){
                hovered = false;
                frame = 2;
                lock = true;
                Player.inititiateSnapPosition(side);
                Player.scared = true;
            }
            if (healthBar > 0) return;
            frame += Gdx.graphics.getDeltaTime();
            if (frame < 22) return;

            state = 1;
            resetAttack(0.75f, 0.4f, 1.75f);
            setInterval1(3);
            setInterval2(1);
            setHitbox(data);
        } else if (state == 4) {
            frame -= Gdx.graphics.getDeltaTime() * 25;
            if (frame < 1) state = 0;
        }
    }

    @Override
    protected void setHitbox(Data data) {

    }

    @Override
    protected void setJumpscare() {
        String name = "Cat";
        String texture = "game/enemy/Cat/Shadow/Jumpscare/Jumpscare";
        String sound = "shadowJumpscare";
        float timer = 0.9f;
        int frameTarget = -1;
        float pitch = 1;
        Game.setJumpscare(name, texture, sound, timer, frameTarget, pitch);
    }
}
