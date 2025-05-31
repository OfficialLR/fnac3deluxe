package com.fnac3.deluxe.core.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.FNaC3Deluxe;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.enemy.*;
import com.fnac3.deluxe.core.functions.Knock;
import com.fnac3.deluxe.core.input.Player;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.Random;

public class Game {

    private static FrameBuffer fbo;
    private static FrameBuffer roomBuffer;
    private static FrameBuffer screenBuffer;

    private static BitmapFont gameoverFont;
    private static BitmapFont gameoverSelectionsFont;
    private static float gameoverScreenAlpha;
    private static boolean fontsAdded;
    private static boolean retryButton;
    private static boolean menuButton;
    private static boolean pressedRetry;
    private static boolean pressedMenu;
    private static float gameoverStatic;
    private static float zoomCharacter;
    private static float monstergamiCooldown;
    private static int monstergamiTimes;
    private static boolean monstergamiPositive;
    private static float monstergamiFrames;
    private static float gameoverAlpha;

    public static float screenAlpha;
    public static Music ambience;

    private static boolean jumpscare;
    private static float jumpscarePitch;
    private static float jumpscareFrameDelay;
    private static float jumpscareFrame;
    private static int jumpscareFrameTarget;
    private static float jumpscareTimer;
    private static String jumpscareCharacter;
    private static String jumpscareTexture;
    private static String jumpscareSound;

    public static String gameoverReason;
    public static float time;
    public static float hour;
    public static int previousHourOfGame;
    public static int hourOfGame;
    public static float purpleSlownessTimer;
    public static float purpleTime;
    public static float purpleTimeTarget;

    public static boolean firstFrame;
    public static boolean gameover;
    public static boolean restart;
    public static boolean win;
    public static float winAlpha;
    public static float winDuration;
    private static float clockAnimation;
    private static Random random;

    public static Pixmap playButton;
    public static Pixmap stopButton;
    public static Pixmap rewindButton;

    public static Pixmap batteryPixmap;
    public static Pixmap tapeButtonPixmap;
    public static Pixmap bedButtonPixmap;
    public static Pixmap tapeBackButtonPixmap;
    public static Pixmap bedBackButtonPixmap;

    public static final Knock knock = new Knock();

    public static final Rat rat = new Rat();
    public static final Cat cat = new Cat();
    public static final ClassicCat classicCat = new ClassicCat();

    private static final TextureRegion roomRegion = new TextureRegion();

    public static void loadPixmaps(){
        playButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/PlayButton");
        stopButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/StopButton");
        rewindButton = ImageHandler.loadImageBuffer("game/room/Tape/Buttons/RewindButton");

        batteryPixmap = ImageHandler.loadImageBuffer("game/room/Tape/batteryHitbox");

        tapeButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/TapePlayer");
        tapeBackButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/TapePlayerBack");
        bedButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/UnderBed");
        bedBackButtonPixmap = ImageHandler.loadImageBuffer("game/Buttons/UnderBedBack");
    }

    public static void start(Data data, AudioClass audioClass){
        audioClass.stopAllSounds();
        if (random == null) {
            random = new Random();
        }
        retryButton = false;
        menuButton = false;
        screenAlpha = 0;
        if (ambience != null){
            ambience.stop();
        }
        gameover = false;
        winDuration = 0;
        win = false;
        winAlpha = 0;
        Player.reset(data);

        rat.reset(data, 0, -1, 1, data.RatAI);
        cat.reset(data, 2, Math.random() < 0.5 ? 0 : 2, 1, data.CatAI);
        classicCat.reset(data, 0, -1, 1, data.classicCat ? 1 : 0);

        jumpscare = false;
        jumpscareFrame = 0;
        jumpscareFrameTarget = 0;
        jumpscareTimer = 0;
        jumpscareCharacter = "";
        jumpscareTexture = "";

        screenAlpha = 0;
        firstFrame = true;
        restart = false;
        hour = 60;
        time = 0;
        hourOfGame = 12;
        previousHourOfGame = 12;
        purpleTime = 0;
        purpleSlownessTimer = 10;
        purpleTimeTarget = 40;
        Discord.updateStatus = true;
    }

