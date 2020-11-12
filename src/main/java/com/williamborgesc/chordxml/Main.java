package com.williamborgesc.chordxml;

import generated.ScorePartwise;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws JAXBException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(ScorePartwise.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(FileUtils.readFileToString(new File("D:\\William\\Desktop\\Ever Be.musicxml"), "utf-8"));
        ScorePartwise partwise = (ScorePartwise) unmarshaller.unmarshal(reader);

        System.out.println(partwise);
    }

}
