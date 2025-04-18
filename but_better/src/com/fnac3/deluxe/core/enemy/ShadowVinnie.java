package com.fnac3.deluxe.core.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;
import com.fnac3.deluxe.core.util.Utils;

import java.util.Random;

public class ShadowVinnie {

    //general variables
    public static int ai;
    public static int room;
    public static int side = -1;
    public static float cooldownTimer;
    public static float twitchPosition;
    public static boolean shaking;
    public static boolean dodgePlaying;
    public static float timeToFlash;
    public static float patienceTimer;

    //attack
    public static boolean attack;
    public static float attackPosition;
    public static float framesToMove;
    public static float attackTime;
    public static int jumps;
    public static boolean jumping;
    public static int jumpTarget;
    public static float jumpAnimation;
    private static int jumpCase;

    //bed
    public static boolean bedSpotted;
    public static float bedPatienceTimer;
    public static boolean tapeSpotted;
    public static float timeUntilWeasel;
    public static boolean tapeWeasel;
    public static float tapePosition;

    public static boolean pause;

    private static float vinnieLaughTime;
    private static int vinnielaugh;

    public static float hitboxDistance;
    public static float[] hitboxPosition;

    public static boolean hitboxHit;

    public static void reset(AudioClass audioClass){
        jumping = false;
        if (hitboxPosition == null){
            hitboxPosition = new float[]{-1, -1};
        } else {
            hitboxPosition[0] = -1;
            hitboxPosition[1] = -1;
        }
        vinnieLaughTime = 2;
        jumpCase = 0;
        tapeWeasel = false;
        cooldownTimer = 5;
        bedSpotted = false;
        jumpTarget = 0;
        jumpAnimation = 0;
        dodgePlaying = false;
        vinnielaugh = 0;

        if (ai != 0) {
            jumps = 5;
            room = 1;
            attackTime = 2;
            timeToFlash = 0.65f;
            attackPosition = 0;
            framesToMove = 0;
            attack = false;
            patienceTimer = 0.75f;
            shaking = false;
            hitboxHit = false;
            if (Math.random() < 0.5) {
                side = 0;
            } else {
                side = 2;
            }
            audioClass.play("walking_in");
            Player.blacknessMultiplier = 6;
            Player.blacknessTimes = 3;
            Player.blacknessDelay = 0;
        } else {
            room = 0;
        }
    }

    private static void setJumpscare(){
        Game.setJumpscare("Vinnie",
                "game/Shadow Vinnie/Jumpscare/Jumpscare",
                "shadowJumpscare",
                0.9f,
                -1,
                1);
    }

    public static void input(){
        if (!Player.turningAround && Player.flashlightAlpha > 0) {
            hitboxCollided();
        }
    }

    public static void update(Random random, Data data, AudioClass audioClass){
        pause = Player.freeze;
        vinnieLaughTime -= Gdx.graphics.getDeltaTime();
        if (vinnieLaughTime <= 0){
            vinnieLaughTime += 12;
            if (vinnielaugh == 0){
                vinnielaugh = 1 + random.nextInt(4);
            } else if (vinnielaugh == 1){
                vinnielaugh = 2 + random.nextInt(3);
            } else if (vinnielaugh == 2){
                if (random.nextInt(2) == 0){
                    vinnielaugh = 1;
                } else {
                    vinnielaugh = 3 + random.nextInt(2);
                }
            } else if (vinnielaugh == 3){
                if (random.nextInt(2) == 0){
                    vinnielaugh = 1 + random.nextInt(2);
                } else {
                    vinnielaugh = 4;
                }
            } else {
                vinnielaugh = 1 + random.nextInt(3);
            }

            audioClass.play("laugh" + vinnielaugh);
            audioClass.setVolume("laugh" + vinnielaugh, 0.75f);
        }

        switch (room) {
            case 1:
                roomMechanic(data, random, audioClass);
                break;
            case 2:
                bedMechanic(data, audioClass);
                break;
            case 3:
                crouchMechanic();
                break;
        }

        if (tapeSpotted && tapePosition >= 0){
            tapePosition -= Gdx.graphics.getDeltaTime() * 30;
            if (tapePosition < 0){
                tapePosition = 0;
            }
        }

        hitboxes(data);
    }

