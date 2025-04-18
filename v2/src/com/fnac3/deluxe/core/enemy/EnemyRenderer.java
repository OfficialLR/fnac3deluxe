package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.ImageHandler;

public class EnemyRenderer {
    public static void backRender(SpriteBatch batch){
        if (Player.turningPosition > 0) return;
        ratRender(batch);
        catRender(batch, false);
    }

    public static void forthRender(SpriteBatch batch){
        if (Player.turningPosition > 0) return;
        catRender(batch, true);
    }

    public static void ratRender(SpriteBatch batch){
        Rat rat = Game.rat;
        String prefix = "game/enemy/Rat/" + (rat.type == 0 ? "Monster" : "Shadow") + "/";
        StringBuilder textureBuilder = new StringBuilder();
        boolean render = true;
        float x = 0, y = 0;
        if (Player.room == 0){
            if (rat.state == 0 && rat.frame >= 1){
                textureBuilder.append("Door/Peek/");
                if (rat.side == 0){
                    textureBuilder.append("Left/");
                    x = 380;
                    y = 372;
                } else if (rat.side == 1){
                    textureBuilder.append("Middle/");
                    x = 1411;
                    y = 382;
                } else {
                    textureBuilder.append("Right/");
                    x = 2483;
                    y = 321;
                }
                textureBuilder.append("Peek").append((int) (14 - rat.frame));
            } else if (rat.state == 1){
                textureBuilder.append("Attack/");
                if (rat.side == 0) {
                    textureBuilder.append("Left/");
                    x = 142;
                    y = 327;
                } else if (rat.side == 1) {
                    textureBuilder.append("Middle/");
                    x = 1144;
                    y = 45;
                } else {
                    textureBuilder.append("Right/");
                    x = 2165;
                    y = 195;
                }
                if (rat.move) textureBuilder.append("AttackMove").append((int) rat.frame);
                else textureBuilder.append("Attack").append((int) rat.frame + (int) rat.twitchFrame);
            } else if (rat.state == 3){
                textureBuilder.append("Crouch/");
                if (rat.side == 0){
                    textureBuilder.append("Left/Crouch").append((int) rat.twitchFrame + 1);
                    x = 415;
                    y = 217;
                } else {
                    textureBuilder.append("Right/Crouch").append((int) rat.twitchFrame + 1);
                    x = 2128;
                    y = 195;
                }
            } else if (rat.state == 4 && rat.frame >= 1){
                textureBuilder.append("Door/Leaving/");
                if (rat.side == 0){
                    textureBuilder.append("Left/Leaving").append((int) (19 - rat.frame));
                    x = 280;
                    y = 376;
                } else {
                    textureBuilder.append("Right/Leaving").append((int) (19 - rat.frame));
                    x = 2490;
                    y = 285;
                }
            } else render = false;
        } else if (Player.room == 2) {
            if (rat.state == 2 && rat.frame < 12) {
                textureBuilder.append("Tape/Tape").append((int) rat.frame + 1);
                x = 194;
                y = 374;
            } else render = false;
        } else if (rat.state == 2){
            if (rat.side == 0) {
                textureBuilder.append("Bed/Left");
                y = 179;
            } else {
                textureBuilder.append("Bed/Right");
                x = 1438;
                y = 214;
            }
        } else render = false;

        if (render) batch.draw(ImageHandler.images.get(prefix + textureBuilder), x, y);
    }

    public static void catRender(SpriteBatch batch, boolean forth){

    }
}
