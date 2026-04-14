package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequest;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponse;
import cl.privdata.organizationService.service.OrganizationSettingsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organization-settings")
public class OrganizationSettingsController {

    private final OrganizationSettingsService service;

    public OrganizationSettingsController(OrganizationSettingsService service) {
        this.service = service;
    }

    // Consulta políticas globales: retención, alertas de vencimiento, correo de privacidad
    @GetMapping
    public ResponseEntity<OrganizationSettingsResponse> findByOrganization(@RequestParam UUID organizationId) {
        return ResponseEntity.ok(service.findByOrganization(organizationId));
    }

    // Actualiza los parámetros críticos de cumplimiento
    @PutMapping
    public ResponseEntity<OrganizationSettingsResponse> createOrUpdate(@Valid @RequestBody OrganizationSettingsRequest request) {
        return ResponseEntity.ok(service.createOrUpdate(request));
    }
}
