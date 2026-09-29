package com.example.solar_tracker.Repository;

import com.example.solar_tracker.Model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {
    Optional<ConsumptionLog> findByHouseholdHouseholdIdAndConsumptionDate(Long householdId, LocalDate consumptionDate);
    boolean existsByHouseholdHouseholdIdAndConsumptionDate(Long householdId, LocalDate consumptionDate);
    List<ConsumptionLog> findByHouseholdHouseholdIdAndConsumptionDateBetween(Long householdId, LocalDate start, LocalDate end);
}
