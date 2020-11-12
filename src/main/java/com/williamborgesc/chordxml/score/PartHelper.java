package com.williamborgesc.chordxml.score;

import generated.AboveBelow;
import generated.Attributes;
import generated.Clef;
import generated.ClefSign;
import generated.Direction;
import generated.DirectionType;
import generated.FormattedText;
import generated.FormattedTextId;
import generated.Harmony;
import generated.Key;
import generated.Kind;
import generated.KindValue;
import generated.Note;
import generated.Rest;
import generated.Root;
import generated.RootStep;
import generated.ScorePartwise;
import generated.Step;
import generated.Time;
import generated.YesNo;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;

public class PartHelper {

    public static final BigDecimal DEFAULT_MEASURE_WIDTH = new BigDecimal("240");

    public static ScorePartwise.Part.Measure createLeadingMeasure(String number, KeyEnum key, String beats, String beatsType) {

        ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
        measure.setWidth(DEFAULT_MEASURE_WIDTH);
        measure.setNumber(number);
        measure.getNoteOrBackupOrForward().add(getLeadingAttributes(key, beats, beatsType));
        measure.getNoteOrBackupOrForward().add(getNote(beatsType));

        return measure;
    }

    public static ScorePartwise.Part.Measure createMeasure(String number, BigDecimal divisions) {

        ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
        measure.setWidth(DEFAULT_MEASURE_WIDTH);
        measure.setNumber(number);
        measure.getNoteOrBackupOrForward().add(getAttributes(divisions));

        return measure;
    }

    @Deprecated
    public static ScorePartwise.Part.Measure getMeasure() {

        ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
        measure.setWidth(new BigDecimal("240"));
        measure.setNumber("1");

        return measure;
    }

    public static Harmony createHarmony(String rootNote, KindValue kindValue) {
        Kind kind = new Kind();
        kind.setValue(kindValue);

        Harmony harmony = new Harmony();
        harmony.getHarmonyChord().add(getRoot(Step.fromValue(rootNote)));
        harmony.getHarmonyChord().add(kind);
        harmony.setPrintFrame(YesNo.NO);

        return harmony;
    }

    public static Note createNote(BigDecimal duration) {
        Rest rest = new Rest();
        rest.setMeasure(YesNo.YES);

        Note note = new Note();
        note.getContent().add(new JAXBElement(new QName("duration"), BigDecimal.class, Note.class, duration));
        note.getContent().add(new JAXBElement(new QName("voice"), String.class, Note.class, "1"));
        note.getContent().add(new JAXBElement(new QName("rest"), Rest.class, Note.class, rest));

        return note;
    }


    public static Direction createRehearsalMark(String text) {

        FormattedTextId formattedTextId = new FormattedTextId();
        formattedTextId.setValue(text);

        DirectionType directionType = new DirectionType();

        directionType.getRehearsal().add(formattedTextId);

        Direction direction = new Direction();
        direction.setPlacement(AboveBelow.ABOVE);
        direction.getDirectionType().add(directionType);

        return direction;
    }

    private static Note getNote(String beatsType) {
        Note note = new Note();

        Rest rest = new Rest();
        rest.setMeasure(YesNo.YES);

        note.getContent().add(new JAXBElement(new QName("rest"), Rest.class, Note.class, rest));
        note.getContent().add(new JAXBElement(new QName("duration"), BigDecimal.class, Note.class, new BigDecimal(beatsType)));
        note.getContent().add(new JAXBElement(new QName("voice"), String.class, Note.class, "1"));

        return note;
    }

    private static Object getRoot(Step step) {
        Root root = new Root();
        RootStep rootStep = new RootStep();
        rootStep.setValue(step);
        root.setRootStep(rootStep);

        return root;
    }

    private static Attributes getAttributes(BigDecimal divisions) {
        Attributes attributes = new Attributes();
        attributes.setDivisions(divisions);

        return attributes;
    }

    private static Attributes getLeadingAttributes(KeyEnum key, String beats, String beatsType) {
        Attributes attributes = new Attributes();
        attributes.setDivisions(BigDecimal.ONE);
        attributes.getKey().add(getKey(key));
        attributes.getTime().add(getTime(beats, beatsType));
        attributes.getClef().add(getClef());

        return attributes;
    }

    private static Clef getClef() {
        Clef clef = new Clef();
        clef.setSign(ClefSign.G);
        clef.setLine(BigInteger.TWO);

        return clef;
    }

    private static Time getTime(String beats, String beatsType) {
        Time time = new Time();
        time.getTimeSignature().add(new JAXBElement(new QName("beats"), String.class, Time.class, beats));
        time.getTimeSignature().add(new JAXBElement(new QName("beat-type"), String.class, Time.class, beatsType));

        return time;
    }

    private static Key getKey(KeyEnum keyEnum) {
        Key key = new Key();
        key.setFifths(keyEnum.getFifths());
        return key;
    }
}
