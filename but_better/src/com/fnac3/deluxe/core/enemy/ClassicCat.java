package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.Utils;

public class ClassicCat extends AbstractEnemy {

    String sidePath = "";

    @Override
    public void reset(Data data, int state, int side, int type, int difficulty){
        super.reset(data, state, side, type, difficulty);
        interval1 = 0;
        frame = 0;
        timer1 = 3;
        timer2 = 1.15f;
        timer3 = 0;
        setHitbox(data);
        sidePath = "";
    }

    @Override
    public void update(Data data, AudioClass audioClass) {
        if (!isActive()) return;

        if (side != -1){
            audioClass.setVolume("cat" + sidePath, frame / 60);
        }

        if (timer2 <= 0){
            frame -= Gdx.graphics.getDeltaTime() * 90;
            if (frame < 1) {
                frame = 0;
                timer1 = 8;
                timer2 = 1.15f;
                timer3 = 0;
                side = -1;
                interval1 = 0;
                hovered = false;
                audioClass.stop("cat" + sidePath);
            }
        } else {
            hovered = Player.room == 0 && !Player.turningAround && hitboxCircleCollision();

            if (hovered) {
                if (difficulty == 1) timer3 = 5;
                timer2 -= Gdx.graphics.getDeltaTime();
                if (timer2 <= 0) {
                    frame -= Gdx.graphics.getDeltaTime() * 90;
                    setHitbox(data);
                }
            }

            if ((int) frame < interval1) {
                if (interval1 == 29) frame += Gdx.graphics.getDeltaTime() * 70;
                else frame += Gdx.graphics.getDeltaTime() * 35;
                if (frame >= interval1) {
                    frame = interval1;
                    setHitbox(data);
                }
            } else if (!Player.freeze){
                if (timer3 > 0) {
                    timer3 -= Gdx.graphics.getDeltaTime();
                    if (timer3 < 0) timer3 = 0;
                } else if (!hovered){
                    if (timer2 < 1.15f) {
                        timer2 += Gdx.graphics.getDeltaTime();
                        if (timer2 > 1.15f) timer2 = 1.15f;
                    }
                    timer1 -= Gdx.graphics.getDeltaTime();
                    if (timer1 <= 0) {
                        if (side == -1) {
                            side = 2 * (int) (Math.random() * 2);
                            if (reservedSide == side){
                                if (interval2 == 1) {
                                    if (side == 0) side = 2;
                                    else side = 0;
                                    interval2 = 0;
                                } else interval2++;
                            } else interval2 = 0;
                            reservedSide = side;
                            if (side == 0) sidePath = "Left";
                            else sidePath = "Right";
                            audioClass.play("cat" + sidePath);
                            audioClass.loop("cat" + sidePath, true);
                            audioClass.setVolume("cat" + sidePath, 0);
                        }
                        if (interval1 != 58) frame++;
                        if (interval1 == 0){
                            interval1 = 29;
                        } else if (interval1 == 29){
                            interval1 = 39;
                        } else if (interval1 == 39){
                            interval1 = 48;
                        } else if (interval1 == 48){
                            interval1 = 58;
                        } else {
                            setJumpscare();
                        }
                        setHitbox(data);
                        if (type == 1) timer1 = 6;
                        else timer1 = 8;
                    }
                }
            }
        }
    }

    @Override
    protected void setHitbox(Data data) {
        hovered = false;
        hitboxSize = -1;
        Utils.setHitbox(hitbox, 0, 0);
        if (side == 0){
            if ((int) frame == 29){
                Utils.setHitbox(hitbox, 215, 616);
                hitboxSize = 75;
            } else if ((int) frame == 39){
                Utils.setHitbox(hitbox, 207, 704);
                hitboxSize = 80;
            } else if ((int) frame == 48){
                Utils.setHitbox(hitbox, 175, 680);
                hitboxSize = 85;
            } else if ((int) frame == 58){
                Utils.setHitbox(hitbox, 187, 661);
                hitboxSize = 90;
            }
        } else {
            if ((int) frame == 29){
                Utils.setHitbox(hitbox, 2877, 465);
                hitboxSize = 80;
            } else if ((int) frame == 39){
                Utils.setHitbox(hitbox, 2833, 523);
                hitboxSize = 85;
            } else if ((int) frame == 48){
                Utils.setHitbox(hitbox, 2820, 564);
                hitboxSize = 90;
            } else if ((int) frame == 58){
                Utils.setHitbox(hitbox, 2795, 619);
                hitboxSize = 90;
            }
        }

        hitboxSize = Utils.setHitboxDistance(data, hitboxSize);
    }

    @Override
    protected void setJumpscare() {
        String name = "Cat";
        String texture = "game/enemy/ClassicCat/Shadow/Jumpscare/Jumpscare";
        String sound = "shadowJumpscare";
        float timer = 0.95f;
        int frameTarget = -1;
        float pitch = 0.95f;
        Game.setJumpscare(name, texture, sound, timer, frameTarget, pitch);
    }
}
