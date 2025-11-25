package com.bappy.application.repository;

import com.bappy.application.domain.DocChunk;
import com.bappy.application.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocChunkRepository extends JpaRepository<DocChunk, UUID> {
    List<DocChunk> findByDocument(Document document);
    List<DocChunk> findByDocumentId(UUID documentId);
}
