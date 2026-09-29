package com.example.solar_tracker.Repository;

import com.example.solar_tracker.Model.Installation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallationRepository
        extends JpaRepository<Installation, Long> {
}