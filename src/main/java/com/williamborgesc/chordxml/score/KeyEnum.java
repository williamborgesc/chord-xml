package com.williamborgesc.chordxml.score;


import java.math.BigInteger;
import java.util.Arrays;

public enum KeyEnum {

    C("C", new BigInteger("0")),
    G("G", new BigInteger("1")),
    D("D", new BigInteger("2")),
    A("A", new BigInteger("3")),
    E("E", new BigInteger("4")),
    B("B", new BigInteger("5")),
    F_SHARP("F#", new BigInteger("6")),
    C_SHARP("C#", new BigInteger("7")),
    F("F", new BigInteger("-1")),
    Bb("Bb", new BigInteger("-2")),
    Eb("Eb", new BigInteger("-3")),
    Ab("Ab", new BigInteger("-4")),
    Db("Db", new BigInteger("-5")),
    Gb("Gb", new BigInteger("-6")),
    Cb("Cb", new BigInteger("-7"));

    private String value;
    private BigInteger fifths;

    KeyEnum(String value, BigInteger fifths) {
        this.value = value;
        this.fifths = fifths;
    }

    public BigInteger getFifths() {
        return fifths;
    }

    public static KeyEnum fromValue(String value) {

        return Arrays.stream(KeyEnum.values())
                .filter(keyEnum -> keyEnum.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value for a KeyEnum"));

    }
}