    public static void hitboxCollided(){
        float mx = Player.flashlightPosition[0];
        float my = Player.flashlightPosition[1];
        float lineA = Math.abs(mx - hitboxPosition[0]);
        float lineB = Math.abs(my - hitboxPosition[1]);

        if (Math.hypot(lineA, lineB) <= hitboxDistance){
            if (Player.room == 0 && room == 1 && attack){
                hitboxHit = true;
            } else if (Player.room == 1 && room == 2) {
                hitboxHit = true;
            } else if (Player.room == 0 && room == 3 && timeToFlash > 0){
                hitboxHit = true;
            }
        }
    }

    public static void hitboxes(Data data){
        hitboxHit = false;
        hitboxPosition[0] = -1;
        hitboxPosition[1] = -1;
        hitboxDistance = 0;
        switch (room){
            case 1:
                if (jumpAnimation != 0) break;
                if (side == 0){
                    hitboxDistance = 80;
                    switch ((int) attackPosition) {
                        case 0 -> Utils.setHitbox(hitboxPosition, 372, 887);
                        case 1 -> Utils.setHitbox(hitboxPosition, 352, 882);
                        case 2 -> Utils.setHitbox(hitboxPosition, 332, 873);
                        case 3 -> Utils.setHitbox(hitboxPosition, 311, 867);
                        case 4 -> Utils.setHitbox(hitboxPosition, 293, 857);
                        case 5 -> Utils.setHitbox(hitboxPosition, 283, 848);
                        case 6 -> Utils.setHitbox(hitboxPosition, 275, 837);
                        case 7 -> Utils.setHitbox(hitboxPosition, 266, 826);
                        case 8 -> Utils.setHitbox(hitboxPosition, 257, 813);
                        case 9 -> Utils.setHitbox(hitboxPosition, 251, 799);
                        case 10 -> Utils.setHitbox(hitboxPosition, 245, 783);
                        case 11 -> Utils.setHitbox(hitboxPosition, 241, 773);

                        case 12 -> Utils.setHitbox(hitboxPosition, 239, 760);
                        case 13 -> Utils.setHitbox(hitboxPosition, 233, 745);
                        case 14 -> Utils.setHitbox(hitboxPosition, 228, 731);
                        case 15 -> Utils.setHitbox(hitboxPosition, 233, 717);
                        case 16 -> Utils.setHitbox(hitboxPosition, 238, 702);
                        case 17 -> Utils.setHitbox(hitboxPosition, 250, 687);
                        case 18 -> Utils.setHitbox(hitboxPosition, 264, 672);
                        case 19 -> Utils.setHitbox(hitboxPosition, 279, 657);
                        case 20 -> Utils.setHitbox(hitboxPosition, 294, 640);
                        case 21 -> Utils.setHitbox(hitboxPosition, 313, 633);
                        case 22 -> Utils.setHitbox(hitboxPosition, 332, 626);
                        case 23 -> Utils.setHitbox(hitboxPosition, 352, 618);

                        case 24 -> Utils.setHitbox(hitboxPosition, 371, 612);
                        case 25 -> Utils.setHitbox(hitboxPosition, 388, 615);
                        case 26 -> Utils.setHitbox(hitboxPosition, 404, 619);
                        case 27 -> Utils.setHitbox(hitboxPosition, 420, 627);
                        case 28 -> Utils.setHitbox(hitboxPosition, 436, 635);
                        case 29 -> Utils.setHitbox(hitboxPosition, 450, 641);
                        case 30 -> Utils.setHitbox(hitboxPosition, 466, 649);
                        case 31 -> Utils.setHitbox(hitboxPosition, 476, 663);
                        case 32 -> Utils.setHitbox(hitboxPosition, 487, 679);
                        case 33 -> Utils.setHitbox(hitboxPosition, 497, 699);
                        case 34 -> Utils.setHitbox(hitboxPosition, 505, 718);
                        case 35 -> Utils.setHitbox(hitboxPosition, 511, 736);

                        case 36 -> Utils.setHitbox(hitboxPosition, 516, 752);
                        case 37 -> Utils.setHitbox(hitboxPosition, 512, 766);
                        case 38 -> Utils.setHitbox(hitboxPosition, 508, 779);
                        case 39 -> Utils.setHitbox(hitboxPosition, 503, 790);
                        case 40 -> Utils.setHitbox(hitboxPosition, 498, 799);
                        case 41 -> Utils.setHitbox(hitboxPosition, 486, 813);
                        case 42 -> Utils.setHitbox(hitboxPosition, 475, 826);
                        case 43 -> Utils.setHitbox(hitboxPosition, 463, 842);
                        case 44 -> Utils.setHitbox(hitboxPosition, 450, 856);
                        case 45 -> Utils.setHitbox(hitboxPosition, 430, 867);
                        case 46 -> Utils.setHitbox(hitboxPosition, 410, 876);
                        case 47 -> Utils.setHitbox(hitboxPosition, 391, 883);
                    }
                } else if (side == 1){
                    hitboxDistance = 95;
                    switch ((int) attackPosition) {
                        case 0 -> Utils.setHitbox(hitboxPosition, 1475, 902);
                        case 1 -> Utils.setHitbox(hitboxPosition, 1449, 899);
                        case 2 -> Utils.setHitbox(hitboxPosition, 1423, 893);
                        case 3 -> Utils.setHitbox(hitboxPosition, 1395, 886);
                        case 4 -> Utils.setHitbox(hitboxPosition, 1369, 878);
                        case 5 -> Utils.setHitbox(hitboxPosition, 1346, 863);
                        case 6 -> Utils.setHitbox(hitboxPosition, 1325, 849);
                        case 7 -> Utils.setHitbox(hitboxPosition, 1305, 827);
                        case 8 -> Utils.setHitbox(hitboxPosition, 1285, 805);
                        case 9 -> Utils.setHitbox(hitboxPosition, 1274, 788);
                        case 10 -> Utils.setHitbox(hitboxPosition, 1264, 769);
                        case 11 -> Utils.setHitbox(hitboxPosition, 1261, 744);

                        case 12 -> Utils.setHitbox(hitboxPosition, 1259, 719);
                        case 13 -> Utils.setHitbox(hitboxPosition, 1271, 694);
                        case 14 -> Utils.setHitbox(hitboxPosition, 1279, 666);
                        case 15 -> Utils.setHitbox(hitboxPosition, 1288, 646);
                        case 16 -> Utils.setHitbox(hitboxPosition, 1298, 626);
                        case 17 -> Utils.setHitbox(hitboxPosition, 1314, 615);
                        case 18 -> Utils.setHitbox(hitboxPosition, 1332, 603);
                        case 19 -> Utils.setHitbox(hitboxPosition, 1351, 590);
                        case 20 -> Utils.setHitbox(hitboxPosition, 1371, 576);
                        case 21 -> Utils.setHitbox(hitboxPosition, 1398, 568);
                        case 22 -> Utils.setHitbox(hitboxPosition, 1425, 559);
                        case 23 -> Utils.setHitbox(hitboxPosition, 1453, 550);

                        case 24 -> Utils.setHitbox(hitboxPosition, 1480, 539);
                        case 25 -> Utils.setHitbox(hitboxPosition, 1507, 546);
                        case 26 -> Utils.setHitbox(hitboxPosition, 1535, 550);
                        case 27 -> Utils.setHitbox(hitboxPosition, 1561, 559);
                        case 28 -> Utils.setHitbox(hitboxPosition, 1587, 566);
                        case 29 -> Utils.setHitbox(hitboxPosition, 1609, 579);
                        case 30 -> Utils.setHitbox(hitboxPosition, 1630, 590);
                        case 31 -> Utils.setHitbox(hitboxPosition, 1649, 602);
                        case 32 -> Utils.setHitbox(hitboxPosition, 1666, 610);
                        case 33 -> Utils.setHitbox(hitboxPosition, 1679, 632);
                        case 34 -> Utils.setHitbox(hitboxPosition, 1689, 652);
                        case 35 -> Utils.setHitbox(hitboxPosition, 1696, 676);

                        case 36 -> Utils.setHitbox(hitboxPosition, 1701, 701);
                        case 37 -> Utils.setHitbox(hitboxPosition, 1698, 725);
                        case 38 -> Utils.setHitbox(hitboxPosition, 1693, 747);
                        case 39 -> Utils.setHitbox(hitboxPosition, 1685, 769);
                        case 40 -> Utils.setHitbox(hitboxPosition, 1675, 789);
                        case 41 -> Utils.setHitbox(hitboxPosition, 1657, 813);
                        case 42 -> Utils.setHitbox(hitboxPosition, 1638, 836);
                        case 43 -> Utils.setHitbox(hitboxPosition, 1617, 853);
                        case 44 -> Utils.setHitbox(hitboxPosition, 1595, 869);
                        case 45 -> Utils.setHitbox(hitboxPosition, 1566, 881);
                        case 46 -> Utils.setHitbox(hitboxPosition, 1536, 892);
                        case 47 -> Utils.setHitbox(hitboxPosition, 1505, 898);
                    }
                } else {
                    hitboxDistance = 110;
                    switch ((int) attackPosition){
                        case 0 -> Utils.setHitbox(hitboxPosition, 2554, 901);
                        case 1 -> Utils.setHitbox(hitboxPosition, 2530, 896);
                        case 2 -> Utils.setHitbox(hitboxPosition, 2507, 891);
                        case 3 -> Utils.setHitbox(hitboxPosition, 2482, 884);
                        case 4 -> Utils.setHitbox(hitboxPosition, 2458, 878);
                        case 5 -> Utils.setHitbox(hitboxPosition, 2441, 863);
                        case 6 -> Utils.setHitbox(hitboxPosition, 2422, 850);
                        case 7 -> Utils.setHitbox(hitboxPosition, 2404, 835);
                        case 8 -> Utils.setHitbox(hitboxPosition, 2385, 821);
                        case 9 -> Utils.setHitbox(hitboxPosition, 2380, 803);
                        case 10 -> Utils.setHitbox(hitboxPosition, 2375, 784);
                        case 11 -> Utils.setHitbox(hitboxPosition, 2370, 763);

                        case 12 -> Utils.setHitbox(hitboxPosition, 2364, 744);
                        case 13 -> Utils.setHitbox(hitboxPosition, 2369, 727);
                        case 14 -> Utils.setHitbox(hitboxPosition, 2375, 708);
                        case 15 -> Utils.setHitbox(hitboxPosition, 2382, 687);
                        case 16 -> Utils.setHitbox(hitboxPosition, 2389, 665);
                        case 17 -> Utils.setHitbox(hitboxPosition, 2408, 654);
                        case 18 -> Utils.setHitbox(hitboxPosition, 2430, 645);
                        case 19 -> Utils.setHitbox(hitboxPosition, 2449, 635);
                        case 20 -> Utils.setHitbox(hitboxPosition, 2469, 623);
                        case 21 -> Utils.setHitbox(hitboxPosition, 2489, 618);
                        case 22 -> Utils.setHitbox(hitboxPosition, 2513, 612);
                        case 23 -> Utils.setHitbox(hitboxPosition, 2535, 607);

                        case 24 -> Utils.setHitbox(hitboxPosition, 2558, 602);
                        case 25 -> Utils.setHitbox(hitboxPosition, 2579, 606);
                        case 26 -> Utils.setHitbox(hitboxPosition, 2599, 609);
                        case 27 -> Utils.setHitbox(hitboxPosition, 2620, 611);
                        case 28 -> Utils.setHitbox(hitboxPosition, 2641, 613);
                        case 29 -> Utils.setHitbox(hitboxPosition, 2666, 623);
                        case 30 -> Utils.setHitbox(hitboxPosition, 2691, 634);
                        case 31 -> Utils.setHitbox(hitboxPosition, 2718, 643);
                        case 32 -> Utils.setHitbox(hitboxPosition, 2744, 653);
                        case 33 -> Utils.setHitbox(hitboxPosition, 2750, 674);
                        case 34 -> Utils.setHitbox(hitboxPosition, 2757, 694);
                        case 35 -> Utils.setHitbox(hitboxPosition, 2765, 714);

                        case 36 -> Utils.setHitbox(hitboxPosition, 2773, 735);
                        case 37 -> Utils.setHitbox(hitboxPosition, 2765, 758);
                        case 38 -> Utils.setHitbox(hitboxPosition, 2756, 780);
                        case 39 -> Utils.setHitbox(hitboxPosition, 2750, 801);
                        case 40 -> Utils.setHitbox(hitboxPosition, 2742, 822);
                        case 41 -> Utils.setHitbox(hitboxPosition, 2721, 837);
                        case 42 -> Utils.setHitbox(hitboxPosition, 2701, 850);
                        case 43 -> Utils.setHitbox(hitboxPosition, 2680, 867);
                        case 44 -> Utils.setHitbox(hitboxPosition, 2660, 878);
                        case 45 -> Utils.setHitbox(hitboxPosition, 2633, 886);
                        case 46 -> Utils.setHitbox(hitboxPosition, 2607, 892);
                        case 47 -> Utils.setHitbox(hitboxPosition, 2581, 897);
                    }
                }
                break;
            case 3:
                hitboxDistance = 80;
                if (side == 0) Utils.setHitbox(hitboxPosition, 630, 434);
                else Utils.setHitbox(hitboxPosition, 2312, 418);
                break;
        }

        hitboxDistance = Utils.setHitboxDistance(data, hitboxDistance);
    }

