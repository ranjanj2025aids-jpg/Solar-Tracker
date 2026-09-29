package com.example.solar_tracker.Controller;

import com.example.solar_tracker.Model.ConsumptionLog;
import com.example.solar_tracker.Service.ConsumptionLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consumption")
@CrossOrigin(origins = "*")
public class ConsumptionLogController {

    private final ConsumptionLogService service;

    public ConsumptionLogController(ConsumptionLogService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConsumptionLog> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ConsumptionLog getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ConsumptionLog create(@RequestParam Long householdId,
                                 @RequestBody ConsumptionLog item) {
        return service.create(item, householdId);
    }

    @PutMapping("/{id}")
    public ConsumptionLog update(@PathVariable Long id,
                                 @RequestBody ConsumptionLog item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/summary/monthly")
    public Map<String, Object> monthlySummary(
            @RequestParam Long householdId,
            @RequestParam int year,
            @RequestParam int month) {

        return service.monthlySummary(householdId, year, month);
    }
}