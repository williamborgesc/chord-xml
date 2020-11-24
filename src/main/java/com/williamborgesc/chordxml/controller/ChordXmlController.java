package com.williamborgesc.chordxml.controller;

import com.williamborgesc.chordxml.domain.ChordXmlConversion;
import com.williamborgesc.chordxml.service.ChordXmlService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;

import static java.lang.String.format;

@Controller
@AllArgsConstructor
@RequestMapping("/conversions")
public class ChordXmlController {

    private ChordXmlService service;

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ByteArrayResource> convert(ChordXmlConversion conversion) throws Exception {
        ByteArrayOutputStream result = service.convert(conversion.getSongName(), conversion.getKey(), conversion.getBeats().concat("/").concat(conversion.getBeatType()), conversion.getChords());

        ByteArrayResource resource = new ByteArrayResource(result.toByteArray());

        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", format("attachment; filename=%s.musicxml", conversion.getSongName()))
                .body(resource);
    }
}