    public static void render(SpriteBatch batch) {
        String texture = null;
        String sideTexture;
        float x = 0;
        float y = 0;

        boolean lookInRoom = false;
        boolean lookUnderBed = false;
        boolean lookAtTape = false;

        if (side == 0) {
            sideTexture = "Left";
        } else if (side == 1) {
            sideTexture = "Middle";
        } else {
            sideTexture = "Right";
        }

        switch (room) {
            case 1:
                lookInRoom = true;
                String position;

                int jumpI = (int) jumpAnimation;
                if (jumpI != 0) {
                    if ((jumpTarget == 21 && side == 0) || (jumpTarget == 19 && side == 1)) {
                        jumpI = jumpTarget - jumpI + 1;
                    }
                }

                if (jumpI == 0) {
                    position = String.valueOf((int) attackPosition);
                    texture = "Battle/" + sideTexture + "/" + position;
                } else {
                    if (jumpTarget == 21) {
                        sideTexture = "Left";
                    } else if (jumpTarget == 19){
                        sideTexture = "Right";
                    }
                    texture = "Battle/Jump/" + sideTexture + "/" + jumpI;
                }

                if (jumpI != 0) {
                    if (jumpTarget == 21){
                        float multiplier = (float) jumpI / 21;
                        float cosMultiplier = (float) (-Math.cos(Math.toRadians(180 * multiplier)) + 1) / 2;
                        float distance = 1089 * cosMultiplier;
                        x = 29 + distance;
                        y = 209;
                    } else {
                        float multiplier = (float) jumpI / 19;
                        float cosMultiplier = (float) (-Math.cos(Math.toRadians(180 * multiplier)) + 1) / 2;
                        float distance = 1105 * cosMultiplier;
                        x = 1044 + distance;
                        y = 170;
                    }
                } else {
                    if (side == 0) {
                        x = 104;
                        y = 276;
                    } else if (side == 1) {
                        x = 1119;
                        y = 200;
                    } else {
                        x = 2175;
                        y = 174;
                    }
                }
                break;
            case 2:
                if (Player.room == 1 && Monstergami.side != 3) {
                    lookUnderBed = true;
                    if (side == 0) {
                        texture = "Under Bed/Bed1";
                        y = 169;
                    } else {
                        texture = "Under Bed/Bed2";
                        x = 1204;
                        y = 180;
                    }
                } else if (Player.room == 2 && tapePosition >= 1) {
                    lookAtTape = true;
                    x = 190;
                    y = 455;
                    texture = "Tape/Leaving" + (int) (15 - tapePosition);
                }
                break;
            case 3:
                lookInRoom = true;
                if (side == 0) {
                    texture = "Under Bed/Left/Left" + ((int) twitchPosition + 1);
                    x = 506;
                    y = 236;
                } else {
                    texture = "Under Bed/Right/Right" + ((int) twitchPosition + 1);
                    x = 2142;
                    y = 184;
                }
        }
        boolean passed = lookInRoom && !Player.turningAround && Player.room == 0;
        if (!passed) {
            passed = lookUnderBed && !Player.turningAround;
        }

        if (!passed) {
            passed = lookAtTape && !Player.turningAround;
        }

        if (!passed) return;
        batch.setColor(1, 1, 1, 1);
        batch.draw(ImageHandler.images.get("game/Shadow Vinnie/" + texture), x, y);
    }

