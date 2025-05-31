package com.fnac3.deluxe.core.discord;

import com.fnac3.deluxe.core.data.Data;
import com.fnac3.deluxe.core.state.Game;
import com.fnac3.deluxe.core.state.Menu;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;

public class Discord {

    private static DiscordRichPresence presence;
    private static DiscordRPC lib;
    public static boolean updateStatus;

    private static final StringBuilder builder = new StringBuilder();

    public static void start(){
        presence = new DiscordRichPresence();
        lib = DiscordRPC.INSTANCE;
        String appID = "1126361619094589491";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = new DiscordEventHandlers.OnReady(){
            @Override
            public void accept(DiscordUser user) {
                System.out.println("Ready!");
            }
        };
        lib.Discord_Initialize(appID, handlers, true, null);
    }

    public static void update(Data data, String state, String gameMode, int hour, boolean cheats){
        switch (state) {
            case "MENU" -> builder.append("In Menu");
            case "LOADING" -> builder.append("Loading Game");
            case "GAME" -> {
                if (Game.gameover) {
                    builder.append(Game.gameoverReason).append(" at ").append(hour).append(" AM");
                } else {
                    builder.append("Time: ").append(hour).append(" AM");
                    if (cheats) builder.append(" | Cheating");
                }
            }
        }

        presence.details = builder.toString();
        builder.delete(0, builder.length());

        int numberOfChallenges = 0;

        if (data.pointer > 0){
            numberOfChallenges++;
        }

        if (data.hardCassette){
            numberOfChallenges++;
        }

        if (data.limitedBattery){
            numberOfChallenges++;
        }

        if (data.classicCat){
            numberOfChallenges++;
        }

        if (Game.win) {
            builder.append("Mode Beaten: ").append(gameMode);
            challengeBuilder(data, numberOfChallenges);
        } else {
            builder.append("Mode: ").append(gameMode);
            challengeBuilder(data, numberOfChallenges);
        }

        presence.state = builder.toString();
        builder.delete(0, builder.length());

        presence.largeImageKey = "dreamscape";
        lib.Discord_UpdatePresence(presence);
        updateStatus = false;
    }

    private static void challengeBuilder(Data data, int numberOfChallenges){
        if (numberOfChallenges == 4){
            builder.append(" All Challenges");
        } else if (numberOfChallenges != 0){
            builder.append(" + ").append(numberOfChallenges).append(" Challenge");
            if (numberOfChallenges > 1) builder.append("s");
        }

        if (data.freeScroll) builder.append(" + Free Scroll");
    }

    public static void end(){
        lib.Discord_Shutdown();
        lib.Discord_ClearPresence();
    }
}
