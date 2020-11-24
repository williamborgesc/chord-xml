package com.williamborgesc.chordxml;

import generated.Kind;
import generated.KindValue;
import generated.ScorePartwise;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlEnumValue;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws JAXBException, IOException {

//        readPart();
        String chord = "B6";


    }

    private static void readPart() throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ScorePartwise.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(FileUtils.readFileToString(new File("D:\\William\\Desktop\\Great_Are_You_Lord.musicxml"), "utf-8"));
        ScorePartwise partwise = (ScorePartwise) unmarshaller.unmarshal(reader);

        System.out.println(partwise);
    }


}
