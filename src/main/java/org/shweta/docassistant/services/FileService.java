package org.shweta.docassistant.services;

import lombok.RequiredArgsConstructor;
import org.shweta.docassistant.dto.PaperInfoDTO;
import org.shweta.docassistant.models.PaperInfo;
import org.shweta.docassistant.repositories.PaperRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileService {

    private final PaperRepository paperRepository;
    private final DocumentProcessingService documentProcessingService;

    private static final String UPLOAD_DIR = "uploads/";

    @Async
    public CompletableFuture<String> uploadFile(MultipartFile multipartFile) {

        try {

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = multipartFile.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));

            String uniqueName = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(uniqueName);

            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            PaperInfo paperInfo = PaperInfo.builder()
                    .title(originalName)            // Store "sample.pdf" here
                    .filePath(filePath.toString())  // Store "uploads/3f97be5f..." here
                    .uploadDate(new Date())
                    .fileSize(multipartFile.getSize())
                    .build();

            PaperInfo savedPaper = paperRepository.save(paperInfo);
            documentProcessingService.processPdf(Long.valueOf(savedPaper.getId()), savedPaper.getFileName());

            return CompletableFuture.completedFuture(filePath.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("File upload failed");
        }
    }

    public byte[] downloadFile(String uuidFilename) {
        // 1. Search the database for the record that contains this specific UUID in its path
        PaperInfo paper = paperRepository.findByFilePathContaining(uuidFilename)
                .orElseThrow(() -> new RuntimeException("File record not found in database for: " + uuidFilename));

        // 2. Get the actual path from the database record
        Path path = Paths.get(paper.getFilePath());

        try {
            if (!Files.exists(path)) {
                throw new RuntimeException("Physical file not found at: " + path);
            }
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    public List<PaperInfoDTO> getAllUploadedFiles() {

        return paperRepository.findAll().stream()
                .map(paper -> PaperInfoDTO.builder()
                        .fileName(paper.getTitle())
                        .fileUrl(paper.getFilePath())
                        .uploadDate(paper.getUploadDate())
                        .fileSize(paper.getFileSize())
                        .build())
                .toList();
    }
}