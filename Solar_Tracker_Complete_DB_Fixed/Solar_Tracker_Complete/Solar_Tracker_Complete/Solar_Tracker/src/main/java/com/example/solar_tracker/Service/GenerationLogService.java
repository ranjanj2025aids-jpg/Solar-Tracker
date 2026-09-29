package com.example.solar_tracker.Service;

import com.example.solar_tracker.Model.GenerationLog;
import com.example.solar_tracker.Model.Installation;
import com.example.solar_tracker.Repository.GenerationLogRepository;
import com.example.solar_tracker.Repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GenerationLogService {

    private final GenerationLogRepository repository;
    private final InstallationRepository installationRepository;

    public GenerationLogService(GenerationLogRepository repository, InstallationRepository installationRepository) {
        this.repository = repository;
        this.installationRepository = installationRepository;
    }

    public List<GenerationLog> getAll() {
        return repository.findAll();
    }

    public GenerationLog getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Generation log not found: " + id));
    }

    @Transactional
    public GenerationLog create(GenerationLog item, Long installationId) {
        validate(item);
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new IllegalArgumentException("Installation not found: " + installationId));

        if (repository.existsByInstallationInstallationIdAndGenerationDate(installationId, item.getGenerationDate())) {
            throw new IllegalArgumentException("Generation log already exists for this installation and date");
        }

        item.setInstallation(installation);
        return repository.save(item);
    }

    @Transactional
    public GenerationLog update(Long id, GenerationLog item) {
        validate(item);
        GenerationLog existing = getById(id);
        if (!existing.getGenerationDate().equals(item.getGenerationDate()) &&
                repository.existsByInstallationInstallationIdAndGenerationDate(
                        existing.getInstallation().getInstallationId(), item.getGenerationDate())) {
            throw new IllegalArgumentException("Generation log already exists for this installation and date");
        }
        existing.setGenerationDate(item.getGenerationDate());
        existing.setUnitsGenerated(item.getUnitsGenerated());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Generation log not found: " + id);
        repository.deleteById(id);
    }

    private void validate(GenerationLog item) {
        if (item == null || item.getGenerationDate() == null) throw new IllegalArgumentException("Generation date is required");
        if (item.getUnitsGenerated() == null || item.getUnitsGenerated().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Generated units cannot be negative");
        }
    }
}
