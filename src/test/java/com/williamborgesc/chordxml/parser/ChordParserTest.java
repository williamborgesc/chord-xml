package com.williamborgesc.chordxml.parser;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChordParserTest {

    @Test
    public void getNoteAlter_shouldReturnSharp() {
        //given
        String chord = "C#";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }

    @Test
    public void getNoteAlter_shouldReturnFlat() {
        //given
        String chord = "Bb";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("b", result);
    }

    @Test
    public void getNoteAlter_shouldReturnFlatFromNoteWhenInversion() {
        //given
        String chord = "Ab/C#";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("b", result);
    }

    @Test
    public void getNoteAlter_shouldReturnSharpFromNoteWhenInversion() {
        //given
        String chord = "G#/Ab";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }

    @Test
    public void getNoteAlter_shouldReturnEmptyWhenNoteIsNatural() {
        //given
        String chord = "C";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("", result);
    }

    @Test
    public void getNoteAlter_shouldReturnEmptyWhenNoteIsNaturalWhenInversion() {
        //given
        String chord = "E/G#";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("", result);
    }

    @Test
    public void getNoteAlter_shouldParseTetrad() {
        //given
        String chord = "G#7b5";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }

    @Test
    public void getNoteAlter_shouldParseInvertedTetrad() {
        //given
        String chord = "G#7b5/Ab";

        //when
        String result = ChordParser.getNoteAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }


    @Test
    public void getBassAlter_shouldReturnSharp() {
        //given
        String chord = "E/G#";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }

    @Test
    public void getBassAlter_shouldReturnSharpIgnoringRoot() {
        //given
        String chord = "Eb/G#";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("#", result);
    }

    @Test
    public void getBassAlter_shouldReturnFlat() {
        //given
        String chord = "Eb/Gb";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("b", result);
    }

    @Test
    public void getBassAlter_shouldReturnFlatIgnoringRoot() {
        //given
        String chord = "C#/Bb";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("b", result);
    }

    @Test
    public void getBassAlter_shouldReturnEmptyWhenBassIsNatural() {
        //given
        String chord = "A#/B";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("", result);
    }

    @Test
    public void getBassAlter_shouldReturnEmptyWhenNotInversion() {
        //given
        String chord = "B";

        //when
        String result = ChordParser.getBassAlter(chord);

        //then
        Assertions.assertEquals("", result);
    }

    @Test
    public void getRootNote_shouldReturnFirstChar() {
        //given
        String chord = "G#7b5/Ab";

        //when
        String result = ChordParser.getRootNote(chord);

        //then
        Assertions.assertEquals("G", result);
    }


    @Test
    public void getBass_shouldReturnBassNote() {
        //then
        Assertions.assertEquals("A", ChordParser.getBass("G#/Ab"));
        Assertions.assertEquals("F", ChordParser.getBass("Eb/F"));
    }

    @Test
    public void getBass_shouldParseInvertedTetrad() {
        //given
        String chord = "G#7b5/Ab";

        //when
        String result = ChordParser.getBass(chord);

        //then
        Assertions.assertEquals("A", result);
    }

    @Test
    public void getBass_shouldParseAdds() {
        //given
        String chord = "G#6/9/Ab";

        //when
        String result = ChordParser.getBass(chord);

        //then
        Assertions.assertEquals("A", result);
    }
}