package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.Utils;

public class Cat extends AbstractEnemy {

    @Override
    public void reset(Data data, int state, int side, int type, int difficulty){
        super.reset(data, state, side, type, difficulty);
        if (isActive()) resetBed(1.5f, 8, 0, 0);
        else this.state = 0;
        interval3 = 0;
        interval4 = 0;
        reservedSide = -1;
    }

    @Override
    public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;
        int logic;
        int multiplier = Game.hourOfGame / 2;
        if (Game.hourOfGame == 12) multiplier = 0;
        if (state == 1) {
            var time = Gdx.graphics.getDeltaTime();
            var speed = time * 16;
            logic = attackUpdate(1.35f);

            var killTimer = timer1;
            var flashTimer = timer2;
            var healthBar = timer3;

            if (Game.rat.getState() == 0 && Game.rat.getTimer1() <= 0.15f
                    && healthBar <= 0.25f) healthBar += Gdx.graphics.getDeltaTime();
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
                    if (interval1 == 0 || (interval5 < 7 - difficulty && Math.random() < 0.65f)) {
                        flashTimer = 0.2f + (int) (Math.random() * 3) * 0.08f;
                        interval1 = difficulty - 1;
                        interval5++;
                    } else {
                        if (Math.random() < 0.1f) {
                            flashTimer = 0.05f;
                        }
                        interval5 = 0;
                        interval1--;
                    }
                }
            } else if (logic == 2){
                audioClass.play("thunder");

                if (interval2 > 0) {
                    interval2--;
                    int chance = (int) (Math.random() * 2);
                    if (side == 0) {
                        side = 1 + chance;
                        if (reservedSide == side) {
                            if (interval4 == 0) interval4++;
                            else {
                                if (reservedSide == 2) side = 1;
                                else side = 2;
                                interval4 = 0;
                            }
                        } else interval4 = 0;
                    } else {
                        side = chance;
                        if (reservedSide == side) {
                            if (interval4 == 0) interval4++;
                            else {
                                if (reservedSide == 0) side = 1;
                                else side = 0;
                                interval4 = 0;
                            }
                        } else interval4 = 0;
                    }
                    reservedSide = side;
                    float newKillTimer = 1.75f;
                    if (difficulty <= 3) newKillTimer += 0.5f;
                    if (difficulty <= 2) newKillTimer += 0.75f;
                    if (difficulty <= 1) newKillTimer += 0.5f;

                    resetAttack(newKillTimer, 0.4f, 4);
                    killTimer = timer1;
                    flashTimer = timer2;
                    healthBar = timer3;
                    if (interval3 == 0) {
                        interval3++;
                    } else {
                        position = (int) (1 + (Math.random() * 2));
                        if (position == 1) frame = 3;
                        else if (position == 2) frame = 5;
                        targetFrame = frame;
                    }

                    interval1 = difficulty - 1;
                    interval5 = (int) (Math.random() * (7 - difficulty));
                    setHitbox(data);

                    lock = false;
                    Player.snapPosition = false;
                    Player.setBlackness(2, 6, 0);
                } else {
                    blackout();
                    audioClass.stop("cat");
                }
            } else if (logic == 3){
                setJumpscare();
            } else if (logic == 4){
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
            resetBed(1.5f, 22, -1, -1);
            setHitbox(data);
        } else if (state == 2) {
            if (Game.rat.isAttack() && timer2 > 8) {
                timer2 = 21 - multiplier;
            }
            logic = bedUpdate(data, audioClass);

            if (logic == 1) {
                audioClass.play("peek");
                boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);
                if (timer1 < 1.5f && lookingAway) {
                    state = 3;
                    Player.lastCharacterAttack = "Shadow";
                    float killTimer = 2;
                    if (difficulty <= 2) killTimer += 0.5f;
                    if (difficulty <= 1) killTimer += 0.5f;
                    resetCrouch(killTimer, 1.5f);
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
            Player.overlayTransparency = 1 - healthBar / 1.5f;
            logic = crouchUpdate();
            if (logic == 1) {
                setJumpscare();
            } else if (logic == 2){
                hovered = false;
                frame = 2;
                lock = true;
                setHitbox(data);
                Player.inititiateSnapPosition(side, true);
                Player.scared = true;
            }
            if (healthBar > 0) return;
            frame += Gdx.graphics.getDeltaTime() * 60;
            if (frame < 22) return;

            state = 1;
            resetAttack(0.5f, 0.25f, 1.75f);
            lock = true;
            interval1 = difficulty - 1;
            interval2 = 1;
            interval5 = (int) (Math.random() * (7 - difficulty));
            setHitbox(data);
        }
    }

    @Override
    protected void setHitbox(Data data) {
        hovered = false;
        hitboxSize = -1;
        Utils.setHitbox(hitbox, 0, 0);
        if (state == 1 && !move) {
            if (side == 0) {
                hitboxSize = 70;
                if (position == 0) Utils.setHitbox(hitbox, 365, 742);
                else if (position == 1) Utils.setHitbox(hitbox, 277, 614);
                else Utils.setHitbox(hitbox, 467, 607);
            } else if (side == 1) {
                hitboxSize = 80;
                if (position == 0) Utils.setHitbox(hitbox, 1529, 724);
                else if (position == 1) Utils.setHitbox(hitbox, 1339, 582);
                else Utils.setHitbox(hitbox, 1705, 610);
            } else {
                hitboxSize = 90;
                if (position == 0) Utils.setHitbox(hitbox, 2560, 890);
                else if (position == 1) Utils.setHitbox(hitbox, 2390, 717);
                else Utils.setHitbox(hitbox, 2730, 671);
            }
        } else if (state == 2){
            if (side == 0) {
                Utils.setHitbox(hitbox, 0, 216);
                Utils.setHitbox(hitboxDimension, 786, 501);
            } else {
                Utils.setHitbox(hitbox, 1100, 186);
                Utils.setHitbox(hitboxDimension, 948, 520);
            }
        } else if (state == 3 && timer2 > 0){
            hitboxSize = 80;
            if (side == 0) Utils.setHitbox(hitbox, 656, 438);
            else Utils.setHitbox(hitbox, 2349, 512);
        }

        hitboxSize = Utils.setHitboxDistance(data, hitboxSize);
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

    public void audioUpdate(AudioClass audioClass){
        if (!isActive()) return;
        if (!audioClass.isPlaying("cat")) {
            audioClass.play("cat");
            audioClass.loop("cat", true);
            audioClass.setVolume("cat", 0);
        }
        float catVolume = audioClass.getVolume("cat");
        float catPitch = audioClass.getPitch("cat");
        if (state == 2 && timer2 > 8){
            if (catVolume < 0.2f) catVolume += Gdx.graphics.getDeltaTime() / 2;
            if (catVolume > 0.2f) catVolume = 0.2f;
        } else {
            if (catVolume < 0.65f) catVolume += Gdx.graphics.getDeltaTime() / 2;
            if (catVolume > 0.65f) catVolume = 0.65f;

            if (catPitch < 2) catPitch += Gdx.graphics.getDeltaTime() / 85;
            if (catPitch > 2) catPitch = 2;
        }
        audioClass.setVolume("cat", catVolume);
        audioClass.setPitch("cat", catPitch);
    }
}
