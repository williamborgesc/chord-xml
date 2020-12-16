package com.williamborgesc.chordxml.score;

import generated.AboveBelow;
import generated.Attributes;
import generated.BackwardForward;
import generated.Barline;
import generated.Bass;
import generated.BassAlter;
import generated.BassStep;
import generated.Clef;
import generated.ClefSign;
import generated.Direction;
import generated.DirectionType;
import generated.FormattedTextId;
import generated.Harmony;
import generated.Key;
import generated.Kind;
import generated.KindValue;
import generated.Metronome;
import generated.Note;
import generated.PerMinute;
import generated.Print;
import generated.Repeat;
import generated.Rest;
import generated.Root;
import generated.RootAlter;
import generated.RootStep;
import generated.ScorePartwise;
import generated.Step;
import generated.Time;
import generated.YesNo;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.williamborgesc.chordxml.score.BeatUnit.fromBeatType;

public class PartHelper {

    public static final BigDecimal DEFAULT_MEASURE_WIDTH = new BigDecimal("240");


    public static ScorePartwise.Part.Measure createLeadingMeasure(String number, KeyEnum key, String beats, String beatsType, BigDecimal divisions) {

        ScorePartwise.Part.Measure measure = new ScorePartwise.Part.Measure();
        measure.setWidth(DEFAULT_MEASURE_WIDTH);
        measure.setNumber(number);
        measure.getNoteOrBackupOrForward().add(getLeadingAttributes(key, beats, beatsType, divisions));
        measure.getNoteOrBackupOrForward().add(getNote(beats));
        measure.getNoteOrBackupOrForward().add(createTempo(beatsType));

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

    public static Harmony createHarmony(String rootNote, KindValue kindValue, String alter) {
        return createHarmony(rootNote, kindValue, alter, null, null);
    }

    public static Harmony createHarmony(String rootNote, KindValue kindValue, String alter, String bassNote, String bassAlter) {
        Kind kind = new Kind();
        kind.setValue(kindValue);

        Harmony harmony = new Harmony();
        harmony.getHarmonyChord().add(getRoot(rootNote, alter));
        harmony.getHarmonyChord().add(kind);
        harmony.getHarmonyChord().add(getBass(bassNote, bassAlter));
        harmony.setPrintFrame(YesNo.NO);

        return harmony;
    }

    private static Bass getBass(String note, String alter) {
        if (StringUtils.isEmpty(note)) {
            return null;
        }
        Bass bass = new Bass();

        BassStep bassStep = new BassStep();
        bassStep.setValue(Step.fromValue(note));

        if (!alter.isEmpty()) {
            BassAlter bassAlter = new BassAlter();
            bassAlter.setValue(alter.contains("#") ? BigDecimal.ONE : new BigDecimal("-1"));
            bass.setBassAlter(bassAlter);
        }

        bass.setBassStep(bassStep);
        return bass;
    }

    public static Note createNote(BigDecimal duration) {
        Rest rest = new Rest();
        rest.setMeasure(YesNo.YES);

        Note note = new Note();
        note.getContent().add(new JAXBElement(new QName("rest"), Rest.class, Note.class, rest));
        note.getContent().add(new JAXBElement(new QName("duration"), BigDecimal.class, Note.class, duration));
        note.getContent().add(new JAXBElement(new QName("voice"), String.class, Note.class, "1"));

        return note;
    }


    public static Direction createTempo(String beatType) {
        DirectionType directionType = new DirectionType();

        Metronome metronome = new Metronome();
        PerMinute perMinute = new PerMinute();
        perMinute.setValue("100");

        metronome.getContent().add(new JAXBElement(new QName("beat-unit"), String.class, Metronome.class, fromBeatType(beatType)));
        metronome.getContent().add(new JAXBElement(new QName("per-minute"), PerMinute.class, Metronome.class, perMinute));

        metronome.setParentheses(YesNo.NO);
        directionType.setMetronome(metronome);

        Direction direction = new Direction();
        direction.setPlacement(AboveBelow.ABOVE);
        direction.getDirectionType().add(directionType);

        return direction;
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

    private static Note getNote(String duration) {
        Note note = new Note();

        Rest rest = new Rest();
        rest.setMeasure(YesNo.YES);

        note.getContent().add(new JAXBElement(new QName("rest"), Rest.class, Note.class, rest));
        note.getContent().add(new JAXBElement(new QName("duration"), BigDecimal.class, Note.class, new BigDecimal(duration)));
        note.getContent().add(new JAXBElement(new QName("voice"), String.class, Note.class, "1"));

        return note;
    }

    private static Attributes getAttributes(BigDecimal divisions) {
        Attributes attributes = new Attributes();
        attributes.setDivisions(divisions);

        return attributes;
    }

    private static Root getRoot(String step, String alter) {
        Root root = new Root();

        RootStep rootStep = new RootStep();
        rootStep.setValue(Step.fromValue(step));

        if (!alter.isEmpty()) {
            RootAlter rootAlter = new RootAlter();
            rootAlter.setValue(alter.contains("#") ? BigDecimal.ONE : new BigDecimal("-1"));
            root.setRootAlter(rootAlter);
        }

        root.setRootStep(rootStep);
        return root;
    }

    private static Attributes getLeadingAttributes(KeyEnum key, String beats, String beatsType, BigDecimal divisions) {
        Attributes attributes = new Attributes();
        attributes.setDivisions(divisions);
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

    public static Barline createStartRepeat() {
        return createRepeat(BackwardForward.FORWARD);
    }

    public static Barline createEndRepeat() {
        return createRepeat(BackwardForward.BACKWARD);
    }

    public static Barline createRepeat(BackwardForward direction) {
        Repeat repeat = new Repeat();
        repeat.setDirection(direction);

        Barline barline = new Barline();
        barline.setRepeat(repeat);

        return barline;
    }

    public static Print createLineBreak() {
        Print print = new Print();
        print.setNewSystem(YesNo.YES);
        return print;
    }
}