    public static void roomMechanic(Data data, Random random, AudioClass audioClass) {
        if (!Player.freeze && cooldownTimer > 0 && !attack && attackTime != 0){
            cooldownTimer -= Gdx.graphics.getDeltaTime();
            if (cooldownTimer < 0){
                cooldownTimer = 0;
            }
        }

        if (attackTime == 0 && !attack && Player.blacknessTimes == 0) {
            shaking = false;
            Player.blacknessMultiplier = 1.25f;
            room = 2;
            bedPatienceTimer = 1.5f;
            cooldownTimer = 32;
            side = (int) (2 * Math.random()) * 2;
            audioClass.play("ShadowVinnieCooldown2");
            audioClass.setVolume("ShadowVinnieCooldown2", 0.25f);
            audioClass.play("crawl");
            tapeSpotted = false;
            if (!tapeWeasel && !Player.tapeStolen) {
                tapePosition = 14f;
                if (data.hardCassette) timeUntilWeasel = 0.01f;
                else timeUntilWeasel = 0.25f + random.nextInt(2) + random.nextFloat() + (0.1f * (20 - ai));
            }
        }

        if (attackTime != 0 && !attack && Player.flashlightAlpha > 0 && !Player.turningAround && Player.room == 0) {
            attack = Player.inititiateSnapPosition(side);
            if (attack) {
                Player.lastCharacterAttack = "Shadow";
            }
        }

        if (attack) {
            shaking = hitboxHit;

            float multiplier = 20;
            if ((int) framesToMove != 0){
                multiplier = 80;
            }
            if (jumpAnimation > 0){
                multiplier = 23;
            }

            if (jumpTarget == 0) {

                if (shaking) {
                    if (attackTime > 0) {
                        attackTime -= Gdx.graphics.getDeltaTime();
                        if (attackTime < 0) {
                            attackTime = 0;
                        }
                    }

                    if ((int) framesToMove == 0) {
                        timeToFlash -= Gdx.graphics.getDeltaTime();
                        patienceTimer += Gdx.graphics.getDeltaTime();
                    }
                    if (timeToFlash <= 0 && attackTime == 0) {
                        if (jumps == 0) {
                            Player.snapPosition = false;
                            attack = false;
                            audioClass.play("thunder");
                            Player.blacknessTimes = 3;
                            Player.blacknessMultiplier = 6;
                            Player.blacknessDelay = 0.5f;
                            Player.freeze = true;
                        } else {
                            if (side == 0){
                                jumpTarget = 21;
                                jumpCase = 1;
                            } else if (side == 2){
                                jumpTarget = 19;
                                jumpCase = 0;
                            } else {
                                if ((ShadowCat.room != 4 && random.nextInt(2) == 0) || ShadowCat.side == 0) {
                                    jumpTarget = 21;
                                    jumpCase = 0;
                                } else {
                                    jumpTarget = 19;
                                    jumpCase = 1;
                                }
                            }
                            patienceTimer = 0.65f;

                            if (attackPosition > 24) {
                                framesToMove = 48 - attackPosition;
                            } else if (attackPosition < 24) {
                                framesToMove = -attackPosition;
                            } else {
                                if (random.nextInt(2) == 1) {
                                    framesToMove = -attackPosition;
                                } else {
                                    framesToMove = attackPosition;
                                }
                            }

                            audioClass.play("vinnieDodge");
                            audioClass.loop("vinnieDodge", true);
                            dodgePlaying = true;
                        }
                    } else if (attackTime > 0 && timeToFlash <= 0) {
                        if (!Player.snapPosition) {
                            Player.snapPosition = true;
                            Player.snapToSide = side;
                        }

                        timeToFlash = 0.2f + 0.05f * random.nextInt(3);
                        patienceTimer = 0.65f;
                        framesToMove = 8 + (1 + random.nextInt(48));
                        if (random.nextInt(2) == 1) {
                            framesToMove = -framesToMove;
                        }
                        audioClass.play("vinnieDodge");
                        audioClass.loop("vinnieDodge", true);
                        dodgePlaying = true;
                    }
                }
            } else {
                if (jumpAnimation == 0) {
                    if (shaking && (int) framesToMove == 0 && attackPosition == 0) {
                        Player.snapPosition = false;
                        jumpAnimation = jumpTarget;
                        jumps--;
                        jumping = true;
                        if (jumpCase == 0) {
                            audioClass.play("vinnieTurnLeft");
                        } else {
                            audioClass.play("vinnieTurnRight");
                        }
                    }
                } else {
                    if (jumpAnimation > 0) {
                        jumpAnimation -= Gdx.graphics.getDeltaTime() * multiplier;
                        if (jumpAnimation < 0) {
                            jumpAnimation = 0;
                        }
                    } else {
                        jumpAnimation += Gdx.graphics.getDeltaTime() * multiplier;
                        if (jumpAnimation > 0) {
                            jumpAnimation = 0;
                        }
                    }

                    if (jumpAnimation < 1) {
                        if (jumpTarget == 19) {
                            if (side == 1) {
                                side = 2;
                            } else {
                                side = 1;
                            }
                        } else if (jumpTarget == 21){
                            if (side == 0) {
                                side = 1;
                            } else {
                                side = 0;
                            }
                        }
                        if (jumpCase == 0) {
                            audioClass.stop("vinnieTurnLeft");
                        } else {
                            audioClass.stop("vinnieTurnRight");
                        }
                        jumping = false;
                        jumpTarget = 0;
                        jumpAnimation = 0;
                        attackTime = 2;
                        patienceTimer = 2;
                        if (ShadowCat.room == 4) patienceTimer++;
                        timeToFlash = 0.65f;
                    }
                }
            }

            if (framesToMove != 0) {
                if (framesToMove > 0) {
                    framesToMove -= Gdx.graphics.getDeltaTime() * multiplier;
                    attackPosition += Gdx.graphics.getDeltaTime() * multiplier;
                    if (attackPosition >= 48){
                        attackPosition -= 48;
                    }
                    if (framesToMove <= 0) {
                        framesToMove = 0;
                        attackPosition = (int) attackPosition;
                    }
                } else if (framesToMove < 0) {
                    framesToMove += Gdx.graphics.getDeltaTime() * multiplier;
                    attackPosition -= Gdx.graphics.getDeltaTime() * multiplier;

                    if (framesToMove > -1) {
                        framesToMove = 0;
                        attackPosition = (int) attackPosition;
                    }
                    if (attackPosition < 0) {
                        attackPosition += 48;
                    }
                }
                shaking = false;
            }

            if ((int) framesToMove == 0 && dodgePlaying){
                audioClass.stop("vinnieDodge");
                dodgePlaying = false;
            }

            if (!shaking && (int) framesToMove == 0 && jumpAnimation == 0) {
                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer < 0) {
                    patienceTimer = 0;
                }
            }
        }

