package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.OrganizationRequest;
import cl.privdata.organizationService.dto.response.OrganizationResponse;
import cl.privdata.organizationService.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    // Registra la organización base en el sistema
    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    // Obtiene los datos legales y de contacto de la empresa (RUT, nombre legal, dirección)
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Actualiza el perfil de la organización
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
