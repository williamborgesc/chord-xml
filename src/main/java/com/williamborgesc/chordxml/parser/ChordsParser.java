package com.williamborgesc.chordxml.parser;

import com.williamborgesc.chordxml.score.Constants;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChordsParser {

    public static final String UTF_8 = "utf-8";
    public static final String NEW_LINE = "\r\n";
    public static final String DOUBLE_NEW_LINE = "\r\n\r\n";

    public static List<String> parseFileToMeasures(File chordsSource) {
        List<String> sourceLines = null;
        try {
            String source = FileUtils.readFileToString(chordsSource, UTF_8)
                    .replaceAll(DOUBLE_NEW_LINE, NEW_LINE + Constants.SONG_SECTION + " ");

            sourceLines = Arrays.asList(source.split(NEW_LINE));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read chords file", e);
        }

        List<String> measures = new ArrayList<>();

        sourceLines.stream()
                .filter(line -> line.startsWith("|") || line.startsWith(Constants.SONG_SECTION))
                .map(line -> line.replace(":||:", "| " + Constants.RITORNELLO_START + " |" + Constants.RITORNELLO_END + " |"))
                .map(line -> line.replace("||:", "| " + Constants.RITORNELLO_START + " |"))
                .map(line -> line.replace(":||", "| " + Constants.RITORNELLO_END + " |"))
                .forEach(line -> measures.addAll(extractMeasures(line)));

        return measures;
    }

    private static List<String> extractMeasures(String line) {
        return Arrays.stream(line.split("\\|"))
                .map(StringUtils::trimLeadingWhitespace)
                .map(StringUtils::trimTrailingWhitespace)
                .filter(string -> !string.isEmpty())
                .collect(Collectors.toList());
    }
}
