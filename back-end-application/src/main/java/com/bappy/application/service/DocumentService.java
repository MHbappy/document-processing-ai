package com.bappy.application.service;

import com.bappy.application.domain.Document;
import com.bappy.application.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final MinioService minioService;
    private final ProcessingPublisher processingPublisher;

    @Transactional
    public Document storeUploadedFile(byte[] content, String originalFilename, String contentType, UUID uploaderId, UUID tenantId) {
        // generate key
        String key = String.format("documents/%s/%s", tenantId == null ? "public" : tenantId.toString(), UUID.randomUUID() + "_" + originalFilename);
        String s3path = minioService.upload(key, content, contentType);

        Document doc = Document.builder()
                .tenantId(tenantId)
                .uploaderId(uploaderId)
                .originalFilename(originalFilename)
                .storagePath(s3path)
                .contentType(contentType)
                .status("UPLOADED")
                .uploadedAt(LocalDateTime.now())
                .build();

        Document saved = documentRepository.save(doc);

        // publish processing job
        processingPublisher.publishProcessingJob(saved.getId().toString(), s3path);

        // set status to PROCESSING and update
        saved.setStatus("PROCESSING");
        return documentRepository.save(saved);
    }
}
