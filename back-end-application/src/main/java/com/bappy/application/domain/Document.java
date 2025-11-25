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
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "status")
    private String status; // UPLOADED, PROCESSING, READY, ERROR

    private Integer pages;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
