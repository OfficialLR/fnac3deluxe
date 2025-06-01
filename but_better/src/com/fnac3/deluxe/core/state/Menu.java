package com.fnac3.deluxe.core.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fnac3.deluxe.core.FNaC3Deluxe;
import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.data.TextString;
import com.fnac3.deluxe.core.discord.Discord;
import com.fnac3.deluxe.core.ui.Button;
import com.fnac3.deluxe.core.util.AudioClass;
import com.fnac3.deluxe.core.util.ImageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    private static FrameBuffer screenBuffer;
    private static FrameBuffer rainbowStarBuffer;

    private static float ratAlpha;
    private static float catAlpha;
    private static float whiteAlpha;
    private static float rainbowPosition;

    private static int ratAITextAlign;
    private static int catAITextAlign;
    private static String ratAIText;
    private static String catAIText;

    private static float blackFade;
    private static boolean writeConfig;
    private static float readyAlpha;
    public static float staticScreen;
    public static boolean loaded;
    public static boolean renderReady;
    public static boolean loading;
    public static boolean fontsLoaded;

    private static final GlyphLayout layoutModeTemp = new GlyphLayout();
    private static final StringBuilder textBuilder = new StringBuilder();
    private static final Map<String, String> textValues = new HashMap<>();
    private static final List<String> textRenderList = new ArrayList<>();
    private static final Map<String, Integer> captionLayoutWidthInformation = new HashMap<>();
    private static final float[] captionPosition = new float[2];

    private static String textToRender;
    private static int previousTextCase;
    private static int textCase;
    private static int previousEnemyCase;
    private static int enemyCase;
    private static int captionWidth;
    private static float textValue;
    private static int captionAlignment;

    private static String menuMusicName;

    private static float charactersX;
    private static float charactersY;

    private static final Button button1 = new Button(414, 638, 32, 32, 0);
    private static final Button button2 = new Button(414, 594, 32, 32, 0);
    private static final Button button3 = new Button(414, 550, 32, 32, 0);
    private static final Button button4 = new Button(414, 506, 32, 32, 0);

    private static final Button button5 = new Button(848, 104, 32, 32, 1);
    private static final Button button6 = new Button(848, 60, 32, 32, 0);
    private static final Button button7 = new Button(848, 16, 32, 32, 0);
    private static final Button readyButton = new Button(414, 16, 196, 99, 0);

    private static final Button ratBox = new Button(33, 255, 474, 256, 0);
    private static final Button catBox = new Button(525, 255, 474, 256, 0);

    private static final Button infoBox = new Button(16, 16, 32, 32, 0);
    private static final Button helpBox = new Button(16, 60, 32, 32, 0);

    private static final Button normalStar = new Button(402, 156, 42, 42, 0, true);
    private static final Button laserStar = new Button(457, 156, 42, 42, 0, true);
    private static final Button cassetteStar = new Button(512, 156, 42, 42, 0, true);
    private static final Button batteryStar = new Button(567, 156, 42, 42, 0, true);
    private static final Button catStar = new Button(622, 156, 42, 42, 0, true);

    private static final Button allChallengesStar = new Button(512, 220, 80, 80, 0, true);

    private static float volume;

    private static BitmapFont menuFontHeading;
    private static BitmapFont menuFontTitle;
    private static BitmapFont menuFontLabel;
    private static BitmapFont menuFontCaption;

    private static boolean playDeluxe;

    public static void input(float mx, float my, StateManager stateManager, Data data, AudioClass audioClass){
        if (!loaded) return;

        charactersX = (512 - mx) / 20;
        charactersY = (384 - my) / 20;

        writeConfig = false;

        button1.update(mx, my);
        button2.update(mx, my);
        button3.update(mx, my);
        button4.update(mx, my);
        button5.update(mx, my);
        button6.update(mx, my);
        button7.update(mx, my);
        readyButton.update(mx, my);
        infoBox.update(mx, my);
        helpBox.update(mx, my);

        normalStar.update(mx, my);
        laserStar.update(mx, my);
        cassetteStar.update(mx, my);
        batteryStar.update(mx, my);
        catStar.update(mx, my);
        allChallengesStar.update(mx, my);

        ratBox.update(mx - charactersX, my - charactersY);
        catBox.update(mx - charactersX, my - charactersY);

        textCase = 0;
        enemyCase = 0;

        boolean challenges = button5.getState() == 1;
        boolean options = button6.getState() == 1;

        if (button1.isHovering()){
            if (challenges){
                textModifier(TextString.laserPointerText);
                textCase = 1;
            } else if (options){
                textModifier(TextString.flashHPText);
                textCase = 2;
            }
            if (textCase != 0) {
                captionAlignment = 2;
                captionPosition[0] = button1.x - 4;
                captionPosition[1] = button1.y + 16;
            }
        } else if (button2.isHovering()){
            if (challenges){
                textModifier(TextString.hardCassetteText);
                textCase = 3;
            } else if (options){
                textModifier(TextString.hitboxText);
                textCase = 4;
            }
            if (textCase != 0) {
                captionAlignment = 2;
                captionPosition[0] = button2.x - 4;
                captionPosition[1] = button2.y + 16;
            }
        } else if (button3.isHovering()){
            if (challenges){
                textModifier(TextString.faultyFlashlight);
                textCase = 5;
            } else if (options){
                textModifier(TextString.timerText);
                textCase = 6;
            }
            if (textCase != 0) {
                captionAlignment = 2;
                captionPosition[0] = button3.x - 4;
                captionPosition[1] = button3.y + 16;
            }
        } else if (button4.isHovering()){
            if (challenges){
                textModifier(TextString.classicCatText);
                textCase = 7;
            } else if (options) {
                textModifier(TextString.freeScrollText);
                textCase = 8;
            }
            if (textCase != 0) {
                captionAlignment = 2;
                captionPosition[0] = button4.x - 4;
                captionPosition[1] = button4.y + 16;
            }
        }

        boolean starHover = normalStar.isHovering()
                || laserStar.isHovering()
                || cassetteStar.isHovering()
                || batteryStar.isHovering()
                || catStar.isHovering()
                || allChallengesStar.isHovering();

        if (starHover){
            captionAlignment = 1;
            captionPosition[0] = 512;
            captionPosition[1] = allChallengesStar.y + allChallengesStar.height + 20;
        }

        if (normalStar.isHovering()){
            textModifier("Shadow Rat and Cat");
            textCase = 9;
        } else if (laserStar.isHovering()){
            textModifier("Shadow Rat and Cat with Laser Pointer");
            textCase = 10;
        } else if (cassetteStar.isHovering()){
            textModifier("Shadow Rat and Cat with Hard Cassette");
            textCase = 11;
        } else if (batteryStar.isHovering()){
            textModifier("Shadow Rat and Cat with Limited Battery");
            textCase = 12;
        } else if (catStar.isHovering()){
            textModifier("Shadow Rat and Cats");
            textCase = 13;
        } else if (allChallengesStar.isHovering()){
            textModifier("Shadow Rat and Cat All Challenges");
            textCase = 14;
        }

        if (readyButton.isHovering()){
            readyAlpha += Gdx.graphics.getDeltaTime() * 2;
            if (readyAlpha > 0.25f) readyAlpha = 0.25f;
        } else {
            readyAlpha -= Gdx.graphics.getDeltaTime() * 2;
            if (readyAlpha < 0) readyAlpha = 0;
        }

        if (readyButton.isLeftPressed()){
            stateManager.setState(StateManager.State.LOADING);
            loaded = false;
        }

        data.RatAI = nightCustomNightAI(audioClass, ratBox, 1, TextString.ratText);
        ratAlpha = nightCharacterAlpha(ratBox.getState() > 0, ratAlpha);

        data.CatAI = nightCustomNightAI(audioClass, catBox, 2, TextString.catText);
        catAlpha = nightCharacterAlpha(catBox.getState() > 0, catAlpha);

        if (infoBox.isHovering()){
            textModifier(TextString.infoText);
            textCase = 16;
            captionAlignment = 0;
            captionPosition[0] = infoBox.x + infoBox.width;
            captionPosition[1] = infoBox.y - 8;
        }
        if (helpBox.isHovering()){
            textModifier(TextString.helpText);
            textCase = 17;
            captionAlignment = 0;
            captionPosition[0] = helpBox.x + helpBox.width;
            captionPosition[1] = helpBox.y - 8;
        }

        boolean starPressed = normalStar.isLeftPressed()
                || laserStar.isLeftPressed()
                || cassetteStar.isLeftPressed()
                || batteryStar.isLeftPressed()
                || catStar.isLeftPressed()
                || allChallengesStar.isLeftPressed();

        ratBox.setState(starPressed && ratBox.getState() == 0, 4);
        catBox.setState(starPressed && catBox.getState() == 0, 4);

        data.RatAI = ratBox.getState();
        data.CatAI = catBox.getState();

        if (normalStar.isLeftPressed()){
            data.pointer = 0;
            data.hardCassette = false;
            data.limitedBattery = false;
            data.classicCat = false;
            writeConfig = true;
        } else if (laserStar.isLeftPressed()){
            if (!data.hardCassette && !data.limitedBattery && !data.classicCat) {
                if (data.pointer == 0 || data.pointer == 3) data.pointer = 1;
                else data.pointer++;
            }
            if (data.pointer == 0) data.pointer = 1;
            data.hardCassette = false;
            data.limitedBattery = false;
            data.classicCat = false;
            writeConfig = true;
        } else if (cassetteStar.isLeftPressed()){
            data.pointer = 0;
            data.hardCassette = true;
            data.limitedBattery = false;
            data.classicCat = false;
            writeConfig = true;
        } else if (batteryStar.isLeftPressed()){
            data.pointer = 0;
            data.hardCassette = false;
            data.limitedBattery = true;
            data.classicCat = false;
            writeConfig = true;
        } else if (catStar.isLeftPressed()){
            data.pointer = 0;
            data.hardCassette = false;
            data.limitedBattery = false;
            data.classicCat = true;
            writeConfig = true;
        } else if (allChallengesStar.isLeftPressed()){
            if (data.hardCassette && data.limitedBattery && data.classicCat){
                if (data.pointer == 0 || data.pointer == 3) data.pointer = 1;
                else data.pointer++;
            }
            if (data.pointer == 0) data.pointer = 1;
            data.hardCassette = true;
            data.limitedBattery = true;
            data.classicCat = true;
            writeConfig = true;
        }

       if (button5.isLeftPressed()) {
            data.options = 0;
            writeConfig = true;
       } else if (button6.isLeftPressed()) {
            data.options = 1;
            writeConfig = true;
       } else if (button7.isLeftPressed()){
            data.options = 2;
            writeConfig = true;
       } else if (button1.isLeftPressed()) {
           if (data.options == 0) {
               data.pointer++;
               if (data.pointer > 3) data.pointer = 0;
           } else if (data.options == 1) data.flashDebug = !data.flashDebug;
           else {
               data.vSync = !data.vSync;
               Gdx.graphics.setVSync(data.vSync);
           }
           writeConfig = true;
       } else if (button2.isLeftPressed()) {
           if (data.options == 0) data.hardCassette = !data.hardCassette;
           else if (data.options == 1) data.hitboxDebug = !data.hitboxDebug;
           else data.muteJumpscare = !data.muteJumpscare;
           writeConfig = true;
       } else if (button3.isLeftPressed()) {
           if (data.options == 0) data.limitedBattery = !data.limitedBattery;
           else if (data.options == 1) data.lightDebug = !data.lightDebug;
           else data.menuMusic = !data.menuMusic;
           writeConfig = true;
       } else if (button4.isLeftPressed()) {
           if (data.options == 0) data.classicCat = !data.classicCat;
           else if (data.options == 1) data.freeScroll = !data.freeScroll;
           else {
               data.ogMusic = !data.ogMusic;
               audioClass.stop(menuMusicName);
               playDeluxe = true;
           }
           writeConfig = true;
       }
       if (writeConfig) {
           audioClass.play("select");
           buttonConfigSync(data);
           data.writeConfig();
           Discord.updateStatus = true;
       }
    }

    public static void update(Camera camera, StateManager stateManager, Data data, AudioClass audioClass){
        float time = Gdx.graphics.getDeltaTime();

        if (data.menuMusic) volume += time * 6;
        else volume -= time * 6;
        volume = volume < 0 ? 0 : volume > 1 ? 1 : volume;

        rainbowPosition += time * 60;
        if (rainbowPosition > 80) {
            rainbowPosition -= 80;
            if (rainbowPosition < 0 || rainbowPosition > 80) rainbowPosition = 0;
        }

        if (loaded) {
            if (blackFade < 1){
                blackFade += time * 3;
                if (blackFade > 1) blackFade = 1;
            }

            if (whiteAlpha > 0){
                whiteAlpha -= time * 2.5f;
                if (whiteAlpha < 0 || whiteAlpha > 1) whiteAlpha = 0;
            }

            if (ratBox.isStateChanged()){
                ratAIText = enemyAIText(ratBox);
                ratAITextAlign = enemyAITextAlign(ratBox, ratAIText);
            }

            if (catBox.isStateChanged()){
                catAIText = enemyAIText(catBox);
                catAITextAlign = enemyAITextAlign(catBox, catAIText);
            }

            if (playDeluxe && stateManager.getState() == StateManager.State.MENU){
                audioClass.play(menuMusicName);
                audioClass.setPitch(menuMusicName, 0.75f);
                audioClass.loop(menuMusicName, true);
                playDeluxe = false;
            }

            audioClass.setVolume(menuMusicName, volume * 0.75f);

            if (staticScreen >= 0 && staticScreen < 8) {
                staticScreen += time * 40;
            }

            if (staticScreen < 0 || staticScreen >= 8){
                staticScreen = 0;
            }

            if (textCase != 0 && textValue < 0.25f){
                textValue += time;
                if (textValue > 0.25f) textValue = 0.25f;
            } else if (textCase == 0 && textValue > 0){
                textValue -= time;
                if (textValue < 0) textValue = 0;
            }

            if (textCase != 0 && (textCase != previousTextCase || enemyCase != previousEnemyCase)) {
                menuTextShapeWidth();
                captionWidth = captionLayoutWidthInformation.get(textToRender);
            }

            previousTextCase = textCase;
            previousEnemyCase = enemyCase;
        } else {
            if (stateManager.getState() == StateManager.State.MENU){
                if (!loading) {
                    ImageHandler.addImages(stateManager);
                    if (!fontsLoaded){
                        fontsLoaded = true;
                        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/timeFont.ttf"));
                        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

                        fontParameter.size = 40;
                        fontParameter.color = Color.WHITE;
                        menuFontTitle = fontGenerator.generateFont(fontParameter);

                        fontParameter.size = 28;
                        menuFontHeading = fontGenerator.generateFont(fontParameter);

                        fontParameter.size = 24;
                        menuFontLabel = fontGenerator.generateFont(fontParameter);

                        fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/fonts/captionFont.ttf"));
                        fontParameter.size = 14;

                        menuFontCaption = fontGenerator.generateFont(fontParameter);
                    }

                    camera.position.x = 512;
                    camera.position.y = 384;
                    camera.update();

                    loading = true;
                    audioClass.stopAllSounds();
                } else if (ImageHandler.doneLoading){
                    loading = false;
                    loaded = true;
                    staticScreen = 0;
                    whiteAlpha = 0;
                    renderReady = true;
                    Discord.updateStatus = true;
                    playDeluxe = true;
                    blackFade = 0;

                    ratBox.setState(true, data.RatAI);
                    catBox.setState(true, data.CatAI);

                    buttonConfigSync(data);
                    ratAIText = enemyAIText(ratBox);
                    ratAITextAlign = enemyAITextAlign(ratBox, ratAIText);

                    catAIText = enemyAIText(catBox);
                    catAITextAlign = enemyAITextAlign(catBox, catAIText);
                }
            } else {
                audioClass.stopAllSounds();
            }
        }
    }

    public static void render(SpriteBatch batch, Viewport viewport, float mx, float my, Data data){
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        if (!renderReady) return;
        if (screenBuffer == null){
            screenBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }

        if (rainbowStarBuffer == null){
            rainbowStarBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 768, true);
        }
        rainbowStarBuffer.begin();

        renderStar(batch, normalStar, 0, data);
        renderStar(batch, laserStar, 1, data);
        renderStar(batch, cassetteStar, 2, data);
        renderStar(batch, batteryStar, 3, data);
        renderStar(batch, catStar, 4, data);
        renderStar(batch, allChallengesStar, 5, data);

        batch.flush();
        rainbowStarBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        screenBuffer.begin();

        float r = 0.6f;
        float b = 1;

        //static
        Texture texture = ImageHandler.images.get("Static/Static" + ((int) staticScreen + 1));
        batch.setColor(r / 1.5f, 0, b / 1.5f, 1);
        batch.draw(texture, 0, 0);
        texture = ImageHandler.images.get("Static/GameoverStatic" + ((int) staticScreen + 1));
        batch.setColor(r / 1.5f, 0, b / 1.5f, 0.75f);
        batch.draw(texture, 0, 0);

        //characters

        texture = ImageHandler.images.get("menu/cat");
        float color = 0.5f + catAlpha;
        batch.setColor(1, 1, 1, color / 2 + 0.5f);
        batch.draw(texture, charactersX + 562, charactersY + 88);

        texture = ImageHandler.images.get("menu/rat");
        color = 0.5f + ratAlpha;
        batch.setColor(1, 1, 1, color / 2 + 0.5f);
        batch.draw(texture, charactersX + 13, charactersY + 84);

        //character buttons box
        customNightAIBox(batch, r, b);
        batch.setColor(1, 1, 1, 1);

        //option boxes
        challengeBoxRender(batch, button1, r, b);
        challengeBoxRender(batch, button2, r, b);
        challengeBoxRender(batch, button3, r, b);
        challengeBoxRender(batch, button4, r, b);

        challengeBoxRender(batch, button5, r, b);
        challengeBoxRender(batch, button6, r, b);
        challengeBoxRender(batch, button7, r, b);

        //ready
        texture = ImageHandler.images.get("menu/ready");
        batch.setColor(r, 0, b, 0.75f + readyAlpha);
        batch.draw(texture, readyButton.x, readyButton.y);

        //icons
        batch.setColor(r, 0, b, 1);
        texture = ImageHandler.images.get("menu/info");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.draw(texture, infoBox.x, infoBox.y);

        texture = ImageHandler.images.get("menu/help");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.draw(texture, helpBox.x, helpBox.y);
        batch.setColor(1, 1, 1, 1);

        batch.flush();
        screenBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        texture = screenBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());

        menuFontTitle.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontTitle.setColor(r, 0, b, 1);
        String text = "Shadow Rat & Cat Remake";
        layoutModeTemp.reset();
        layoutModeTemp.setText(menuFontTitle, text);
        menuFontTitle.draw(batch, text, 512 - layoutModeTemp.width / 2, 768 - 18);

        menuFontLabel.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontLabel.setColor(r, 0, b, 1);
        text = "Click to adjust AI";
        layoutModeTemp.reset();
        layoutModeTemp.setText(menuFontLabel, text);
        menuFontLabel.draw(batch, text, 512 - layoutModeTemp.width / 2, 704);

        menuFontHeading.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuFontHeading.setColor(r, 0, b, 1);

        text = data.options == 0 ?
                (data.pointer == 3 ? "Pixel" : data.pointer == 2 ? "OG Laser" : "Laser") + " Pointer" :
                data.options == 1 ? "Flash Debug" : "V-Sync";
        menuFontHeading.draw(batch, text, button1.x + 40, button1.y + 26);

        text = data.options == 0 ? "Hard Cassette" : data.options == 1 ? "Hitbox Debug" : "Mute Jumpscare";
        menuFontHeading.draw(batch, text, button2.x + 40, button2.y + 26);

        text = data.options == 0 ? "Limited Battery" : data.options == 1 ? "Light Room Debug" : "Menu Music";
        menuFontHeading.draw(batch, text, button3.x + 40, button3.y + 26);

        text = data.options == 0 ? "Classic Cat" : data.options == 1 ? "Free Scroll" : "OG Music";
        menuFontHeading.draw(batch, text, button4.x + 40, button4.y + 26);

        menuFontHeading.draw(batch, "Challenges", button5.x + 40, button5.y + 26);
        menuFontHeading.draw(batch, "Options", button6.x + 40, button6.y + 26);
        menuFontHeading.draw(batch, "Settings", button7.x + 40, button7.y + 26);

        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();

        if (textValue > 0) {
            float distance = 20;
            float shapeHeight = distance * (textRenderList.size() + 1) - 8;
            float height = captionPosition[1] - shapeHeight / 2;
            if (captionPosition[1] < 124){
                height = captionPosition[1];
            }

            batch.setColor(r / 3, 0, b / 3, textValue * 3.5f);

            float x = captionPosition[0];

            if (captionAlignment == 1){
                x -= (float) captionWidth / 2;
            } else if (captionAlignment == 2){
                x -= captionWidth - 10;
            }

            float tempX = x;
            if (captionAlignment == 2) {
                tempX -= 30;
            } else if (captionAlignment == 0){
                tempX += 5;
            } else {
                tempX -= 10f;
            }

            batch.draw(texture, tempX, height, captionWidth + 10, shapeHeight);

            menuFontCaption.setColor(1, 1, 1, textValue * 4);
            menuFontCaption.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            height = shapeHeight / 2;
            if (captionPosition[1] < 124){
                height = shapeHeight;
            }

            tempX += 5;

            for (int i = 0; i < textRenderList.size(); i++) {
                float y = captionPosition[1] - (distance * i) - 10;
                menuFontCaption.draw(batch, textRenderList.get(i), tempX, y + height);
            }
        }

        //stars
        batch.setColor(1, 1, 1, 1);
        texture = rainbowStarBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        batch.draw(texture, 0, texture.getHeight(), texture.getWidth(), -texture.getHeight());

        texture = FNaC3Deluxe.shapeBuffer.getColorBufferTexture();
        batch.setColor(0, 0, 0, 1 - blackFade);
        batch.draw(texture, 0, 0);

        batch.setColor(1, 1, 1, whiteAlpha);
        batch.draw(texture, 0, 0);
        batch.setColor(1, 1, 1, 1);

        renderReady = loaded;

        batch.flush();
    }

    private static void challengeBoxRender(SpriteBatch batch, Button button, float r, float b){
        Texture texture;
        texture = ImageHandler.images.get("menu/challenge_" + (button.getState() == 1 ? "on" : "off"));

        if (button.isHovering()) batch.setColor(1, 1, 1, (float) 1);
        else batch.setColor(r, 0, b, (float) 1);

        batch.draw(texture, button.x, button.y);
    }

    private static void menuTextShapeWidth(){
        textRenderList.clear();
        textRenderList.addAll(Arrays.asList(textBuilder.toString().split("\n")));

        if (!captionLayoutWidthInformation.containsKey(textToRender)) {
            int maxWidth = 0;
            GlyphLayout layout = new GlyphLayout();

            for (String s: textRenderList) {
                layout.setText(menuFontCaption, s);
                maxWidth = (int) Math.max(maxWidth, layout.width);
                layout.reset();
            }
            captionLayoutWidthInformation.put(textToRender, maxWidth);
        }
    }

    private static void textModifier(String text){
        if (textToRender != null && textToRender.equals(text)) return;

        textToRender = text;
        textBuilder.delete(0, textBuilder.length());
        if (textValues.containsKey(text)){
            textBuilder.append(textValues.get(text));
        } else {
            textBuilder.append(text);
            int index = -1;
            int linePosition = 0;
            int difference = 0;
            int distance = 45;
            for (int i = 0; i < text.length(); i++) {
                linePosition++;
                difference++;
                if (text.charAt(i) == ' ') {
                    index = i;
                    difference = 0;
                }

                if (text.charAt(i) == '\n'){
                    index = -1;
                    difference = 0;
                    linePosition = 0;
                } else if (linePosition % distance == 0) {
                    textBuilder.replace(index, index + 1, "\n");
                    linePosition = difference;
                }
            }
            textValues.put(text, textBuilder.toString());
        }
    }

    private static float nightCharacterAlpha(boolean condition2, float alpha){
        if (condition2) {
            if (alpha < 0.5f) {
                alpha += Gdx.graphics.getDeltaTime() * 4;
                if (alpha > 0.5f) alpha = 0.5f;
            }
        } else {
            if (alpha > 0) {
                alpha -= Gdx.graphics.getDeltaTime() * 4;
                if (alpha < 0) alpha = 0;
            }
        }
        return alpha;
    }

    private static int nightCustomNightAI(AudioClass audioClass, Button enemyBox, int enemyCase, String text){
        if (enemyBox.isLeftPressed() || enemyBox.isRightPressed()){
            enemyBox.setState(enemyBox.isLeftPressed(), Math.min(enemyBox.getState() + 1, 4));
            enemyBox.setState(enemyBox.isRightPressed(), Math.max(enemyBox.getState() - 1, 0));
            writeConfig = true;
        }
        if (enemyBox.isHovering()) {
            textModifier(text);
            textCase = 15;
            Menu.enemyCase = enemyCase;
            captionAlignment = 1;
            captionPosition[0] = enemyBox.x + (float) enemyBox.width / 2 + charactersX;
            captionPosition[1] = enemyBox.y - 40 + charactersY;
        }
        return enemyBox.getState();
    }

    private static void customNightAIBox(SpriteBatch batch, float r, float b){
        renderDifficulty(batch, ratBox, ratAIText, ratAITextAlign, 20, r, b);
        renderDifficulty(batch, catBox, catAIText, catAITextAlign, 0, r, b);
    }

    private static void renderDifficulty(SpriteBatch batch, Button enemyBox, String aiText, int textAlign, int yOffset, float r, float b){
        batch.setColor(r, 0, b, (0.75f + enemyBox.getFrame() / 4));

        var texture = ImageHandler.images.get("menu/ai_box");
        float boxAlignX = (float) enemyBox.width / 2 - (float) texture.getWidth() / 2;
        float boxAlignY = (float) enemyBox.height / 4 - (float) texture.getHeight() / 2;
        batch.draw(texture, enemyBox.x + boxAlignX + charactersX, enemyBox.y + boxAlignY + charactersY + yOffset);

        int limit = enemyBox.getState();
        int offset = 0;
        for (int i = 0; i < limit; i++) {
            batch.draw(FNaC3Deluxe.shapeBuffer.getColorBufferTexture(),
                    enemyBox.x +  boxAlignX + 13 + offset + charactersX,
                    enemyBox.y + 12 + boxAlignY + yOffset + charactersY,
                    90, 40);
            offset += 98;
        }

        batch.setColor(1, 1, 1, 1);
        menuFontTitle.setColor(r, 0, b, (0.75f + enemyBox.getFrame() / 4));
        menuFontTitle.draw(batch, aiText,
                textAlign + charactersX, enemyBox.y + boxAlignY + 110 + yOffset + charactersY);
    }

    private static void renderStar(SpriteBatch batch, Button starButton, int starIndex, Data data){
        int value = data.saveData.stars[starIndex];
        Texture texture = ImageHandler.images.get("menu/star" + (starIndex < 5 ? "Mini" : ""));
        if (value == 0) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        else if (value == 1) batch.setColor(0.9f, 0, 0.1f, 1);
        else if (value == 2) batch.setColor(0.75f, 0, 1, 1);
        else if (value == 3) batch.setColor(1, 0.82f, 0, 1);
        else batch.setColor(1, 1, 1, 1);

        int srcFunc = batch.getBlendSrcFunc();
        int dstFunc = batch.getBlendDstFunc();

        if (value == 4) {
            float multiplier = 1;
            if (starIndex < 5) multiplier = 0.525f;
            Texture rainbowTexture = ImageHandler.images.get("menu/rainbow" + (starIndex < 5 ? "Mini" : ""));
            TextureRegion region = new TextureRegion(rainbowTexture);
            region.setRegion((int) (rainbowPosition * multiplier), 0, starButton.width, starButton.height);
            batch.draw(region, starButton.x, starButton.y);
            batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
        }

        batch.draw(texture, starButton.x, starButton.y);

        if (value == 4){
            batch.flush();
            batch.setBlendFunction(srcFunc, dstFunc);
        }

        batch.setColor(1, 1, 1, 1);
    }

    private static String enemyAIText(Button box){
        return "Difficulty: " + (box.getState() == 0 ? "Off" : box.getState() == 1 ? "Easy" :
                box.getState() == 2 ? "Medium" : box.getState() == 3 ? "Hard" : "Monster");

    }

    private static int enemyAITextAlign(Button box, String difficulty){
        layoutModeTemp.reset();
        layoutModeTemp.setText(menuFontTitle, difficulty);
        return (int) ((float) box.width / 2 - layoutModeTemp.width / 2 + box.x);
    }

    private static void buttonConfigSync(Data data){
        button1.setState(data.options == 0, data.pointer > 0 ? 1 : 0);
        button2.setState(data.options == 0, data.hardCassette ? 1 : 0);
        button3.setState(data.options == 0, data.limitedBattery ? 1 : 0);
        button4.setState(data.options == 0, data.classicCat ? 1 : 0);

        button1.setState(data.options == 1, data.flashDebug ? 1 : 0);
        button2.setState(data.options == 1, data.hitboxDebug ? 1 : 0);
        button3.setState(data.options == 1, data.lightDebug ? 1 : 0);
        button4.setState(data.options == 1, data.freeScroll ? 1 : 0);

        button1.setState(data.options == 2, data.vSync ? 1 : 0);
        button2.setState(data.options == 2, data.muteJumpscare ? 1 : 0);
        button3.setState(data.options == 2, data.menuMusic ? 1 : 0);
        button4.setState(data.options == 2, data.ogMusic ? 1 : 0);

        button5.setState(true, data.options == 0 ? 1 : 0);
        button6.setState(true, data.options == 1 ? 1 : 0);
        button7.setState(true, data.options == 2 ? 1 : 0);

        if (data.ogMusic) menuMusicName = "menu";
        else menuMusicName = "deluxeMenu";
    }
}
