package com.williamborgesc.chordxml;

import com.williamborgesc.chordxml.parser.ChordsParser;
import com.williamborgesc.chordxml.score.DefaultScore;
import com.williamborgesc.chordxml.score.KeyEnum;
import com.williamborgesc.chordxml.score.PartHelper;
import generated.Harmony;
import generated.KindValue;
import generated.ScorePartwise;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class Application {

    public static final String FLAT = "b";
    public static final String SHARP = "#";
    public static final String MINOR = "m";

    public static String key = "B";
    public static String timeSignature = "4/4";
    public static String songName = "Test";

    // TODO Tetrades
    // TODO inversions
    // TODO RITORNELLO
    // TODO check chords in key (probably fixed with #)
    // TODO %
    // TODO improve rehearse
    // TODO Create Screen

    public static void main(String[] args) throws JAXBException {
        List<String> measures = ChordsParser.parseFileToMeasures(new File("D:\\Dev\\eclipse-wp\\chord-xml\\source.txt"));
        ScorePartwise.Part part = new ScorePartwise.Part();

        if (!timeSignature.contains("/")) {
            throw new IllegalArgumentException("timeSignature should be in format 'beats/beat-type' e.g. 6/8");
        }

        String[] tempoTokens = timeSignature.split("/");

        String beats = tempoTokens[0];
        String beatsType = tempoTokens[1];
        BigDecimal divisions = new BigDecimal(beatsType).divide(new BigDecimal("4"));

        part.getMeasure().add(PartHelper.createLeadingMeasure(String.valueOf(1), KeyEnum.fromValue(key), beats, beatsType, divisions));

        addMeasuresToPart(measures, part, beats, beatsType, divisions);

        DefaultScore score = new DefaultScore(songName, part);

        JAXBContext jaxbContext = JAXBContext.newInstance(ScorePartwise.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(score, new File("D:\\William\\Desktop\\" + songName + ".musicxml"));
    }

    private static void addMeasuresToPart(List<String> measures, ScorePartwise.Part part, String beats, String beatsType, BigDecimal divisions) {
        boolean rehearse = false;
        int measureNumber = 2;
        int markChar = 65;
        ScorePartwise.Part.Measure measure = null;
        for (String measureString : measures) {
            if (measureString.isBlank()) {
                if (rehearse) {
                    continue;
                }
                measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
                measure.getNoteOrBackupOrForward().add(PartHelper.createRehearsalMark(getLetter(markChar++)));
                rehearse = true;
            } else {
                if (!rehearse) {
                    measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
                }
                rehearse = false;

                String[] chords = measureString.replaceAll("\\/{2,}", "/").split(" ");
                Integer noteDuration = Integer.valueOf(beats) / chords.length;

                for (int i = 0; i < chords.length; i++) {
                    if ("-/".contains(chords[i])) {
                        addNoteToMeasure(measure, noteDuration);
                    } else {
                        addHarmonyToMeasure(measure, noteDuration, chords[i]);
                    }
                }
                part.getMeasure().add(measure);
            }
        }
    }

    private static void addNoteToMeasure(ScorePartwise.Part.Measure measure, Integer noteDuration) {
        measure.getNoteOrBackupOrForward().add(PartHelper.createNote(new BigDecimal(noteDuration.toString())));
    }

    private static void addHarmonyToMeasure(ScorePartwise.Part.Measure measure, Integer noteDuration, String chord) {
        Harmony harmony = PartHelper.createHarmony(chord.substring(0, 1), chord.contains(MINOR) ? KindValue.MINOR : KindValue.MAJOR, getAlter(chord));
        measure.getNoteOrBackupOrForward().add(harmony);
        measure.getNoteOrBackupOrForward().add(PartHelper.createNote(new BigDecimal(noteDuration.toString())));
    }

    private static String getAlter(String chord) {
        if (chord.contains(SHARP)) {
            return SHARP;
        }
        if (chord.contains(FLAT)) {
            return FLAT;
        }

        return "";
    }

    private static String getLetter(int markChar) {
        return String.valueOf((char) markChar);
    }
}
