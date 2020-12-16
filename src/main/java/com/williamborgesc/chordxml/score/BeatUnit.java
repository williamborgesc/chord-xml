package com.williamborgesc.chordxml.score;

import java.util.HashMap;

public class BeatUnit {
    private static HashMap<String, String> beatUnit;

    static {
        beatUnit = new HashMap<>();

        beatUnit.put("64", "64th");
        beatUnit.put("32", "32nd");
        beatUnit.put("16", "16th");
        beatUnit.put("8", "eighth");
        beatUnit.put("4", "quarter");
        beatUnit.put("2", "half");
        beatUnit.put("1", "whole");
    }

    public static String fromBeatType(String beatType) {
        return beatUnit.get(beatType);
    }
}