        shaking = shaking && (int) framesToMove == 0;

        if (shaking) {
            twitchPosition += Gdx.graphics.getDeltaTime() * 30;
            if (twitchPosition >= 2) {
                twitchPosition = 0;
            }
        } else {
            twitchPosition = 0;
        }

        if (cooldownTimer == 0 || patienceTimer == 0){
            setJumpscare();
        }
    }

    public static void bedMechanic(Data data, AudioClass audioClass){
        if (cooldownTimer > 0 && !Player.freeze){
            cooldownTimer -= Gdx.graphics.getDeltaTime();

            if (!tapeSpotted && timeUntilWeasel > 0){
                timeUntilWeasel -= Gdx.graphics.getDeltaTime();
                if (timeUntilWeasel <= 0){
                    timeUntilWeasel = 0;
                    tapeWeasel = true;
                    if (data.hardCassette){
                        Player.playPosition = 3;
                        Player.stopPosition = 0;
                        Player.rewindPosition = 0;
                        Player.tapeRewind = false;
                        Player.tapePlay = true;
                        Player.tapeStop = false;
                        Player.tape.stop();
                        audioClass.stop("tapeRewind");
                    }
                    audioClass.play("tapeWeasel");
                    audioClass.loop("tapeWeasel", true);
                }
            }

            if (Player.room == 1 && !Player.turningAround && bedPatienceTimer > 0){
                bedPatienceTimer -= Gdx.graphics.getDeltaTime();
                if (bedPatienceTimer <= 0){
                    bedPatienceTimer = 0;
                    setJumpscare();
                }
            }
            boolean lookingAway = (Player.side == 0 && side == 2) || (Player.side == 2 && side == 0);

            if (cooldownTimer > 0) return;
            if (lookingAway && bedPatienceTimer < 1.5f) {
                audioClass.play("peek");
                room = 1;
                jumps = 5;
                attackTime = 2;
                attackPosition = 24;
                bedSpotted = false;
                cooldownTimer = 2;
                patienceTimer = 2f + 0.2f * (20 - ai);
                tapeSpotted = false;
                timeToFlash = 0.65f;
                return;
            }
            setJumpscare();
        }
    }

    public static void crouchMechanic(){
        if (shaking) {
            twitchPosition += Gdx.graphics.getDeltaTime() * 30;
            if (twitchPosition >= 2) {
                twitchPosition = 0;
            }
        } else {
            twitchPosition = 0;
        }

        if (timeToFlash > 0) {
            shaking = hitboxHit;

            if (shaking) {
                timeToFlash -= Gdx.graphics.getDeltaTime();
                if (timeToFlash <= 0){
                    shaking = false;
                    //Write functionality where Vinnie jumps backwards into the room.
                }
            } else if (!Player.freeze){
                patienceTimer -= Gdx.graphics.getDeltaTime();
                if (patienceTimer > 0) return;
                patienceTimer = 0;
                setJumpscare();
            }
        }
    }
}
