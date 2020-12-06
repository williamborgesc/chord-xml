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

import static com.williamborgesc.chordxml.score.Constants.RITORNELLO_END;
import static com.williamborgesc.chordxml.score.Constants.RITORNELLO_START;
import static com.williamborgesc.chordxml.score.Constants.SONG_SECTION;

public class ChordSheetParser {

    public static final String UTF_8 = "utf-8";
    public static final String NEW_LINE = "\r\n";
    public static final String DOUBLE_NEW_LINE = "\r\n\r\n";

    public static List<String> parseFileToMeasures(String source) {
        List<String> sourceLines = null;
        source = SONG_SECTION.concat(source.replaceAll(DOUBLE_NEW_LINE, NEW_LINE + SONG_SECTION + " "));

        sourceLines = Arrays.asList(source.split(NEW_LINE));

        List<String> measures = new ArrayList<>();

        sourceLines.stream()
                .filter(line -> line.startsWith("|") || line.startsWith(SONG_SECTION))
                .map(line -> line.replace(":||:", "| " + RITORNELLO_END + " |" + RITORNELLO_START + " |"))
                .map(line -> line.replace("||:", "| " + RITORNELLO_START + " |"))
                .map(line -> line.replace(":||", "| " + RITORNELLO_END + " |"))
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
