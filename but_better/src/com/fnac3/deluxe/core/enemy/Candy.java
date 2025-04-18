package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;
import com.fnac3.deluxe.core.util.Utils;

public class Candy {
    public static float sideCooldownTimer;
    public static float cooldownTimer;
    public static float position;
    public static boolean retreat;
    public static int side;
    public static String sideString;
    public static boolean active;
    public static float volume;
    public static int lastPlayerSide;

    public static boolean jumpscareI;
    public static boolean jumpscare;
    public static float jumpscareTime;
    public static boolean shadow;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static void reset(boolean active, boolean shadow){
        Candy.active = active;
        Candy.shadow = shadow;
        jumpscare = false;
        jumpscareTime = 0.9f;
        if (!active) return;
        retreat = true;

        volume = 0;
        sideCooldownTimer = 0;
        cooldownTimer = 0;
        side = -1;
        position = 0;
        lastPlayerSide = 1;
    }

    public static void update(AudioClass audioClass) {
        if (!active) return;

        if (Player.side != -1) lastPlayerSide = Player.side;

        boolean shadowCat = ShadowCat.room != 4 || ShadowCat.side != lastPlayerSide;
        if ((lastPlayerSide == 0 || lastPlayerSide == 2) && (side == -1 || lastPlayerSide == side) && shadowCat) {
            sideCooldownTimer += Gdx.graphics.getDeltaTime();
        } else {
            sideCooldownTimer -= Gdx.graphics.getDeltaTime();
        }

        if (sideCooldownTimer <= 0) {
            sideCooldownTimer = 0;
            cooldownTimer = 0;
            retreat = true;
        }
        if (sideCooldownTimer >= 2) {
            sideCooldownTimer = 2;
            retreat = false;
        }

        if (sideCooldownTimer == 2 && side == -1) {
            side = lastPlayerSide;
            position = 1;
            sideString = side == 0 ? "Left" : "Right";
            audioClass.play("OldCandy" + sideString);
            audioClass.setVolume("OldCandy" + sideString, 0);
        }

        if (side == -1) return;
        if (!retreat){
            if (volume < 1) {
                volume += Gdx.graphics.getDeltaTime() * 4;
                if (volume > 1) volume = 1;
                audioClass.setVolume("OldCandy" + sideString, volume);
            }
            cooldownTimer += Gdx.graphics.getDeltaTime();
            float positionTarget = cooldownTimer <= 8 ? 30 : 59;

            if (position < positionTarget) {
                if (position < 30) {
                    position += Gdx.graphics.getDeltaTime() * 45;
                } else {
                    position += Gdx.graphics.getDeltaTime() * 24;
                }
                if (position > positionTarget) {
                    position = positionTarget;
                }
            }
        } else if (position > 1){
            if (volume > 0) {
                volume -= Gdx.graphics.getDeltaTime() * 4;
                if (volume <= 0) {
                    volume = 0;
                    audioClass.stop("OldCandy" + sideString);
                } else audioClass.setVolume("OldCandy" + sideString, volume);
            }

            position -= Gdx.graphics.getDeltaTime() * 60;
            if (position <= 1) {
                position = 0;
                side = -1;
            }
        }
    }

    public static void render(SpriteBatch batch){
        if (!active) return;

        String type = shadow ? "Shadow" : "Monster";
        String texture = null;
        int x = 0;
        int y = 0;
        if (side != -1){
            texture = "Retreat/" + sideString + "/" + (int) position;
            y = 216;
        }

        if (side == 2){
            x = 2532;
        }

        texture = "game/Candy/" + type + "/" + texture;

        if (texture == null || Player.room != 0 || Player.turningPosition > 0) return;
        batch.draw(ImageHandler.images.get(texture), x, y);
    }

    public static void hitboxes(Data data){
        hitboxDistance = 80;
        Utils.setHitbox(hitboxPosition, 0, 0);

        if (side == 0){
            switch ((int) position){
                case 24 -> Utils.setHitbox(hitboxPosition, 0, 0);
            }
        } else if (side == 2){
            switch ((int) position){
                case 24 -> Utils.setHitbox(hitboxPosition, 0, 0);
            }
        } else hitboxDistance = -1;

        hitboxDistance = Utils.setHitboxDistance(data, hitboxDistance);
    }
}
