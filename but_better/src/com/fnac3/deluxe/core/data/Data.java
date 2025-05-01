package com.fnac3.deluxe.core.data;

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
    public boolean timerDebug;
    public boolean freeScroll;

    public int pointer;
    public boolean hardCassette;
    public boolean limitedBattery;
    public boolean classicCat;

    public int options;

    public SaveData saveData;

    //file names
    private static final String externalName = "AppData/Roaming/Five Nights at Candy's 3 Deluxe/Save";
    static final String dirName = System.getProperty("user.home") + "/" + externalName;
    private static final String fileName = dirName + "/Game_ButBetter_v1.save";
    private static final String configFileName = dirName + "/Config_ButBetter_v1.save";
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
            timerDebug = readBooleanLineConfig(br);
            freeScroll = readBooleanLineConfig(br);

            pointer = readIntLineConfig(br);
            hardCassette = readBooleanLineConfig(br);
            limitedBattery = readBooleanLineConfig(br);
            classicCat = readBooleanLineConfig(br);
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
                .append("timerDebug = ").append(timerDebug).append("\n")
                .append("freeScroll = ").append(freeScroll).append("\n")
                .append("pointer = ").append(pointer).append("\n")
                .append("hardCassette = ").append(hardCassette).append("\n")
                .append("limitedBattery = ").append(limitedBattery).append("\n")
                .append("classicCat = ").append(classicCat).append("\n");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile))){
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeWin(int difficulty, boolean notCheating){
        if (notCheating){
            mode(difficulty);
        }
        writeModes();
    }

    private void mode(int difficulty){
        boolean[] arr = new boolean[]{true, pointer > 0, hardCassette, limitedBattery, classicCat};
        int starIndex = 0;

        for (boolean b : arr) {
            if (b) saveData.stars[starIndex] = Math.max(saveData.stars[starIndex], difficulty);
        }
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
