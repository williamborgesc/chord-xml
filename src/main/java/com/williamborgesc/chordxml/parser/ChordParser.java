package com.williamborgesc.chordxml.parser;

import generated.KindValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordParser {

    public static KindValue getChordKind(String chord) {
        for (ChordParser.KindPatterns kindPattern : ChordParser.KindPatterns.values()) {
            Pattern pattern = Pattern.compile(kindPattern.pattern);
            Matcher matcher = pattern.matcher(chord);
            if (matcher.find()) {
                return KindValue.valueOf(kindPattern.name());
            }
        }
        return KindValue.MAJOR;
    }

    public static String getRootNote(String chord) {
        return chord.substring(0, 1);
    }

    public static String getNoteAlter(String chord) {
        Pattern pattern = Pattern.compile("^[A-G]([b#])(:?/[A-G][b#])?");
        Matcher matcher = pattern.matcher(chord);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static String getBassAlter(String chord) {
        Pattern pattern = Pattern.compile("/[A-G]([b#])");
        Matcher matcher = pattern.matcher(chord);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static String getBass(String chord) {
        Pattern pattern = Pattern.compile("/([A-G])[b#]?");
        Matcher matcher = pattern.matcher(chord);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static enum KindPatterns {
        MINOR("(m$)"),
        AUGMENTED("aug"),
        DIMINISHED("dim"),
        DOMINANT("[A-G][b#]?(7$)"),
        MAJOR_SEVENTH("maj7|7M"),
        MINOR_SEVENTH("m7"),
        DIMINISHED_SEVENTH("dim7"),
        AUGMENTED_SEVENTH("aug7"),
        HALF_DIMINISHED("7b5"),
        MAJOR_MINOR("mmaj7"),
        MAJOR_SIXTH("[A-G][b#]?(6)"),
        MINOR_SIXTH("m6"),
        DOMINANT_NINTH("[A-G][b#]?(9)"),
        MAJOR_NINTH("maj9"),
        MINOR_NINTH("m9"),
        DOMINANT_11_TH("[A-G][b#]?(11)"),
        MAJOR_11_TH("maj11"),
        MINOR_11_TH("m11"),
        DOMINANT_13_TH("[A-G][b#]?(13)"),
        MAJOR_13_TH("maj13"),
        MINOR_13_TH("m13"),
        SUSPENDED_SECOND("(sus2|([A-G][b#]?(2)))"),
        SUSPENDED_FOURTH("(sus|([A-G][b#]?(4)))");

        String pattern;

        KindPatterns(String pattern) {
            this.pattern = pattern;
        }
    }
}
