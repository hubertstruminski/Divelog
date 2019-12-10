package com.example.controller;

import com.itextpdf.text.pdf.BaseFont;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;


@Controller
public class PDFController {

    private static final String FILE_NAME = "itext.pdf";

    @GetMapping(value = "/generate/pdf", produces = "application/json")
    public ResponseEntity<byte[]> displayProcessFile() throws DocumentException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "attachment;filename=" + "itext.pdf");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(writeUsingIText(), headers, HttpStatus.OK);
        return response;
    }

    private static byte[] writeUsingIText() throws DocumentException, IOException {

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        BaseFont bf = BaseFont.createFont("Arial.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);
        document.add(new Paragraph("Imię partnera ąęśćżźńłó", font));

        //open

        Paragraph p = new Paragraph();
        p.add("This is my paragraph 1");
        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        Font f = new Font();
        f.setStyle(Font.BOLD);
        f.setSize(8);

        document.add(new Paragraph("This is my paragraph 3", f));

            //close
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        return pdfBytes;
    }
}
