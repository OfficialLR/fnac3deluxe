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
        resetDoor(6, 5, false);
    }

	@Override
	public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;
        int logic;
        int multiplier = Game.hourOfGame / 2;
        if (Game.hourOfGame == 12) multiplier = 0;
		if (state == 0) {
			logic = doorUpdate();
            if (logic == 1){
                var peekTimer = timer2;
                int mandatorySide = Game.cat.getState() == 3 || Game.cat.getState() == 1 ? Game.cat.side : -1;
                int knockType = peekTimer > 3 ? 1 : 2;
                float knockTarget = 0.1f + (Math.max(0.065f, 0.05f * peekTimer));
                if (mandatorySide != -1) side = mandatorySide;
                else side = (int) (Math.random() * 3);
                reservedSide = side;
                setHitbox(data);
                if (peekTimer > 0.175f) Game.knock.set(side, 3, knockType, knockTarget);
            } else if (logic == 2){
                timer1 = 6 - multiplier;
                if (Game.cat.isActive() && Game.cat.state == 1) {
                    if (difficulty == 2 && timer2 < 1.5f) timer2 = 1.5f;
                    if (difficulty == 1 && timer2 < 2) timer2 = 2;
                }
                setHitbox(data);
                Game.knock.retreat(side);
            } else if (logic == 3){
                Game.knock.set(side, 0, 0, 0);
                audioClass.play("walking_in");
                Player.setBlackness(3, 6, 0);
                float killTimer = 3;
                if (difficulty <= 2) killTimer += 1;
                if (difficulty <= 1) killTimer += 1;
                resetAttack(killTimer, 0.65f, 3);
                interval1 = difficulty + (int) (Math.random() * 2);
                interval2 = 2;
                interval3 = difficulty - 1;
                state = 1;
                setHitbox(data);
            }
		} else if (state == 1) {
            if (Game.cat.isActive() && Game.cat.side == Game.rat.side
                    && ((Game.cat.state == 3 && Game.cat.frame > 2)
                    || Game.cat.state == 1)){
                setJumpscare();
            }

            var time = Gdx.graphics.getDeltaTime();
            var speed = time * 16;
            logic = attackUpdate(1);

            var killTimer = timer1;
            var flashTimer = timer2;
            var healthBar = timer3;

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
                    if (interval1 == 0) {
                        flashTimer = 0.65f - 0.05f * multiplier;
                        interval1 = difficulty + (int) (Math.random() * 2);
                    } else if (interval3 == 0 || Math.random() < 0.85f) {
                        flashTimer = 0.05f;
                        interval1--;
                        interval3 = difficulty - 1;
                    } else {
                        interval3--;
                    }
                }
            } else if (logic == 2){
                if (interval2 == 0) {
                    audioClass.play("thunder");
                    blackout();
                } else {
                    interval2--;
                    killTimer = 3;
                    if (difficulty <= 2) killTimer += 1;
                    if (difficulty <= 1) killTimer += 1;
                    int chance = (int) (Math.random() * 2);
                    if (side == 0){
                        if (reservedSide == 2) side = 1;
                        else if (reservedSide == 1 || Game.classicCat.getSide() == 2) side = 2;
                        else side = 1 + chance;
                        audioClass.play("dodgeRight");
                    } else if (side == 1){
                        if (reservedSide == 0 || Game.classicCat.getSide() == 2) side = 2;
                        else if (reservedSide == 2 || Game.classicCat.getSide() == 0) side = 0;
                        else side = 2 * chance;
                        if (side == 0) audioClass.play("dodgeLeft");
                        else audioClass.play("dodgeRight");
                    } else {
                        if (reservedSide == 0) side = 1;
                        else if (reservedSide == 1 || Game.classicCat.getSide() == 0) side = 0;
                        else side = chance;
                        audioClass.play("dodgeLeft");
                    }
                    flashTimer = 0.65f - 0.05f * multiplier;
                    interval1 = difficulty + (int) (Math.random() * 2);
                    Player.snapPosition = false;
                    Player.setBlackness(1, 6, 0);
                    position = 0;
                    move = false;
                    frame = 1;
                    targetFrame = frame;
                    setHitbox(data);
                    healthBar = 3;
                }
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
            state = 2;
            Player.setBlackness(Player.blacknessTimes, 1, Player.blacknessDelay);
            audioClass.play("crawl");
            side = (int) (Math.random() * 2) * 2;
            if (Game.cat.getState() == 2){
                if (Game.cat.getSide() == 0) side = 2;
                else side = 0;
            }
            resetBed(1.5f,  10 - multiplier, 1.5f, 12);
            setHitbox(data);
        } else if (state == 2) {
            logic = bedUpdate(data, audioClass);

            if (logic == 1) {
                audioClass.play("peek");
                boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);
                if ((timer1 < 1.5f || (Game.cat.getState() == 2 && Game.cat.timer1 < 1.5f)) && lookingAway) {
                    state = 3;
                    float killTimer = 1.75f;
                    if (difficulty <= 2) killTimer += 0.75f;
                    if (difficulty <= 1) killTimer += 0.5f;
                    resetCrouch(killTimer, 2.75f);
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
            resetDoor(6 - multiplier, 5, true);
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