package com.example.solar_tracker.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "consumption_logs",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"household_id", "consumption_date"}
        )
)
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consumptionLogId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @Column(name = "consumption_date", nullable = false)
    private LocalDate consumptionDate;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal unitsConsumed;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal generationShare = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal unitsExported = BigDecimal.ZERO;

    public ConsumptionLog() {
    }

    public Long getConsumptionLogId() {
        return consumptionLogId;
    }

    public void setConsumptionLogId(Long consumptionLogId) {
        this.consumptionLogId = consumptionLogId;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public LocalDate getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(LocalDate consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public BigDecimal getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(BigDecimal unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public BigDecimal getGenerationShare() {
        return generationShare;
    }

    public void setGenerationShare(BigDecimal generationShare) {
        this.generationShare = generationShare;
    }

    public BigDecimal getUnitsExported() {
        return unitsExported;
    }

    public void setUnitsExported(BigDecimal unitsExported) {
        this.unitsExported = unitsExported;
    }
}