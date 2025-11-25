package com.bappy.application.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "search_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String query;

    @Column(name = "result_count")
    private Integer resultCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
