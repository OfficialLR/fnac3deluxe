package com.fnac3.deluxe.core.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.state.StateManager;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageHandler {

    public static Map<String, Texture> images = new HashMap<>();
    private static final Map<String, Pixmap> pixmaps = new HashMap<>();
    private static final Queue<String> queue = new LinkedList<>();
    private static ExecutorService serviceLoader;
    private static boolean terminated;
    public static int currentPercent;
    private static int otherCurrentPercent;
    public static int maxPercent;
    public static boolean loading;
    public static boolean doneLoading;
    private static long time;

    public static void load(){
        if (!loading && !doneLoading){
            terminated = false;
            time = System.currentTimeMillis();
            currentPercent = 0;
            otherCurrentPercent = 0;
            pixmaps.clear();
            serviceLoader = Executors.newFixedThreadPool(3);
            queue.forEach(key -> serviceLoader.execute(() -> {
                var pixmap = loadImageBuffer(key);
                pixmaps.put(key, pixmap);
                currentPercent++;
            }));
            serviceLoader.shutdown();
            loading = true;
        } else {
            if (serviceLoader.isTerminated()) {
                terminated = true;
            }
            if (!terminated) return;
            for (String key : queue) {
                if (!pixmaps.containsKey(key) || images.containsKey(key)) continue;
                Pixmap pixmap = pixmaps.get(key);
                Texture texture = new Texture(pixmap);
                pixmap.dispose();
                images.put(key, texture);
                otherCurrentPercent++;
                if (otherCurrentPercent != maxPercent) continue;
                loading = false;
                System.out.println(System.currentTimeMillis() - time + "ms");
                queue.clear();
                pixmaps.clear();
                doneLoading = true;
                break;
            }
        }
    }

    public static Pixmap loadImageBuffer(String path){
        path = "assets/" + path + ".png";
        int[] width = new int[1];
        int[] height = new int[1];
        ByteBuffer bytes = STBImage.stbi_load(path, width, height, new int[1], 0);
        if (bytes == null) throw new RuntimeException("Image not loaded: " + path);
        Pixmap pixmap = new Pixmap(width[0], height[0], Pixmap.Format.RGBA8888);
        pixmap.setPixels(bytes);
        STBImage.stbi_image_free(bytes);
        return pixmap;
    }
    
    public static void add(String path){
        queue.add(path);
        maxPercent++;
        doneLoading = false;
    }

    public static void addImages(StateManager stateManager){
        ImageHandler.dispose();
        if (stateManager.getState() == StateManager.State.LOADING) {
            gameLoad();
        } else if (stateManager.getState() == StateManager.State.MENU){
            menuLoad();
        }
    }

    private static void gameLoad(){
        roomLoad();
        Game.loadPixmaps();
        add("game/Buttons/TapePlayer");
        add("game/Buttons/TapePlayerBack");
        add("game/Buttons/UnderBed");
        add("game/Buttons/UnderBedBack");

        for (int i = 1; i <= 8; i++){
            add("Static/GameoverStatic" + i);
        }

        for (int i = 1; i <= 4; i++){
            add("Clock/Clock" + i);
        }

        for (int i = 0; i <= 6; i++){
            add("game/time/" + (i == 0 ? 12: i) + "AM");
        }

        add("game/Flashlight");
        shadowNight();
    }

    private static void menuLoad(){
        for (int i = 1; i <= 8; i++){
            add("Static/Static" + i);
            add("Static/GameoverStatic" + i);
        }
        add("menu/challenge_off");
        add("menu/challenge_on");
        add("menu/ready");
        add("menu/star");
        add("menu/starMini");
        add("menu/rainbow");
        add("menu/rainbowMini");
        add("menu/ai_box");
        add("menu/info");
        add("menu/help");

        add("menu/rat");
        add("menu/cat");
        add("menu/classicCat");
    }

    public static void roomLoad(){
        String prefix = "game/room/";
        add(prefix + "FullRoom");
        add(prefix + "FullRoomEffect");
        add(prefix + "FullRoomBed");
        add(prefix + "UnderBed");
        add(prefix + "UnderBedEffect");

        String[] movingImages = new String[]{"Turn Around", "Turn Back", "Under Bed"};
        for (String move: movingImages){
            for (int i = 1; i <= 11; i++){
                if (i == 11 && move.equals("Under Bed")) break;
                add(prefix + "Moving/" + move + "/Moving" + i);
            }
        }

        prefix += "Tape/";

        add(prefix + "Tape");
        add(prefix + "TapeMissing");
        add(prefix + "battery");

        prefix += "Buttons/";

        add(prefix + "Play1");
        add(prefix + "Play2");
        add(prefix + "Play3");

        add(prefix + "Rewind1");
        add(prefix + "Rewind2");
        add(prefix + "Rewind3");

        add(prefix + "StopPlay1");
        add(prefix + "StopRewind1");
        add(prefix + "Stop2");
        add(prefix + "Stop3");
    }

    public static void shadowNight(){
        add("game/ShadowBattleOverlay");

        ratLoad();
        catLoad();
    }

    private static void ratLoad(){
        if (!Game.rat.isActive()) return;
        String prefix = "game/enemy/Rat/Shadow/";

        triangleAttackLoad(prefix);
        doorAssetsLoad(prefix);
        tapeAssetsLoad(prefix);
        bedAssetsLoad(prefix);
        crouchAssetsLoad(prefix, 2);

        for (int i = 1; i <= 6; i++) {
            add(prefix + "Jumpscare/Jumpscare" + i);
        }
//        add("game/gameover/shadowRat");
    }

    private static void catLoad() {
        boolean catGameOver = false;
        if (Game.cat.isActive()) {
            catGameOver = true;
            String prefix = "game/enemy/Cat/Shadow/";

            triangleAttackLoad(prefix);
            bedAssetsLoad(prefix);
            crouchAssetsLoad(prefix, 22);

            for (int i = 1; i <= 6; i++) {
                add(prefix + "Jumpscare/Jumpscare" + i);
            }
        }

        if (Game.classicCat.isActive()) {
            catGameOver = true;
            String prefix = "game/enemy/ClassicCat/Shadow/";

            for (int i = 1; i <= 58; i++){
                add(prefix + "Retreat/Left/Retreat" + i);
                add(prefix + "Retreat/Right/Retreat" + i);
            }

            for (int i = 1; i <= 6; i++) {
                add(prefix + "Jumpscare/Jumpscare" + i);
            }
        }

        if (catGameOver) {
//            add("game/gameover/shadowCat");
        }
    }

    private static void triangleAttackLoad(String prefix){
        for (int i = 1; i <= 6; i++) {
            add(prefix + "Attack/Left/Attack" + i);
            add(prefix + "Attack/Left/AttackMove" + i);
            add(prefix + "Attack/Middle/Attack" + i);
            add(prefix + "Attack/Middle/AttackMove" + i);
            add(prefix + "Attack/Right/Attack" + i);
            add(prefix + "Attack/Right/AttackMove" + i);
        }
    }

    private static void doorAssetsLoad(String prefix){
        for (int i = 1; i <= 13; i++) {
            add(prefix + "Door/Peek/Left/Peek" + i);
            add(prefix + "Door/Peek/Middle/Peek" + i);
            add(prefix + "Door/Peek/Right/Peek" + i);
        }

        for (int i = 1; i <= 18; i++) {
            add(prefix + "Door/Leaving/Left/Leaving" + i);
            add(prefix + "Door/Leaving/Right/Leaving" + i);
        }
    }

    private static void bedAssetsLoad(String prefix){
        add(prefix + "Bed/Left");
        add(prefix + "Bed/Right");
    }

    private static void crouchAssetsLoad(String prefix, int framesTotal){
        for (int i = 1; i <= framesTotal; i++){
            add(prefix + "Crouch/Left/Crouch" + i);
            add(prefix + "Crouch/Right/Crouch" + i);
        }
    }

    private static void tapeAssetsLoad(String prefix){
        for (int i = 1; i <= 12; i++) {
            add(prefix + "Tape/Tape" + i);
        }
    }

    public static void dispose(){
        maxPercent = 0;
        images.values().forEach(Texture::dispose);
        images.clear();
    }
}
