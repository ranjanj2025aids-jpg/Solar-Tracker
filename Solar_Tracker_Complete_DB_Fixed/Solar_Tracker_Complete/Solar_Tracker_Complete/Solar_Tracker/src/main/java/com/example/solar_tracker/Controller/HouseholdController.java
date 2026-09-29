package com.example.solar_tracker.Controller;

import com.example.solar_tracker.Model.Household;
import com.example.solar_tracker.Service.HouseholdService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/households")
@CrossOrigin(origins = "*")
public class HouseholdController {

    private final HouseholdService service;

    public HouseholdController(HouseholdService service) {
        this.service = service;
    }

    @GetMapping
    public List<Household> getAll(@RequestParam(required = false) Long installationId) {
        return service.getAll(installationId);
    }

    @GetMapping("/{id}")
    public Household getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Household create(@RequestParam Long installationId, @RequestBody Household item) {
        return service.create(item, installationId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
