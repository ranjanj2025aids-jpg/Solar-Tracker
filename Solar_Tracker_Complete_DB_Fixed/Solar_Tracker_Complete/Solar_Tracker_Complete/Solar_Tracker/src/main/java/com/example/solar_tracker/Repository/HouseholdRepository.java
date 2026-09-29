package com.example.solar_tracker.Repository;

import com.example.solar_tracker.Model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    List<Household> findByInstallationInstallationId(Long installationId);
}
