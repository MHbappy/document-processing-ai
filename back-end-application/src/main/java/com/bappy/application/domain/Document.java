package com.bappy.application.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID tenantId;
    private UUID uploaderId;
    private String originalFilename;
    private String storagePath; // e.g. s3://bucket/key (MinIO)
    private String contentType;
    private String status; // UPLOADED, PROCESSING, READY, ERROR
    private Integer pages;
    private LocalDateTime uploadedAt;
    private LocalDateTime processedAt;
}
