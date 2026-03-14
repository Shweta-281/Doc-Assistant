package org.shweta.docassistant.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;



@Service
@Slf4j
public class PdfService {

    public String extractText(String fileName) {
        // This ensures it looks in the 'uploads' folder using the UUID filename
        File file = new File("uploads", fileName);

        log.info("Attempting to load: {}", file.getAbsolutePath());

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            log.error("Extraction failed for file: {}", fileName);
            throw new RuntimeException("File not found or unreadable: " + fileName);
        }
    }
}