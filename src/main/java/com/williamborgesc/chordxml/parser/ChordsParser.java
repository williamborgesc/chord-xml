package com.williamborgesc.chordxml.parser;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChordsParser {

    public static List<String> parseFileToMeasures(File chordsSource) {
        List<String> sourceLines = null;

        try {
            sourceLines = FileUtils.readLines(chordsSource, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read chords file", e);
        }

        List<String> measures = new ArrayList<>();

        sourceLines.stream()
                .filter(line -> line.startsWith("|") || line.isBlank())
                .map(line -> line.replace("||:", "|"))
                .map(line -> line.replace(":||", "|"))
                .forEach(line -> measures.addAll(extractMeasures(line)));

        return measures;
    }

    private static List<String> extractMeasures(String line) {
        return Arrays.stream(line.split("\\|"))
                .map(StringUtils::trimLeadingWhitespace)
                .map(StringUtils::trimTrailingWhitespace)
                .collect(Collectors.toList());
    }
}
