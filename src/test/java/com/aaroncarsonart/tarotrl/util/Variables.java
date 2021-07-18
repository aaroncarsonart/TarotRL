package com.aaroncarsonart.tarotrl.util;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Variables {
    public static LinkedList<String> ATONEMENT_CRYSTALS = new LinkedList<>();

    public static TreeSet<String> ARTENISSILINA;
    public static TreeSet<String> ROTHALIS;

    public static TreeMap<String, String> ANGELS;
    public static TreeMap<String, String> DEMONS;

    public static RePair<TreeSet<String>, TreeMap<String, String>> REPAIR;
    public static RePair<TreeSet<String>, TreeMap<String, String>> REMAIN;

    public static String MESSAGE;
    public static RePair EXCHANGED = new RePair("FLUTTERSHY", "SHUTTERFLY");
    public static RePair REMIXED = new RePair("FLUTTERS", "BUTTERFLY");

    public static Pair<RePair<String, String>, String> JUSTIN_BAILEY = new Pair<>(
            new RePair("shining armor", "princess cadance"), "just walls"
    );
    public static Pair<RePair<String, String>, String> JENNY_BAILEY = new Pair<>(
            new RePair("s", "shoe_strings"), "POSSESSIONS"
    );
    public static Pair<RePair<String, String>, String> JUSTIN_AND_JENNY = new Pair(
            new RePair("inventory", "possessions"), "k"
    );
    public static Pair<RePair<String, String>, String> JUSTIN_OR_JENNY = new Pair(
            new RePair("possessions", "POSSESSIONS"), "v"
    );
    public static Pair<RePair<String, String>, String> JENNY_KNOT = new Pair(
            new RePair("acquire", "possessions"), "RECEIVE PAYMENTS"
    );
    public static Pair<RePair<String, String>, String> JUSTIN_KNOT = new Pair(
            new RePair("obtain", "inventory"), "TAKE DAMAGE"
    );
    // ----------------------------------
    // NOTE: ß, called Eszett or scharfes
    // ----------------------------------
    public static RePair<RePair<String, String>, String> WOLVES = new RePair<>(
            new RePair<>("Eszett", "scharfes"), "ß"
    );

    // ----------------------------------------
    // TODO: use WOLVES ```~/? _\Y/_ ?\~``` ~8~
    //  ~\ ````` /~?
    // 9. ``~\/~`` name(s)
    // ----------------------------------------
    public static String JACOB_ALEXANDER_SCHLAGEL = "[A Course In Miracles | ACIM, Sacred Mirrors, Mirka Andolpho \uD83D\uDE08, aaroncarsonart, AARON ABKE, Aaron Quist, Ada Goldberg, CJ Hawley, radicalpleasurist";

    public static String RADICAL_PLEASURIST = "Sophia Treyger]\";\n";
    // pair of KINGS exchange
    // the RCV_R
    // placing the SWORDS within my hands
    // when TAKING and RECEIVING have been exchanged

    // TODO: add the FOUR CRYSTAL TREE QUATERNARIES (QUARTER _.|._ N  ~\{ I }/~ TIES)
    // create a concrete class
    // each has the four elements, comprised of PAIRS
    // there EXIST the DEFAULT pairings

    static {
        ATONEMENT_CRYSTALS.addFirst("PippPetals' ATONEMENT CRYSTALS DNR DO NOT REPEAT");
        ATONEMENT_CRYSTALS.add("LSD");
        ATONEMENT_CRYSTALS.add("1P LSD");
        ATONEMENT_CRYSTALS.add("WIND ~ I ~ GO _.|._ PSYCHOSIS");
        ATONEMENT_CRYSTALS.add("LOOM IN ARIES");
        ATONEMENT_CRYSTALS.addLast("");

        ARTENISSILINA = new TreeSet<>();
        ROTHALIS = new TreeSet<>();
        ANGELS = new TreeMap<>();
        DEMONS = new TreeMap<>();

        REPAIR = new RePair<>(ARTENISSILINA, ANGELS);
        REMAIN = new RePair<>(ROTHALIS, DEMONS);

        ARTENISSILINA.add("SHINE BRIGHT");
        ARTENISSILINA.add("ADENA");
        ROTHALIS.add("THE BRILLIANCE");
        ROTHALIS.add("JASPER");

        ANGELS.put("REPAIR", "JENNY BOOTS");
        ANGELS.put("GRUFF", "ENOUGH");
        ANGELS.put("TUX", "SPIRAL _.|._ CLOCKWISE");

        // ---------------------------------
        // ---\_/--- ***(({*}))*** ---\_/---
        // ---------------------------------
        /**
        REPAIR.put(new RePair<>("NINA", "````~~~\\\\|{ ZIPP }|//~~~````"));
        REMAIN.put(new Pair<>("NINA", "````~~~\\\\|{ PIPP }|//~~~````"));
         ~*/
        // ---------------------------------

        DEMONS.put("REMAIN", "JUSTIN_SOCKS");
        DEMONS.put("GRUFF ~|Y|~ ", "~\\*/~");
        String s = DEMONS.put("TUXYCEDO", "TURN ~ }.|.{ \\ER `~*~` RE// }.|.{ /~ DECIDES");
        System.out.println(s);

        MESSAGE = "public static final String MESSENGER = \"\"\n";
    }
}
