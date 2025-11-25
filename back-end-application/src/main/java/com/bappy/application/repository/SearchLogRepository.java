package com.bappy.application.repository;

import com.bappy.application.domain.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchLogRepository extends JpaRepository<SearchLog, UUID> {
}
