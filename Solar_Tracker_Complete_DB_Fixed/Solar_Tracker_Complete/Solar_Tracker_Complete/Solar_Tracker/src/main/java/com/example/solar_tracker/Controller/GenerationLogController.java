package com.example.solar_tracker.Controller;

import com.example.solar_tracker.Model.GenerationLog;
import com.example.solar_tracker.Service.GenerationLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generation")
@CrossOrigin(origins = "*")
public class GenerationLogController {

    private final GenerationLogService service;

    public GenerationLogController(GenerationLogService service) {
        this.service = service;
    }

    @GetMapping
    public List<GenerationLog> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GenerationLog getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public GenerationLog create(@RequestParam Long installationId,
                                @RequestBody GenerationLog item) {
        return service.create(item, installationId);
    }

    @PutMapping("/{id}")
    public GenerationLog update(@PathVariable Long id,
                                @RequestBody GenerationLog item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}