package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.ImageHandler;

public class EnemyRenderer {
    public static void backRender(SpriteBatch batch){
        if (Player.turningPosition > 0) return;
        ratRender(batch);
        catRender(batch);
//        classicCatRender(batch, false);
    }

    public static void forthRender(SpriteBatch batch){
        if (Player.turningPosition > 0) return;
        classicCatRender(batch, true);
    }

    public static void ratRender(SpriteBatch batch){
        Rat rat = Game.rat;
        String prefix = "game/enemy/Rat/Shadow/";
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

    public static void catRender(SpriteBatch batch){
        Cat cat = Game.cat;
        String prefix = "game/enemy/Cat/Shadow/";
        StringBuilder textureBuilder = new StringBuilder();
        boolean render = true;
        float x = 0, y = 0;
        if (Player.room == 0){
            if (cat.state == 1){
                textureBuilder.append("Attack/");
                if (cat.side == 0) {
                    textureBuilder.append("Left/");
                    x = 143;
                    y = 322;
                } else if (cat.side == 1) {
                    textureBuilder.append("Middle/");
                    x = 1171;
                    y = 51;
                } else {
                    textureBuilder.append("Right/");
                    x = 2221;
                    y = 196;
                }
                if (cat.move) textureBuilder.append("AttackMove").append((int) cat.frame);
                else textureBuilder.append("Attack").append((int) cat.frame + (int) cat.twitchFrame);
            } else if (cat.state == 3) {
                textureBuilder.append("Crouch/");
                if (cat.frame < 2) {
                    if (cat.side == 0) {
                        textureBuilder.append("Left/Crouch").append((int) cat.twitchFrame + 1);
                        x = 515;
                        y = 231;
                    } else {
                        textureBuilder.append("Right/Crouch").append((int) cat.twitchFrame + 1);
                        x = 2071;
                        y = 194;
                    }
                } else {
                    if (cat.side == 0) {
                        textureBuilder.append("Left/Crouch").append((int) cat.frame + 1);
                        switch ((int) cat.frame) {
                            case 2 -> {
                                x = 227;
                                y = 2;
                            }
                            case 3 -> {
                                x = 255;
                                y = 7;
                            }
                            case 4 -> {
                                x = 278;
                                y = 11;
                            }
                            case 5 -> {
                                x = 270;
                                y = 14;
                            }
                            case 6 -> {
                                x = 262;
                                y = 14;
                            }
                            case 7 -> {
                                x = 256;
                                y = 10;
                            }
                            case 8 -> {
                                x = 252;
                                y = 5;
                            }
                            case 9 -> {
                                x = 249;
                                y = 0;
                            }
                            case 10 -> {
                                x = 245;
                                y = 6;
                            }
                            case 11 -> {
                                x = 245;
                                y = 49;
                            }
                            case 12 -> {
                                x = 245;
                                y = 81;
                            }
                            case 13 -> {
                                x = 245;
                                y = 108;
                            }
                            case 14 -> {
                                x = 245;
                                y = 138;
                            }
                            case 15 -> {
                                x = 245;
                                y = 165;
                            }
                            case 16 -> {
                                x = 245;
                                y = 188;
                            }
                            case 17 -> {
                                x = 245;
                                y = 209;
                            }
                            case 18 -> {
                                x = 237;
                                y = 227;
                            }
                            case 19 -> {
                                x = 230;
                                y = 242;
                            }
                            case 20 -> {
                                x = 220;
                                y = 257;
                            }
                            case 21 -> {
                                x = 207;
                                y = 271;
                            }
                        }
                    } else {
                        textureBuilder.append("Right/Crouch").append((int) cat.frame + 1);
                        switch ((int) cat.frame) {
                            case 2 -> {
                                x = 2020;
                                y = 5;
                            }
                            case 3 -> {
                                x = 2030;
                                y = 11;
                            }
                            case 4 -> {
                                x = 2045;
                                y = 19;
                            }
                            case 5 -> {
                                x = 2066;
                                y = 20;
                            }
                            case 6 -> {
                                x = 2077;
                                y = 23;
                            }
                            case 7 -> {
                                x = 2097;
                                y = 20;
                            }
                            case 8 -> {
                                x = 2115;
                                y = 13;
                            }
                            case 9 -> {
                                x = 2120;
                                y = 6;
                            }
                            case 10 -> {
                                x = 2134;
                                y = 0;
                            }
                            case 11 -> {
                                x = 2150;
                                y = 0;
                            }
                            case 12 -> {
                                x = 2166;
                                y = 0;
                            }
                            case 13 -> {
                                x = 2176;
                                y = 0;
                            }
                            case 14 -> {
                                x = 2186;
                                y = 0;
                            }
                            case 15 -> {
                                x = 2194;
                                y = 0;
                            }
                            case 16 -> {
                                x = 2203;
                                y = 11;
                            }
                            case 17 -> {
                                x = 2212;
                                y = 38;
                            }
                            case 18 -> {
                                x = 2225;
                                y = 59;
                            }
                            case 19 -> {
                                x = 2242;
                                y = 81;
                            }
                            case 20 -> {
                                x = 2265;
                                y = 98;
                            }
                            case 21 -> {
                                x = 2280;
                                y = 114;
                            }
                        }
                    }
                }
            } else render = false;
        } else if (Player.room == 1 && cat.state == 2){
            if (cat.side == 0) {
                textureBuilder.append("Bed/Left");
                y = 216;
            } else {
                textureBuilder.append("Bed/Right");
                x = 1100;
                y = 186;
            }
        } else render = false;

        if (render) batch.draw(ImageHandler.images.get(prefix + textureBuilder), x, y);
    }

    public static void classicCatRender(SpriteBatch batch, boolean forth){
        ClassicCat cat = Game.classicCat;
        String prefix = "game/enemy/ClassicCat/Shadow/";
        StringBuilder textureBuilder = new StringBuilder();
        boolean render = true;
        float x = 0, y = 0;
        if (Player.room == 0 && cat.frame >= 1){
            textureBuilder.append("Retreat/");
            if (cat.side == 0) {
                textureBuilder.append("Left/Retreat").append((int) (59 - cat.frame));
                y = 216;
            } else {
                textureBuilder.append("Right/Retreat").append((int) (59 - cat.frame));
                x = 2532;
                y = 218;
            }
        } else render = false;

        System.out.println(prefix + textureBuilder);

        if (render) batch.draw(ImageHandler.images.get(prefix + textureBuilder), x, y);
    }
}
