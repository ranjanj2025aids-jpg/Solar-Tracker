package com.example.solar_tracker.Service;

import com.example.solar_tracker.Model.Installation;
import com.example.solar_tracker.Repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InstallationService {

    private final InstallationRepository repository;

    public InstallationService(InstallationRepository repository) {
        this.repository = repository;
    }

    public List<Installation> getAll() {
        return repository.findAll();
    }

    public Installation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Installation not found: " + id));
    }

    @Transactional
    public Installation create(Installation installation) {
        validate(installation);
        return repository.save(installation);
    }

    @Transactional
    public Installation update(Long id, Installation installation) {
        validate(installation);
        Installation existing = getById(id);
        existing.setInstallationName(installation.getInstallationName());
        existing.setLocation(installation.getLocation());
        existing.setCapacityKw(installation.getCapacityKw());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Installation not found: " + id);
        }
        repository.deleteById(id);
    }

    private void validate(Installation installation) {
        if (installation == null) throw new IllegalArgumentException("Installation data is required");
        if (isBlank(installation.getInstallationName())) throw new IllegalArgumentException("Installation name is required");
        if (isBlank(installation.getLocation())) throw new IllegalArgumentException("Installation location is required");
        BigDecimal capacity = installation.getCapacityKw();
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0 kW");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