    public static void gameoverInput(float mx, float my){
        if (pressedRetry || pressedMenu) return;

        retryButton = gameoverAlpha > 0 && mx >= 394 && mx <= 455 && my >= 277 && my <= 302;

        menuButton = gameoverAlpha > 0 && mx >= 571 && mx <= 636 && my >= 277 && my <= 302;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if (menuButton){
                pressedMenu = true;
            } else if (retryButton){
                pressedRetry = true;
            }
        }
    }

    public static void input(StateManager stateManager, AudioClass audioClass, Viewport viewport, Vector3 v3, Data data){
        float mx = v3.x - Player.roomPosition[0] - Player.shakingPosition;
        float my = v3.y - Player.roomPosition[1];

        if (win) return;

        if (gameover){
            gameoverInput(mx, my);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            stateManager.setState(StateManager.State.MENU);
            ambience.stop();
            Player.tape.stop();
            return;
        }

        if (jumpscare) return;

        if (!Player.turningAround && !Player.freeze && (!Player.snapPosition || data.freeScroll || Player.room != 0)) {
            Player.move(mx, my, viewport.getWorldWidth(), viewport.getWorldHeight());
        } else if (Player.snapPosition && !data.freeScroll){
            Player.moveTarget();
        }

        if (Player.room != 2 && (Player.scared
                || audioClass.isPlaying("twitch"))){
             Player.roomShake();
        } else {
            Player.shakingPosition = 0;
        }

        if (Player.roomPosition[0] < 3) {
            Player.roomPosition[0] = 3;
        } else if (Player.roomPosition[0] > 2045) {
            Player.roomPosition[0] = 2045;
        }

        mx += Player.roomPosition[0] + Player.shakingPosition;
        my += Player.roomPosition[1];

        Player.flashlight(mx, my);
        if (!Player.freeze && !Player.justStarted && Player.room == 0) {
            if (mx < 1024) {
                Player.side = 0;
            } else if (mx < 2047){
                Player.side = 1;
            } else {
                Player.side = 2;
            }
        } else {
            if (Player.justStarted){
                Player.justStarted = false;
            }

            if (Player.room != 0) {
                Player.side = -1;
            }
        }

        Player.buttons(mx, my);

        if (Player.room == 2 && !Player.turningAround && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if (!Player.tapePlay && Player.tapeCondition("Play", !Player.tapeRewind && !Player.tapeStolen, mx, my)){
                Player.tapePlay = true;
                if (Player.stopPosition > 0){
                    Player.stopPosition = 2;
                    Player.tapeStop = false;
                }
                if (Player.rewindPosition > 0){
                    Player.rewindPosition = 0;
                }
            } else if (!Player.tapeStop && Player.tapeCondition("Stop", (Player.tapePlay || Player.tapeRewind) && !Player.tapeStolen, mx, my)){
                Player.tapeStop = true;
                if (Player.tapePlay) {
                    Player.playPosition = 3;
                    Player.tapePlay = false;
                } else if (Player.tapeRewind) {
                    Player.rewindPosition = 3;
                    Player.tapeRewind = false;
                }
            } else if (!Player.tapeRewind && Player.tapeCondition("Rewind", !Player.tapePlay && !Player.tapeStolen, mx, my)){
                Player.tapeRewind = true;
                if (Player.stopPosition > 0){
                    Player.stopPosition = 2;
                    Player.tapeStop = false;
                }
                if (Player.playPosition > 0){
                    Player.playPosition = 0;
                }
            }

            Player.batteryInput(mx, my);
        }
    }

    public static void gameoverUpdate(StateManager stateManager, AudioClass audioClass){
        if (gameoverScreenAlpha > 0){
            gameoverScreenAlpha -= Gdx.graphics.getDeltaTime() * 2;
            if (gameoverScreenAlpha < 0){
                gameoverScreenAlpha = 0;
            }
        }

        if (monstergamiCooldown > 0){
            monstergamiCooldown -= Gdx.graphics.getDeltaTime();
            if (monstergamiCooldown <= 0){
                monstergamiCooldown = 3 + random.nextInt(4);
                monstergamiTimes = 1 + random.nextInt(2);
            }
        }

        if (monstergamiTimes > 0 && !monstergamiPositive && monstergamiFrames == 0){
            monstergamiPositive = true;
            monstergamiTimes--;
        }

        if (monstergamiPositive){
            monstergamiFrames += Gdx.graphics.getDeltaTime() * 40;
            if (monstergamiFrames >= 10) {
                monstergamiFrames = 8.99f;
                monstergamiPositive = false;
            }
        } else {
            monstergamiFrames -= Gdx.graphics.getDeltaTime() * 40;
            if (monstergamiFrames <= 0) monstergamiFrames = 0;
        }

        if (zoomCharacter > 0){
            zoomCharacter -= Gdx.graphics.getDeltaTime();
            if (zoomCharacter < 0) zoomCharacter = 0;
        }

        if (zoomCharacter == 0 && gameoverAlpha < 1) {
            gameoverAlpha += Gdx.graphics.getDeltaTime() * 2;
            if (gameoverAlpha > 1) gameoverAlpha = 1;
        }

        gameoverStatic += Gdx.graphics.getDeltaTime() * 30;
        if (gameoverStatic >= 8){
            gameoverStatic = 0;
        }

        if (pressedRetry || pressedMenu){
            if (screenAlpha > 0) {
                screenAlpha -= Gdx.graphics.getDeltaTime();
                audioClass.setVolume("monstergami", screenAlpha);
                audioClass.setVolume("scaryImpact", screenAlpha);
                if (screenAlpha < 0) {
                    screenAlpha = 0;
                }
            } else {
                audioClass.stop("monstergami");
                audioClass.stop("scaryImpact");
                if (pressedRetry){
                    pressedRetry = false;
                    restart = true;
                    gameover = false;
                } else {
                    pressedMenu = false;
                    gameover = false;
                    stateManager.setState(StateManager.State.MENU);
                }
            }
        }
    }

    private static void jumpscareUpdate(AudioClass audioClass){
        jumpscareTimer -= Gdx.graphics.getDeltaTime();
        if (jumpscareTimer < 0) jumpscareTimer = 0;
        if (jumpscareFrame == 0){
            jumpscareFrame++;
            audioClass.stopAllSounds();
            audioClass.play(jumpscareSound);
            audioClass.setPitch(jumpscareSound, jumpscarePitch);
        }

        if (jumpscareFrameTarget == -1){
            jumpscareFrameDelay -= Gdx.graphics.getDeltaTime() * 25;
            if (jumpscareFrameDelay <= 0){
                jumpscareFrame = (int) (Math.random() * 6) + 1;
                jumpscareFrameDelay += Gdx.graphics.getDeltaTime() * 25;
            }
        } else {
            jumpscareFrame += Gdx.graphics.getDeltaTime() * 25;
            if (jumpscareFrame > jumpscareFrameTarget) jumpscareFrame = jumpscareFrameTarget;
        }
    }

    public static void setJumpscare(String jumpscareCharacter, String jumpscareTexture, String jumpscareSound, float jumpscareTimer, int jumpscareFrameTarget, float jumpscarePitch){
        Player.blacknessTimes = 3;
        Player.blacknessMultiplier = 10;
        Player.blacknessDelay = 0;
        jumpscare = true;
        Game.jumpscareCharacter = jumpscareCharacter;
        Game.jumpscareTimer = jumpscareTimer;
        Game.jumpscareSound = jumpscareSound;
        Game.jumpscareTexture = jumpscareTexture;
        Game.jumpscareFrameTarget = jumpscareFrameTarget;
        Game.jumpscarePitch = jumpscarePitch;
    }

    public static void jumpscareRender(SpriteBatch batch){
        Texture texture = ImageHandler.images.get(jumpscareTexture + (int) jumpscareFrame);
        batch.draw(texture, Player.roomPosition[0] + Player.shakingPosition, Player.roomPosition[1]);
    }

    public static void update(StateManager stateManager, Camera camera, Viewport viewport, Data data, AudioClass audioClass) {
        if (stateManager.getState() != StateManager.State.GAME) return;

        if (!fontsAdded){
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/timeFont.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            fontParameter.color = Color.WHITE;
            fontParameter.size = 56;
            gameoverFont = fontGenerator.generateFont(fontParameter);

            fontParameter.size = 28;
            gameoverSelectionsFont = fontGenerator.generateFont(fontParameter);

            fontsAdded = true;
        }

        if (win) {
            clockAnimation += Gdx.graphics.getDeltaTime() * 60;
            if (clockAnimation >= 4) {
                clockAnimation = 0;
            }
            if (screenAlpha > 0) {
                if (Gdx.graphics.getDeltaTime() < 0.5f) {
                    screenAlpha -= Gdx.graphics.getDeltaTime() * 2;
                    if (screenAlpha < 0) {
                        screenAlpha = 0;
                    }
                }
            }

            if (winDuration < 6) {
                if (screenAlpha == 0) {
                    winAlpha += Gdx.graphics.getDeltaTime();
                    if (winAlpha > 1) {
                        winAlpha = 1;
                    }
                }

                winDuration += Gdx.graphics.getDeltaTime();
                if (winDuration > 6) {
                    winDuration = 6;
                }
            } else if (winAlpha > 0) {
                winAlpha -= Gdx.graphics.getDeltaTime();
                if (winAlpha < 0) {
                    winAlpha = 0;
                }
            } else {
                stateManager.setState(StateManager.State.MENU);
            }
            return;
        }

        if (gameover) {
            gameoverUpdate(stateManager, audioClass);
            return;
        }

        if (ambience == null) {
            ambience = Gdx.audio.newMusic(Gdx.files.local("assets/sounds/ambience.wav"));
            ambience.setLooping(true);
            ambience.play();
        } else if (!ambience.isPlaying() && stateManager.getState() == StateManager.State.GAME) {
            ambience.play();
        }

        if (screenAlpha < 1 && !firstFrame) {
            if (Gdx.graphics.getDeltaTime() < 0.5f) {
                screenAlpha += Gdx.graphics.getDeltaTime() * 4;
                if (screenAlpha > 1) {
                    screenAlpha = 1;
                }
            }
        }

        firstFrame = false;

        float add = Gdx.graphics.getDeltaTime();

        if (Player.tape.isPlaying()) {
            if (data.hardCassette) time += add;
            else time += add * 1.5f;
        }

        if (data.hardCassette && audioClass.isPlaying("tapeWeasel")) time -= add;
        if (time < 0) time = 0;

        if (time / hour >= 1) {
            hourOfGame = (int) (time / hour);
            if (previousHourOfGame != hourOfGame){
                Discord.updateStatus = true;
                previousHourOfGame = hourOfGame;
            }
        }

        if (hourOfGame == 6) {
            win = true;
            data.writeWin(Math.min(rat.getDifficulty(), cat.getDifficulty()),
                    rat.isActive() && cat.isActive() &&
                    !data.flashDebug && !data.hitboxDebug && !data.timerDebug);
            audioClass.stopAllSounds();
            audioClass.play("win");
            ambience.stop();
            Player.tape.stop();
            camera.position.x = viewport.getWorldWidth() / 2 + Player.roomPosition[0] + Player.shakingPosition;
            camera.position.y = viewport.getWorldHeight() / 2 + Player.roomPosition[1];
            camera.update();
            return;
        }

        Player.tapeFunctionality(audioClass);
        Player.flashlightFlickerMechanic(data, random);
        if (Player.batterySound){
            audioClass.play("battery");
            Player.batterySound = false;
        }

        if (data.hardCassette) {
            if ((Player.tape.isPlaying() && !Player.tapeEnd) || jumpscare) {
                purpleSlownessTimer = 10;
                if (purpleTime > 0) {
                    purpleTime -= Gdx.graphics.getDeltaTime() * 100;
                    if (purpleTime < 0) {
                        purpleTime = 0;
                    }
                }
            } else {
                if (purpleSlownessTimer > 0) {
                    purpleSlownessTimer -= Gdx.graphics.getDeltaTime();
                    if (purpleSlownessTimer < 0) {
                        purpleSlownessTimer = 0;
                    }
                }

                if (purpleSlownessTimer == 0) {
                    purpleTime += Gdx.graphics.getDeltaTime() * 1.5f;
                } else {
                    purpleTime += Gdx.graphics.getDeltaTime() / 2;
                }
            }

            if (purpleTime >= purpleTimeTarget) {
                gameover = true;
                Discord.updateStatus = true;
                Player.battleOverlay = 0;
                Player.overlayTransparency = 0;
                Player.scared = false;
                Player.shakingPosition = 0;
                gameoverScreenAlpha = 1;
                Player.roomPosition[0] = 0;
                Player.roomPosition[1] = 0;
                audioClass.stopAllSounds();
                ambience.stop();
                gameoverReason = "Fell asleep";
            }
        }

        if (!gameover) {
            if (!jumpscare) {
                if (Player.foundUnderBed && !Player.foundUnderBedLock) {
                    audioClass.play("bed");
                    Player.foundUnderBedLock = true;
                } else if (Player.foundUnderBedLock && Player.room != 1) {
                    Player.foundUnderBedLock = false;
                    Player.foundUnderBed = false;
                }
            }

            boolean attack = false;

            if (!jumpscare) {
                rat.update(data, audioClass);
                if (rat.isAttack()) {
                    attack = true;
                }

                cat.update(data, audioClass);
                cat.audioUpdate(audioClass);
                if (cat.isAttack()) {
                    attack = true;
                }

                classicCat.update(data, audioClass);
            }

            if (classicCat.isHovered() && !audioClass.isPlaying("catPulse")) {
                audioClass.play("catPulse");
                audioClass.loop("catPulse", true);
            } else if (!classicCat.isHovered() && audioClass.isPlaying("catPulse")) {
                audioClass.stop("catPulse");
            }

            if (!Player.freeze
                    && (rat.isHovered() && rat.getState() != 0)
                    || cat.isHovered()){
                if (!audioClass.isPlaying("twitch")){
                    audioClass.play("twitch");
                    audioClass.loop("twitch", true);
                }
            } else {
                if (audioClass.isPlaying("twitch")){
                    audioClass.stop("twitch");
                }
            }

            knock.update(audioClass);

            Player.blackness();

            if (jumpscare && Player.blacknessTimes == 0){
                jumpscareUpdate(audioClass);
            }

            if (!jumpscare) {

                Player.turningAround(audioClass);
                Player.buttonVisibility();

                if (!Player.scared && attack) {
                    audioClass.play("attack_begin");
                    if (!cat.isAttack()) {
                        audioClass.play("attack");
                        audioClass.loop("attack", true);
                    }
                    if (Player.overlayTransparency != 1) Player.overlayTransparency = 1;
                    Player.scared = true;
                } else if (Player.scared) {
                    if (attack) {
                        if (!cat.isAttack()) {
                            String path = "attack";
                            float pitch = audioClass.getPitch(path);
                            float speed = Gdx.graphics.getDeltaTime() * 60;
                            if (rat.isAttack() && rat.getType() == 1) {
                                pitch += 0.00035f * speed;
                            } else {
                                pitch += 0.00025f * speed;
                            }

                            audioClass.setPitch(path, pitch);
                        }
                    } else {
                        Player.scared = false;
                        audioClass.stop("attack");
                    }
                }

                if (!Player.scared && !attack) {
                    Player.overlayTransparency -= Gdx.graphics.getDeltaTime() / 7;
                    if (Player.overlayTransparency < 0) {
                        Player.overlayTransparency = 0;
                    }
                }

                if (Player.battleOverlay <= 0 && Player.overlayTransparency != 0) {
                    Player.battleOverlay = 1;
                } else {
                    Player.battleOverlay -= Gdx.graphics.getDeltaTime() * 2;
                }
            } else {
                if (ambience.isPlaying()) {
                    ambience.stop();
                }

                if (!Player.freeze) {
                    Player.scared = false;
                    Player.buttonVisibility = 0;
                    Player.battleOverlay = 0;
                    Player.overlayTransparency = 0;
                    Player.disableTape();
                    Player.freeze = true;
                }

                if (jumpscareTimer <= 0) {
                    StringBuilder gameoverBuilder = new StringBuilder("Died to ");
                    switch (jumpscareCharacter) {
                        case "Rat" -> gameoverBuilder.append("Shadow Rat");
                        case "Cat" -> gameoverBuilder.append("Shadow Cat");
                    }
                    gameoverReason = gameoverBuilder.toString();

                    gameover = true;
                    Discord.updateStatus = true;
                    gameoverScreenAlpha = 1;
                    Player.battleOverlay = 0;
                    Player.overlayTransparency = 0;
                    Player.scared = false;
                    Player.shakingPosition = 0;
                    Player.roomPosition[0] = 0;
                    Player.roomPosition[1] = 0;
                    audioClass.stopAllSounds();
                }
            }
        }

        camera.position.x = viewport.getWorldWidth() / 2 + Player.roomPosition[0] + Player.shakingPosition;
        camera.position.y = viewport.getWorldHeight() / 2 + Player.roomPosition[1];
        camera.update();
    }

    private static void gameoverRender(SpriteBatch batch, Viewport viewport) {

        screenBuffer.begin();

        batch.setColor(0, 0, 0, 1);
        batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(), 0, 0);

        batch.setColor(0.5f, 0.5f, 0.5f, 1);
        String name = null;
        switch (jumpscareCharacter){
            case "Rat" -> name = "shadowRat";
            case "Cat" -> name = "shadowCat";
        }
        if (name != null) {
            batch.draw(ImageHandler.images.get("game/gameover/" + name), 0, 0);
        }
        batch.setColor(0.4f, 0, 1f, 1);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_DST_COLOR);

        Texture texture = ImageHandler.images.get("Static/GameoverStatic" + ((int) gameoverStatic + 1));
        batch.draw(texture, 0, 0);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        batch.setBlendFunction(srcFunc, dstFunc);

        gameoverFont.setColor(0.4f, 0, 1f, gameoverAlpha);
        gameoverFont.draw(batch, "GAME OVER", 363.5f, 424);

        if (retryButton) {
            gameoverSelectionsFont.setColor(1, 1, 1, gameoverAlpha);
        } else {
            gameoverSelectionsFont.setColor(0.4f, 0, 1, gameoverAlpha);
        }
        gameoverSelectionsFont.draw(batch, "Retry", 396, 300);

        if (menuButton) {
            gameoverSelectionsFont.setColor(1, 1, 1, gameoverAlpha);
        } else {
            gameoverSelectionsFont.setColor(0.4f, 0, 1, gameoverAlpha);
        }
        gameoverSelectionsFont.draw(batch, "Menu", 573, 300);

        batch.flush();

        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.setColor(0.2f, 0, 0.5f, gameoverScreenAlpha);

        batch.draw(texture, Player.roomPosition[0], Player.roomPosition[1]);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(1, 1, 1, screenAlpha);
        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    public static void render(SpriteBatch batch, Viewport viewport, Data data){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (gameover && fontsAdded){
            gameoverRender(batch, viewport);
            return;
        }

        Texture texture = null;
        String room = null;

        batch.setColor(1, 1, 1, 1);

        if (!jumpscare || Player.blacknessTimes > 0) {
            if (Player.turningAround) {
                room = switch (Player.room) {
                    case 0 -> "game/room/Moving/Turn Around/Moving" + (int) Player.turningPosition;
                    case 1 -> "game/room/Moving/Under Bed/Moving" + (int) Player.turningPosition;
                    case 2 -> "game/room/Moving/Turn Back/Moving" + (int) Player.turningPosition;
                    default -> null;
                };
            } else {
                switch (Player.room) {
                    case 0:
                        room = "game/room/FullRoom";
                        fbo = Player.flashlightPrep(batch, fbo, viewport, data, room);
                        break;
                    case 1:
                        room = "game/room/UnderBed";
                        fbo = Player.flashlightPrep(batch, fbo, viewport, data, room);
                        break;
                    case 2:
                        if (Player.tapeStolen){
                            room = "game/room/Tape/TapeMissing";
                        } else {
                            room = "game/room/Tape/Tape";
                        }
                        break;
                }
            }

            if (roomBuffer == null){
                roomBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
            }

            roomBuffer.begin();

            batch.draw(ImageHandler.images.get(room), 0, 0);

            EnemyRenderer.backRender(batch);

            if (Player.room == 0 && !Player.turningAround) {
                batch.draw(ImageHandler.images.get("game/room/FullRoomBed"), 0, 0);
            }

            EnemyRenderer.forthRender(batch);

            batch.flush();

            if (Player.room != 2 && !Player.turningAround) {
                Player.flashlightRender(batch, fbo);
            } else if (!Player.turningAround) {
                String tapeButtons = null;
                if (Player.stopPosition == 0) {
                    if (Player.playPosition > 0) {
                        tapeButtons = "Play" + (int) Player.playPosition;
                    } else if (Player.rewindPosition > 0) {
                        tapeButtons = "Rewind" + (int) Player.rewindPosition;
                    }
                } else {
                    if (Player.stopPosition >= 2) {
                        tapeButtons = "Stop" + (int) Player.stopPosition;
                    } else {
                        if ((int) (Player.playPosition) != 0) {
                            tapeButtons = "StopPlay1";
                        } else if ((int) (Player.rewindPosition) != 0) {
                            tapeButtons = "StopRewind1";
                        }
                    }
                }

                if (tapeButtons != null) {
                    batch.draw(ImageHandler.images.get("game/room/Tape/Buttons/" + tapeButtons),
                            416, 162);
                }

                batch.setColor(1, 1, 1, 1 - Player.flashlightAlpha);
                batch.draw(ImageHandler.images.get("game/room/Moving/Turn Back/Moving1"), 0, 0);
                batch.setColor(1, 1, 1, 1);

                if (Player.batteryAvailable) {
                    texture = ImageHandler.images.get("game/room/Tape/battery");
                    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    batch.setColor(0.2f + Player.flashlightAlpha / 1.25f, 0.2f + Player.flashlightAlpha / 1.25f, 0.2f + Player.flashlightAlpha / 1.25f, 1);
                    batch.draw(texture, 0, 0);
                    batch.setColor(1, 1, 1, 1);
                }
            }

            batch.flush();
            roomBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
            batch.end();

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            batch.enableBlending();
            batch.begin();
        }

        if (screenBuffer == null){
            screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }

        screenBuffer.begin();

        float position = Player.roomPosition[0] + Player.shakingPosition;

        roomRegion.setRegion(roomBuffer.getColorBufferTexture());
        roomRegion.flip(false, true);
        batch.draw(roomRegion, position, Player.roomPosition[1]);

        batch.setColor(1, 1, 1, 1);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();

        batch.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE);

        texture = switch (Player.lastCharacterAttack) {
            case "Shadow" -> ImageHandler.images.get("game/ShadowBattleOverlay");
            case "RatCat" -> ImageHandler.images.get("game/RatCatBattleOverlay");
            default -> texture;
        };

        if (texture != null) {
            float alpha = -0.5f + Player.overlayTransparency + Player.battleOverlay;
            batch.setColor(1, 1, 1, 1 - alpha);
            batch.draw(texture, Player.roomPosition[0] + Player.shakingPosition, Player.roomPosition[1]);
            batch.setColor(1, 1, 1, 1);
        }

        batch.flush();
        batch.end();
        batch.enableBlending();
        batch.begin();
        batch.setBlendFunction(srcFunc, dstFunc);

        if (jumpscare && Player.blacknessTimes == 0){
            jumpscareRender(batch);
        }

        batch.setColor(0, 0, 0, 1 - Player.blackness);
        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.draw(texture, position, Player.roomPosition[1]);

        batch.setColor(0.2f, 0, 0.2f, purpleTime / purpleTimeTarget);
        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.draw(texture, position, Player.roomPosition[1]);

        batch.setColor(1, 1, 1, 1);
        batch.flush();

        float leftButton = position + 46;
        float rightButton = leftButton + (float) 1024 / 2;
        float middleButton = leftButton + (float) 1024 / 4;
        switch (Player.room){
            case 0:
                texture = ImageHandler.images.get("game/Buttons/TapePlayer");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, leftButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);

                texture = ImageHandler.images.get("game/Buttons/UnderBed");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, rightButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
            case 1:
                texture = ImageHandler.images.get("game/Buttons/UnderBedBack");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, middleButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
            case 2:
                texture = ImageHandler.images.get("game/Buttons/TapePlayerBack");
                batch.setColor(1, 1, 1, Player.buttonVisibility);
                batch.draw(texture, middleButton, Player.roomPosition[1]);
                batch.setColor(1, 1, 1, 1);
                break;
        }
        texture = ImageHandler.images.get("game/time/" + hourOfGame + "AM");
        batch.draw(texture, 916 + position, 724 + Player.roomPosition[1]);
        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.setColor(1, 1, 1, screenAlpha);
        batch.draw(texture, position, Player.roomPosition[1] + texture.getHeight(),
                texture.getWidth(), -texture.getHeight());
        batch.setColor(1, 1, 1, 1);

        batch.flush();

        if (!win) {

            if (data.flashDebug && Player.snapPosition) {
                texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
                float offsety = Player.roomPosition[1];
                float width = viewport.getWorldWidth() / 4;

                batch.setColor(0.15f, 0, 0.2f, 1);
                batch.draw(texture, position + width - 4, offsety + 106, width * 2 + 8, 28);

                batch.setColor(0.35f, 0, 0, 1);
                batch.draw(texture, position + width, offsety + 110, width * 2, 20);

                batch.setColor(0.75f, 0, 0.65f, 1);

                float rect_value = 0;

                if (rat.getState() == 1) rect_value = Math.min(rat.getTimer1(), 1);
                else if (cat.getState() == 1) rect_value = Math.min(cat.getTimer1(), 1);

                batch.draw(texture, position + width, offsety + 110, width * 2 * rect_value, 20);
            }

            if (data.hitboxDebug && !Player.turningAround && !Player.freeze) {
                batch.setColor(1, 0, 1, 0.25f);

                hitboxRender(batch, rat.getHitboxSize(), rat.getHitbox()[0], rat.getHitbox()[1]);
                hitboxRender(batch, cat.getHitboxSize(), cat.getHitbox()[0], cat.getHitbox()[1]);
                hitboxRender(batch, classicCat.getHitboxSize(), classicCat.getHitbox()[0], classicCat.getHitbox()[1]);

                batch.setColor(1, 1, 1, 1);
            }
        } else {
            texture = ImageHandler.images.get("Clock/Clock" + ((int) clockAnimation + 1));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            batch.setColor(1, 1, 1, winAlpha);
            batch.draw(texture,
                    position + viewport.getWorldWidth() / 2 - (float) texture.getWidth() / 2,
                    Player.roomPosition[1] + viewport.getWorldHeight() / 2 - (float) texture.getHeight() / 2);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private static void hitboxRender(SpriteBatch batch, float hitboxDistance, float hitboxX, float hitboxY){
        if (hitboxX == -1) return;
        float size = hitboxDistance * 2;
        batch.draw(FNaC3Deluxe.circleShapeBuffer.getColorBufferTexture(),
                hitboxX - size / 2, hitboxY - size / 2, size, size);
    }
}
