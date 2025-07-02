package com.fnac3.deluxe.core.data;

import com.fnac3.deluxe.core.state.Menu;

import java.io.*;

public class Data {

    public int RatAI;
    public int CatAI;

    public boolean vSync;
    public boolean muteJumpscare = false;
    public boolean menuMusic = true;
    public boolean ogMusic = false;

    public boolean flashDebug;
    public boolean hitboxDebug;
    public boolean lightDebug;
    public boolean freeScroll;

    public int pointer;
    public int cassette;
    public int limitedBattery;
    public int classicCat;
    public boolean shadowChallenge;

    public int options;

    public SaveData saveData;

    //file names
    private static final String name = "Reimagined_Beta_2";
    private static final String externalName = "AppData/Roaming/Five Nights at Candy's 3 Deluxe/Save";
    static final String dirName = System.getProperty("user.home") + "/" + externalName;
    private static final String fileName = dirName + "/" + name + ".save";
    private static final String configFileName = dirName + "/" + name + ".config";
    private File configFile;

    public Data(){
        saveData = new SaveData();
        readSaveFile();
        readConfigFile();
    }

    private void readSaveFile(){
        File file = new File(dirName);
        file.mkdirs();

        file = new File(fileName);

        if (!file.exists()) {
            writeNewSaveFile(file);
        }
        readModes();
        writeModes();
    }

    private void writeNewSaveFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readConfigFile(){
        File file = new File(dirName);
        file.mkdirs();

        configFile = new File(configFileName);

        if (!configFile.exists()) {
            writeNewConfigFile();
        } else {
            readConfig();
        }
    }

    private void writeNewConfigFile() {
        try {
            configFile.createNewFile();
            writeConfig();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readConfig(){
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            br.readLine();
            RatAI = readIntLineConfig(br);
            CatAI = readIntLineConfig(br);

            vSync = readBooleanLineConfig(br);
            muteJumpscare = readBooleanLineConfig(br);
            menuMusic = readBooleanLineConfig(br);
            ogMusic = readBooleanLineConfig(br);

            flashDebug = readBooleanLineConfig(br);
            hitboxDebug = readBooleanLineConfig(br);
            lightDebug = readBooleanLineConfig(br);
            freeScroll = readBooleanLineConfig(br);

            pointer = readIntLineConfig(br);
            cassette = readIntLineConfig(br);
            limitedBattery = readIntLineConfig(br);
            classicCat = readIntLineConfig(br);
            shadowChallenge = readBooleanLineConfig(br);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private boolean readBooleanLineConfig(BufferedReader br) throws IOException {
        String line = br.readLine();
        return Boolean.parseBoolean(line.substring(line.indexOf('=') + 2));
    }

    private int readIntLineConfig(BufferedReader br) throws IOException {
        String line = br.readLine();
        return Integer.parseInt(line.substring(line.indexOf('=') + 2));
    }

    public void writeConfig(){
        StringBuilder sb = new StringBuilder();
        sb.append("[Config]\n")
                .append("Rat AI = ").append(RatAI).append("\n")
                .append("Cat AI = ").append(CatAI).append("\n")
                .append("vSync = ").append(vSync).append("\n")
                .append("muteJumpscare = ").append(muteJumpscare).append("\n")
                .append("menuMusic = ").append(menuMusic).append("\n")
                .append("ogMusic = ").append(ogMusic).append("\n")
                .append("flashDebug = ").append(flashDebug).append("\n")
                .append("hitboxDebug = ").append(hitboxDebug).append("\n")
                .append("lightDebug = ").append(lightDebug).append("\n")
                .append("freeScroll = ").append(freeScroll).append("\n")
                .append("pointer = ").append(pointer).append("\n")
                .append("cassette = ").append(cassette).append("\n")
                .append("limitedBattery = ").append(limitedBattery).append("\n")
                .append("classicCat = ").append(classicCat).append("\n")
                .append("shadowChallenge = ").append(shadowChallenge).append("\n");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile))){
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeWin(Data data, int difficulty, boolean notCheating){
        if (notCheating){
            int[] stars;
            if (Menu.mode.equals("Monster Rat & Cat")) {
                stars = data.saveData.ratCatMonsterStars;
            } else {
                stars = data.saveData.ratCatShadowStars;
            }
            mode(difficulty, stars);
        }
        writeModes();
    }

    private void mode(int difficulty, int[] stars){
        boolean[] arr = new boolean[]{true, pointer > 0, cassette > 0, limitedBattery > 0, classicCat > 0};
        int starIndex = 0;
        boolean allChallenges = true;

        for (boolean b : arr) {
            if (b) stars[starIndex] = Math.max(stars[starIndex], difficulty);
            else allChallenges = false;
            starIndex++;
        }
        if (allChallenges) stars[starIndex] = Math.max(stars[starIndex], difficulty);
    }

    private void readModes(){
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)){
             saveData = (SaveData) ois.readObject();
        } catch (Exception e){
            saveData = new SaveData();
        }
    }

    private void writeModes(){
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(saveData);
        } catch (IOException e){
            saveData = new SaveData();
        }
    }
}
