package com.example.solar_tracker.Repository;

import com.example.solar_tracker.Model.GenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface GenerationLogRepository extends JpaRepository<GenerationLog, Long> {
    Optional<GenerationLog> findByInstallationInstallationIdAndGenerationDate(Long installationId, LocalDate generationDate);
    boolean existsByInstallationInstallationIdAndGenerationDate(Long installationId, LocalDate generationDate);
}
