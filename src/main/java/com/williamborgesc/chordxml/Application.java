package com.williamborgesc.chordxml;

import com.williamborgesc.chordxml.parser.ChordsParser;
import com.williamborgesc.chordxml.score.Constants;
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

import static com.williamborgesc.chordxml.score.Constants.RITORNELLO_END;
import static com.williamborgesc.chordxml.score.Constants.RITORNELLO_START;

public class Application {

    public static String key = "B";
    public static String timeSignature = "4/4";
    public static String songName = "Test";

    // TODO Tetrades
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
        boolean keepMeasure = false;
        int measureNumber = 2;
        int markChar = 65;
        ScorePartwise.Part.Measure measure = null;
        for (String measureString : measures) {
            if (measureString.startsWith(Constants.SONG_SECTION)) {
                measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
                measure.getNoteOrBackupOrForward().add(PartHelper.createRehearsalMark(getLetter(markChar++)));
                keepMeasure = true;
            } else {
                if (!keepMeasure) {
                    measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
                }
                keepMeasure = false;

                if (measureString.equals(RITORNELLO_START)) {
                    addRepeatStartToMeasure(measure);
                    keepMeasure = true;
                    continue;
                }
                if (measureString.equals(RITORNELLO_END)) {
                    addRepeatEndToMeasure(measure);
                    keepMeasure = true;
                    continue;
                }
                String[] chords = measureString.replaceAll("\\/{2,}", "/").split(" ");
                Integer noteDuration = Integer.valueOf(beats) / chords.length;

                if (measureString.contains("%")) {
                    addNoteToMeasure(measure, Integer.valueOf(beats));
                } else {
                    addChordsToMeasure(measure, chords, noteDuration);
                }
                part.getMeasure().add(measure);
            }
        }
    }

    private static void addRepeatStartToMeasure(ScorePartwise.Part.Measure measure) {
        measure.getNoteOrBackupOrForward().add(PartHelper.createStartRepeat());
    }

    private static void addRepeatEndToMeasure(ScorePartwise.Part.Measure measure) {
        measure.getNoteOrBackupOrForward().add(PartHelper.createEndRepeat());
    }

    private static void addChordsToMeasure(ScorePartwise.Part.Measure measure, String[] chords, Integer noteDuration) {
        for (int i = 0; i < chords.length; i++) {
            if ("-/".contains(chords[i])) {
                addNoteToMeasure(measure, noteDuration);
            } else {
                addHarmonyToMeasure(measure, noteDuration, chords[i]);
            }
        }
    }

    private static void addNoteToMeasure(ScorePartwise.Part.Measure measure, Integer noteDuration) {
        measure.getNoteOrBackupOrForward().add(PartHelper.createNote(new BigDecimal(noteDuration.toString())));
    }

    private static void addHarmonyToMeasure(ScorePartwise.Part.Measure measure, Integer noteDuration, String chord) {
        Harmony harmony = PartHelper.createHarmony(getRootNote(chord), getChordKind(chord), getAlter(chord), getBass(chord), getBassAlter(chord));
        measure.getNoteOrBackupOrForward().add(harmony);
        measure.getNoteOrBackupOrForward().add(PartHelper.createNote(new BigDecimal(noteDuration.toString())));
    }

    private static String getRootNote(String chord) {
        return chord.substring(0, 1);
    }

    private static String getBassAlter(String chord) {
        if (!chord.contains("/")) {
            return null;
        }
        return getAlter(chord.split("/")[1]);
    }

    private static String getBass(String chord) {
        if (!chord.contains("/")) {
            return null;
        }
        return getRootNote(chord.split("/")[1]);
    }

    private static KindValue getChordKind(String chord) {
        return chord.contains(Constants.MINOR) ? KindValue.MINOR : KindValue.MAJOR;
    }

    private static String getAlter(String chord) {
        if (chord.contains("/")) {
            return getAlter(chord.split("/")[0]);
        }
        if (chord.contains(Constants.SHARP)) {
            return Constants.SHARP;
        }
        if (chord.contains(Constants.FLAT)) {
            return Constants.FLAT;
        }

        return "";
    }

    private static String getLetter(int markChar) {
        return String.valueOf((char) markChar);
    }
}
