package com.example.solar_tracker.Service;

import com.example.solar_tracker.Model.Household;
import com.example.solar_tracker.Model.Installation;
import com.example.solar_tracker.Repository.HouseholdRepository;
import com.example.solar_tracker.Repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HouseholdService {

    private final HouseholdRepository repository;
    private final InstallationRepository installationRepository;

    public HouseholdService(HouseholdRepository repository, InstallationRepository installationRepository) {
        this.repository = repository;
        this.installationRepository = installationRepository;
    }

    public List<Household> getAll(Long installationId) {
        return installationId == null ? repository.findAll() : repository.findByInstallationInstallationId(installationId);
    }

    public Household getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Household not found: " + id));
    }

    @Transactional
    public Household create(Household household, Long installationId) {
        if (household == null) throw new IllegalArgumentException("Household data is required");
        if (household.getHouseholdName() == null || household.getHouseholdName().trim().isEmpty()) {
            throw new IllegalArgumentException("Household name is required");
        }
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new IllegalArgumentException("Installation not found: " + installationId));

        BigDecimal ratio = household.getAllocationRatio() == null ? BigDecimal.ONE : household.getAllocationRatio();
        if (ratio.compareTo(BigDecimal.ZERO) <= 0 || ratio.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Allocation ratio must be greater than 0 and at most 1");
        }

        BigDecimal currentTotal = repository.findByInstallationInstallationId(installationId).stream()
                .map(Household::getAllocationRatio)
                .filter(r -> r != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (currentTotal.add(ratio).compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Total household allocation for this installation cannot exceed 100%");
        }

        household.setInstallation(installation);
        household.setAllocationRatio(ratio);
        return repository.save(household);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Household not found: " + id);
        repository.deleteById(id);
    }
}
