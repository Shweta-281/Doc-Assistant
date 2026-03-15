package org.shweta.docassistant.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shweta.docassistant.dto.PaperInfoDTO;
import org.shweta.docassistant.services.FileService;
import org.shweta.docassistant.services.PdfService;
import org.shweta.docassistant.services.PromptService;
import org.shweta.docassistant.services.TextChunkService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/doc_assistant")
@Slf4j
@AllArgsConstructor
public class PaperController {

    private final FileService fileService;
    private final PdfService pdfService;
    private final PromptService promptService;
    private TextChunkService textChunkService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Use .join() to get the actual String from the CompletableFuture
            String filePath = fileService.uploadFile(file).join();
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }


    @GetMapping("/files")
    public ResponseEntity<List<PaperInfoDTO>> getAllUploadedFiles() {
        log.info("Getting all uploaded files");

        List<PaperInfoDTO> files = fileService.getAllUploadedFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filePath") String fileName) {
        byte[] fileData = fileService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .header("Content-Type", "application/pdf") // Specific for PDFs
                .body(fileData);
    }

    @GetMapping("/extract")
    public ResponseEntity<?> extract(@RequestParam String fileName) {

        String text = pdfService.extractText(fileName);

        List<String> chunks = textChunkService.chunkText(text);

        return ResponseEntity.ok(chunks);
    }

}