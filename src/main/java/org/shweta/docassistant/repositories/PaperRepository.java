package org.shweta.docassistant.repositories;

import org.shweta.docassistant.models.PaperInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaperRepository extends JpaRepository<PaperInfo, Integer> {

    Optional<PaperInfo> findByFilePathContaining(String filePath);

    List<PaperInfo> findAllByTitleOrderByUploadDateDesc(String title);

    // This allows you to find the record by the original filename (e.g., "sample.pdf")
    Optional<PaperInfo> findByTitle(String title);

    // Keep your existing one if you still need to search by the UUID path
    PaperInfo findByFilePath(String filePath);
}