package com.williamborgesc.chordxml.domain;

import lombok.Data;

@Data
public class ChordXmlConversion {

    private String songName;
    private String key;
    private String beats;
    private String beatType;
    private String chords;

}