package com.example.solar_tracker.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "households")
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long householdId;

    @Column(nullable = false, length = 120)
    private String householdName;

    @Column(nullable = false, precision = 8, scale = 6)
    private BigDecimal allocationRatio;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "installation_id", nullable = false)
    private Installation installation;

    public Household() {
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Long householdId) {
        this.householdId = householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public BigDecimal getAllocationRatio() {
        return allocationRatio;
    }

    public void setAllocationRatio(BigDecimal allocationRatio) {
        this.allocationRatio = allocationRatio;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }
}