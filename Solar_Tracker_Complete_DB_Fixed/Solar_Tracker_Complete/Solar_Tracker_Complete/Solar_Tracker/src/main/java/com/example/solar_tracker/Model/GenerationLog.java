package com.example.solar_tracker.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "generation_logs",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"installation_id", "generation_date"}
        )
)
public class GenerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long generationLogId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "installation_id", nullable = false)
    private Installation installation;

    @Column(name = "generation_date", nullable = false)
    private LocalDate generationDate;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal unitsGenerated;

    public GenerationLog() {
    }

    public Long getGenerationLogId() {
        return generationLogId;
    }

    public void setGenerationLogId(Long generationLogId) {
        this.generationLogId = generationLogId;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }

    public BigDecimal getUnitsGenerated() {
        return unitsGenerated;
    }

    public void setUnitsGenerated(BigDecimal unitsGenerated) {
        this.unitsGenerated = unitsGenerated;
    }
}