package com.fnac3.deluxe.core.data;

public class TextString {

    private static final String version = "Beta v1.0.5";

    public static String ratText = "Rat comes from the doors. Flashing him scares him away. Once he gets in, "
            + "he will wait to begin his attack. He also teleports while attacking. Afterwards, he will go "
            + "under the bed. Find where he is under the bed and face the opposite side "
            + "of him in the room. He will eventually peek out. Flash him to scare him away.";

    public static String catText = "Cat will camp under the bed and will eventually come out to do his attack. He has "
            + "two attack phases, and you must also stall Rat while mid-attack. He will then have a big cooldown timer "
            + "before repeating his cycle.";

    public static String classicCatText = "Classic Cat will be behind the bed posts. making progress towards you. "
            + "Flashing him will pause him for 5 seconds. Flashing him all the away will scare him away.";

    public static String hardClassicCatText = "Classic Cat will be behind the bed posts. making progress towards you. "
            + "Flashing him will pause him for 2.5 seconds. Flashing him all the away will scare him away.";

    public static String laserPointerText = "The flashlight and hitbox size decreases.";

    public static String hardCassetteText = "The cassette must be played to slowly progress through the night and stay awake. "
            + "The night's progression will rewind if left tampered";

    public static String hardcoreCassetteText = "The cassette must be played to slowly progress through the night and stay awake. " +
            "You cannot recover yourself from fading out. The night's progression will rewind if left tampered";

    public static String limitedBattery = "The flashlight batteries are limited and will run out. "
            + "Grab batteries to continue flashing monsters.";

    public static String deadlyBattery = "The flashlight batteries are limited and will run out quicker. "
            + "Grab batteries to continue flashing monsters.";

    public static String hitboxText = "The hitboxes are visible.";

    public static String flashHPText = "The health bar for attack phase is visible.";

    public static String timerText = "The room lights are on";

    public static String freeScrollText = "This setting prevents you from automatically locking "
            + "into place when attacking a monster.";

    public static String infoText = version + "\n" + """
            Creator: Official_LR
            Original FNaC 3 creator: Emil Macko
            FNaC 3 But Better creator: Mr. Manatee""";

    public static String helpText = """
            R: Restart night
            F2: Return to menu
            F4: Change screen resolution""";
}
