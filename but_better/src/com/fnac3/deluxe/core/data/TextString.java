package com.fnac3.deluxe.core.data;

public class TextString {

    private static final String version = "Beta v2.0.1";

    public static String ratText = "Rat will appear from the doors. After he comes in, he will "
            + "wait to begin his attack. After his attack, he will go under the bed and you must "
            + "find him under the bed, then go back up the bed and face the opposite side of him. "
            + "He will eventually peek out of the bed, and you must shine his face to make him go "
            + "away.";

    public static String catText = "Cat will camp under the bed and eventually come out and go to "
            + "either far left or right side of the bed. He will try to advance towards you to "
            + "jumpscare you. Flash him to make him go back under the bed and reset his progression.";

    public static String vinnieText = "Vinnie will appear from the doors. After he comes in, he "
            + "will wait to begin his attack. During the attack, he will jump three times. "
            + "After his attack, he will go under the bed and you must find him under the bed, "
            + "then go back up the bed and face the opposite side of him. He will eventually peek "
            + "out of the bed, and you must shine his face to make him go away.";

    public static String shadowRatText = "Rat will appear from the doors. Flash him to make him go away. "
            + "If he gets in while Vinnie is attacking, he'll kill you. Otherwise, he'll get in and do his attack "
            + "with more speed and moves. If Vinnie is under the bed, he'll leave. Otherwise, he'll go under the bed "
            + "before he leaves.";

    public static String shadowCatText = "Cat will be on either side of the bed. "
            + "Once he appears, you have 15 seconds until he kills you. Flashing him pauses his timer. Flashing him "
            + "fully will make him retreat.";

    public static String shadowVinnieText = "Vinnie will enter your room immediately. "
            + "He will start attacking jumping around as well. He does NOT twitch, but he can still be flashed. "
            + "When under the bed, he will do a cooldown before coming back up. You must look under the bed, or "
            + "he'll kill you to prevent guessing.";

    public static String candyText = "Candy will appear from either side of the bed. Flash him before he kills you.";

    public static String twitchyCatText = "Shadow Cat will twitch more and do another attack up close before going "
            + "under the bed.";

    public static String laserPointerText = "The flashlight and hitbox size decreases.";

    public static String hardCassetteText = "The cassette must be played to keep awake and slowly progress throughout "
            + "the night. Monsters will mess up your cassette automatically, tampering with the night's progression.";

    public static String monstergami = "Monstergami will eventually appear in the room. His aggression gets worse "
            + "throughout the night. If you flash him enough but you were slow, he will go under the bed to do another "
            + "attack. He will steal the cassette if it's playing while he's present. He is free scroll mandatory!";

    public static String faultyFlashlight = "The flashlight batteries are limited and will die. "
            + "Eventually, a new set of batteries will appear next to the cassette. "
            + "If the batteries run out, you can't flash the monsters.";

    public static String allChallengesText = "Enable/Disable all challenges.";

    public static String oldMenuText = "Enjoy the old FNaC 3 Custom Night Menu.";

    public static String hitboxText = "The hitboxes for the room sequences and bed peek phases are "
            + "visible.";

    public static String flashHPText = "The flashlight health bar for attack sequences is visible.";

    public static String freeScrollText = "This setting prevents you from automatically locking "
            + "into place when battling a monster.";

    public static String expandedPointerText = "The flashlight increases by 25% and the hitbox sizes "
            + "increase by 20%";

    public static String descriptionText = version + "\n" + """
            Creator: Official_LR
            Original FNaC 3 creator: Emil Macko""";

    public static String controlText = """
            R: Restart night
            F2: Return to menu
            F4: Change screen resolution""";
}
