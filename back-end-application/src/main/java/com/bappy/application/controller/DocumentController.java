package com.bappy.application.controller;

import com.bappy.application.domain.Document;
import com.bappy.application.dto.UploadResponse;
import com.bappy.application.repository.DocumentRepository;
import com.bappy.application.service.DocumentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadDocument(@RequestPart("file") @NotNull MultipartFile file,
                                                         @AuthenticationPrincipal Jwt jwt) throws IOException {
        UUID uploaderId = UUID.fromString(jwt.getSubject());
        UUID tenantId = null;
        if (jwt.containsClaim("tenant_id")) {
            try { tenantId = UUID.fromString(jwt.getClaimAsString("tenant_id")); } catch (Exception ignored) {}
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        byte[] content = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        Document saved = documentService.storeUploadedFile(content, originalFilename, contentType, uploaderId, tenantId);

        UploadResponse resp = UploadResponse.builder().id(saved.getId()).status(saved.getStatus()).build();
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        Optional<Document> doc = documentRepository.findById(id);
        if (doc.isEmpty()) return ResponseEntity.notFound().build();

        // simple RBAC: allow uploader, same tenant, or role ADMIN
        Document d = doc.get();
        String tenantClaim = jwt.getClaimAsString("tenant_id");
        boolean isAdmin = jwt.getClaimAsString("roles") != null && jwt.getClaimAsString("roles").contains("ADMIN");
        if (!isAdmin) {
            if (d.getUploaderId() == null || !d.getUploaderId().toString().equals(jwt.getSubject())) {
                if (tenantClaim == null || !tenantClaim.equals(d.getTenantId() == null ? null : d.getTenantId().toString()))
                    return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.ok(d);
    }

    @GetMapping
    public ResponseEntity<List<Document>> listDocuments(@RequestParam(value = "status", required = false) String status) {
        if (status == null) return ResponseEntity.ok(documentRepository.findAll());
        return ResponseEntity.ok(documentRepository.findAll().stream().filter(d -> status.equals(d.getStatus())).toList());
    }
}
