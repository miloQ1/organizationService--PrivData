package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.DepartmentRequest;
import cl.privdata.organizationService.dto.response.DepartmentResponse;
import cl.privdata.organizationService.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DepartmentResponse>> findAllByOrganization(@PathVariable UUID organizationId) {
        return ResponseEntity.ok(service.findAllByOrganization(organizationId));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DepartmentResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.deactivate(id));
    }
}
