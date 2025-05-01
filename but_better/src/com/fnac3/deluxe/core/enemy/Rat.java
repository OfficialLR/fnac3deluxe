package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.Utils;

public class Rat extends AbstractEnemy {

    @Override
    public void reset(Data data, int state, int side, int type, int difficulty){
        super.reset(data, state, side, type, difficulty);
        resetDoor(type == 0 ? 6: 4, 5, false);
    }

	@Override
	public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;
        int logic;
		if (state == 0) {
			logic = doorUpdate(data);
            if (logic == 1){
                var peekTimer = timer2;
                int mandatorySide = -1;
                int knockType = peekTimer > 3 ? 1 : 2;
                float knockTarget = 0.1f + (Math.max(0.065f, 0.05f * peekTimer));
                if (mandatorySide != -1) side = mandatorySide;
                else side = (int) (Math.random() * 3);
                setHitbox(data);
                Game.knock.set(side, 3, knockType, knockTarget);
            } else if (logic == 2){
                timer1 = type == 0 ? 6 : 4;
                setHitbox(data);
                Game.knock.retreat(side);
            } else if (logic == 3){
                Game.knock.set(side, 0, 0, 0);
                audioClass.play("walking_in");
                Player.setBlackness(3, 6, 0);
                resetAttack(5, 0.65f, type == 0 ? 5 : 4);
                state = 1;
                setHitbox(data);
            }
		} else if (state == 1) {
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
                    killTimer = 0.75f;
                    if (type == 0){
                        if (Math.random() < 0.85f) flashTimer = (float) (0.35f + 0.05f * Math.random() * 5);
                    } else {
                        if (Math.random() < 0.75f) flashTimer = 0.3f - 0.05f * multiplier;
                    }
                }
            } else if (logic == 2){
                audioClass.play("thunder");
                blackout();
            } else if (logic == 3){
                setJumpscare();
            } else if (logic == 4){
                if (type == 0) Player.lastCharacterAttack = "RatCat";
                else Player.lastCharacterAttack = "Shadow";
                killTimer = 0.75f;
            }

            timer1 = killTimer;
            timer2 = flashTimer;
            timer3 = healthBar;

            if (healthBar > 0 || lock || Player.blacknessTimes > 0) return;
            state = 2;
            Player.setBlackness(Player.blacknessTimes, 1, Player.blacknessDelay);
            audioClass.play("crawl");
            side = (int) (Math.random() * 2) * 2;
            resetBed(1.5f, 10, 1.5f, 12);
            setHitbox(data);
        } else if (state == 2) {
            logic = bedUpdate(data, audioClass);

            if (logic == 1) {
                audioClass.play("peek");
                boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);
                if (timer1 < 1.5f && lookingAway) {
                    state = 3;
                    resetCrouch(1.75f, 2.75f);
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
            logic = crouchUpdate();
            if (logic == 1) {
                setJumpscare();
            } else if (logic == 2){
                audioClass.play("thunder");
                blackout();
            }
            if (healthBar > 0 || Player.blacknessTimes > 0 || Player.freeze) return;
            state = 4;
            audioClass.play("leave");
            Player.setBlackness(Player.blacknessTimes, 1, Player.blacknessDelay);
            resetDoor(9, 5, true);
            setHitbox(data);
        } else if (state == 4) {
            frame -= Gdx.graphics.getDeltaTime() * 25;
            if (frame < 1) state = 0;
        }
	}

    @Override
    protected void setHitbox(Data data){
        hovered = false;
        hitboxSize = -1;
        Utils.setHitbox(hitbox, 0, 0);
        if (state == 0 && timer1 <= 0){
            if (side == 0){
                hitboxSize = 65;
                Utils.setHitbox(hitbox, 481, 663);
            } else if (side == 1){
                hitboxSize = 70;
                Utils.setHitbox(hitbox, 1531, 696);
            } else if (side == 2){
                hitboxSize = 80;
                Utils.setHitbox(hitbox, 2525, 715);
            }
        } else if (state == 1 && !move) {
            if (side == 0) {
                hitboxSize = 70;
                if (position == 0) Utils.setHitbox(hitbox, 376, 765);
                else if (position == 1) Utils.setHitbox(hitbox, 289, 621);
                else Utils.setHitbox(hitbox, 470, 595);
            } else if (side == 1) {
                hitboxSize = 80;
                if (position == 0) Utils.setHitbox(hitbox, 1521, 720);
                else if (position == 1) Utils.setHitbox(hitbox, 1315, 574);
                else Utils.setHitbox(hitbox, 1686, 604);
            } else {
                hitboxSize = 90;
                if (position == 0) Utils.setHitbox(hitbox, 2560, 869);
                else if (position == 1) Utils.setHitbox(hitbox, 2402, 680);
                else Utils.setHitbox(hitbox, 2734, 651);
            }
        } else if (state == 2){
            if (side == 0) {
                Utils.setHitbox(hitbox, 0, 179);
                Utils.setHitbox(hitboxDimension, 915, 529);
            } else {
                Utils.setHitbox(hitbox, 1438, 214);
                Utils.setHitbox(hitboxDimension, 610, 521);
            }
        } else if (state == 3){
            hitboxSize = 80;
            if (side == 0) Utils.setHitbox(hitbox, 710, 450);
            else Utils.setHitbox(hitbox, 2361, 468);
        }

        hitboxSize = Utils.setHitboxDistance(data, hitboxSize);
    }

    @Override
    protected void setJumpscare(){
        String name = "Rat";
        String texture = "game/enemy/Rat/Shadow/Jumpscare/Jumpscare";
        String sound = "shadowJumpscare";
        float timer = 0.9f;
        int frameTarget = -1;
        float pitch = 1;
        Game.setJumpscare(name, texture, sound, timer, frameTarget, pitch);
    }
}