package com.example.solar_tracker.Service;

import com.example.solar_tracker.Model.ConsumptionLog;
import com.example.solar_tracker.Model.GenerationLog;
import com.example.solar_tracker.Model.Household;
import com.example.solar_tracker.Repository.ConsumptionLogRepository;
import com.example.solar_tracker.Repository.GenerationLogRepository;
import com.example.solar_tracker.Repository.HouseholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumptionLogService {

    private final ConsumptionLogRepository repository;
    private final HouseholdRepository householdRepository;
    private final GenerationLogRepository generationLogRepository;

    public ConsumptionLogService(ConsumptionLogRepository repository,
                                 HouseholdRepository householdRepository,
                                 GenerationLogRepository generationLogRepository) {
        this.repository = repository;
        this.householdRepository = householdRepository;
        this.generationLogRepository = generationLogRepository;
    }

    public List<ConsumptionLog> getAll() {
        return repository.findAll();
    }

    public ConsumptionLog getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consumption log not found: " + id));
    }

    @Transactional
    public ConsumptionLog create(ConsumptionLog item, Long householdId) {
        validate(item);
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found: " + householdId));

        if (repository.existsByHouseholdHouseholdIdAndConsumptionDate(householdId, item.getConsumptionDate())) {
            throw new IllegalArgumentException("Consumption log already exists for this household and date");
        }

        GenerationLog generation = generationLogRepository
                .findByInstallationInstallationIdAndGenerationDate(
                        household.getInstallation().getInstallationId(), item.getConsumptionDate())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Add the generation log for the same installation and date before recording consumption"));

        BigDecimal share = generation.getUnitsGenerated()
                .multiply(household.getAllocationRatio())
                .setScale(3, RoundingMode.HALF_UP);
        BigDecimal exported = share.subtract(item.getUnitsConsumed())
                .max(BigDecimal.ZERO)
                .setScale(3, RoundingMode.HALF_UP);

        item.setHousehold(household);
        item.setGenerationShare(share);
        item.setUnitsExported(exported);
        return repository.save(item);
    }

    @Transactional
    public ConsumptionLog update(Long id, ConsumptionLog item) {
        validate(item);
        ConsumptionLog existing = getById(id);
        if (!existing.getConsumptionDate().equals(item.getConsumptionDate()) &&
                repository.existsByHouseholdHouseholdIdAndConsumptionDate(
                        existing.getHousehold().getHouseholdId(), item.getConsumptionDate())) {
            throw new IllegalArgumentException("Consumption log already exists for this household and date");
        }

        GenerationLog generation = generationLogRepository
                .findByInstallationInstallationIdAndGenerationDate(
                        existing.getHousehold().getInstallation().getInstallationId(), item.getConsumptionDate())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Add the generation log for the same installation and date before recording consumption"));

        BigDecimal share = generation.getUnitsGenerated()
                .multiply(existing.getHousehold().getAllocationRatio())
                .setScale(3, RoundingMode.HALF_UP);
        BigDecimal exported = share.subtract(item.getUnitsConsumed())
                .max(BigDecimal.ZERO)
                .setScale(3, RoundingMode.HALF_UP);

        existing.setConsumptionDate(item.getConsumptionDate());
        existing.setUnitsConsumed(item.getUnitsConsumed());
        existing.setGenerationShare(share);
        existing.setUnitsExported(exported);
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Consumption log not found: " + id);
        repository.deleteById(id);
    }

    public Map<String, Object> monthlySummary(Long householdId, int year, int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException("Month must be between 1 and 12");
        householdRepository.findById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found: " + householdId));

        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        List<ConsumptionLog> logs = repository.findByHouseholdHouseholdIdAndConsumptionDateBetween(householdId, start, end);

        BigDecimal consumed = sum(logs, ConsumptionLog::getUnitsConsumed);
        BigDecimal share = sum(logs, ConsumptionLog::getGenerationShare);
        BigDecimal exported = sum(logs, ConsumptionLog::getUnitsExported);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("householdId", householdId);
        result.put("year", year);
        result.put("month", month);
        result.put("daysLogged", logs.size());
        result.put("totalConsumedUnits", consumed);
        result.put("totalGenerationShareUnits", share);
        result.put("totalExportedUnits", exported);
        return result;
    }

    private BigDecimal sum(List<ConsumptionLog> logs, java.util.function.Function<ConsumptionLog, BigDecimal> getter) {
        return logs.stream().map(getter).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.HALF_UP);
    }

    private void validate(ConsumptionLog item) {
        if (item == null || item.getConsumptionDate() == null) throw new IllegalArgumentException("Consumption date is required");
        if (item.getUnitsConsumed() == null || item.getUnitsConsumed().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Consumed units cannot be negative");
        }
    }
}
