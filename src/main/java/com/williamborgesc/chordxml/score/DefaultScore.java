package com.williamborgesc.chordxml.score;

import generated.PartList;
import generated.PartName;
import generated.ScoreInstrument;
import generated.ScorePart;
import generated.ScorePartwise;
import generated.Work;

public class DefaultScore extends ScorePartwise {

    public DefaultScore(String songName, ScorePartwise.Part part) {
        Work work = new Work();
        work.setWorkTitle(songName);

        PartList partList = new PartList();

        ScorePart scorePart = new ScorePart();
        scorePart.setId("P1");

        PartName partName = new PartName();
        partName.setValue("Piano");

        ScoreInstrument scoreInstrument = new ScoreInstrument();
        scoreInstrument.setId("P1-I1");
        scoreInstrument.setInstrumentName("Piano");

        partList.setScorePart(scorePart);

        part.setId(scorePart);
        this.setWork(work);
        this.setIdentification(identification);
        this.setPartList(partList);
        this.getPart().add(part);
    }
}
