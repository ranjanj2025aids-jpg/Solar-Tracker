package com.example.solar_tracker.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "installations")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long installationId;

    @Column(nullable = false, length = 120)
    private String installationName;

    @Column(length = 250)
    private String location;

    @Column(name = "capacity_kw", nullable = false, precision = 10, scale = 3, columnDefinition = "DECIMAL(10,3) NOT NULL DEFAULT 1.000")
    private BigDecimal capacityKw;

    public Installation() {
    }

    public Installation(String installationName, String location, BigDecimal capacityKw) {
        this.installationName = installationName;
        this.location = location;
        this.capacityKw = capacityKw;
    }

    public Long getInstallationId() {
        return installationId;
    }

    public void setInstallationId(Long installationId) {
        this.installationId = installationId;
    }

    public String getInstallationName() {
        return installationName;
    }

    public void setInstallationName(String installationName) {
        this.installationName = installationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getCapacityKw() {
        return capacityKw;
    }

    public void setCapacityKw(BigDecimal capacityKw) {
        this.capacityKw = capacityKw;
    }
}
