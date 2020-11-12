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

    public static String key = "D";
    public static String timeSignature = "6/8";
    public static String songName = "Test";


    public static void main(String[] args) throws JAXBException {
        List<String> measures = ChordsParser.parseFileToMeasures(new File("D:\\Dev\\eclipse-wp\\chord-xml\\ever-be.txt"));

        int measureNumber = 1;
        int markChar = 65;
        ScorePartwise.Part part = new ScorePartwise.Part();

        String[] tempoTokens = timeSignature.split("/");

        if (!timeSignature.contains("/")) {
            throw new IllegalArgumentException("timeSignature should be in format 'beats/beat-type' e.g. 6/8");
        }
        String beats = tempoTokens[0];
        String beatsType = tempoTokens[1];
        BigDecimal divisions = new BigDecimal(beatsType).divide(new BigDecimal("4"));
        part.getMeasure().add(PartHelper.createLeadingMeasure(String.valueOf(measureNumber++), KeyEnum.fromValue(key), beats, beatsType));
        boolean rehearse = false;

        ScorePartwise.Part.Measure measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
        for (String measureString : measures) {

            if (measureString.isBlank()) {
                if (rehearse) {
                    continue;
                }
                measure.getNoteOrBackupOrForward().add(PartHelper.createRehearsalMark(getLetter(markChar++)));
                rehearse = true;
                continue;
            } else {
                measure = PartHelper.createMeasure(String.valueOf(measureNumber++), divisions);
            }
            rehearse = false;

            String[] chords = measureString.split(" ");

            Integer noteDuration = Integer.valueOf(beats) / chords.length;

            for (String chord : chords) {
                Harmony harmony = PartHelper.createHarmony(chord.substring(0, 1), chord.contains("m") ? KindValue.MINOR : KindValue.MAJOR);

                measure.getNoteOrBackupOrForward().add(harmony);
                measure.getNoteOrBackupOrForward().add(PartHelper.createNote(new BigDecimal(noteDuration.toString())));
            }
            part.getMeasure().add(measure);
        }

        DefaultScore score = new DefaultScore(songName, part);

        JAXBContext jaxbContext = JAXBContext.newInstance(ScorePartwise.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(score, new File("D:\\William\\Desktop\\" + songName + ".musicxml"));
    }

    private static String getLetter(int markChar) {
        return String.valueOf((char) markChar);
    }
}
