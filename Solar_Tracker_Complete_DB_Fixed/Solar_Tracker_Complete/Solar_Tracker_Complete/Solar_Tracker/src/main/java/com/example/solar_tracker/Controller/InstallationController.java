package com.example.solar_tracker.Controller;

import com.example.solar_tracker.Model.Installation;
import com.example.solar_tracker.Service.InstallationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/installations")
@CrossOrigin(origins = "*")
public class InstallationController {

    private final InstallationService service;

    public InstallationController(InstallationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Installation> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Installation getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Installation create(@RequestBody Installation item) {
        return service.create(item);
    }

    @PutMapping("/{id}")
    public Installation update(@PathVariable Long id,
                               @RequestBody Installation item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}