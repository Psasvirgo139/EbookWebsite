package com.mycompany.ebookwebsite.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import jakarta.servlet.http.Part;
import java.util.List;

public class FileTextExtractor {

    public static String extractTextFromFile(Part filePart) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        try (InputStream is = filePart.getInputStream()) {
            if (fileName.endsWith(".pdf")) {
                return extractTextFromPDF(is);
            } else if (fileName.endsWith(".docx")) {
                return extractTextFromDOCX(is);
            }
        }
        return "";
    }

    public static String extractText(String filePath) throws IOException {
        String fileName = Paths.get(filePath).getFileName().toString();
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            if (fileName.endsWith(".pdf")) {
                return extractTextFromPDF(is);
            } else if (fileName.endsWith(".docx")) {
                return extractTextFromDOCX(is);
            }
        }
        return "";
    }

    public static String extractText(Part filePart) throws IOException {
        return extractTextFromFile(filePart);
    }

    private static String extractTextFromPDF(InputStream is) throws IOException {
        try (PDDocument document = PDDocument.load(is)) {
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        }
        return "";
    }

    private static String extractTextFromDOCX(InputStream is) throws IOException {
        try (XWPFDocument document = new XWPFDocument(is)) {
            StringBuilder text = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                text.append(para.getText()).append("\n");
            }
            return text.toString();
        }
    }
} 