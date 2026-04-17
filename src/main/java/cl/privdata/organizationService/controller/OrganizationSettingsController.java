package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequestDTO;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponseDTO;
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

    @GetMapping
    public ResponseEntity<OrganizationSettingsResponseDTO> findByOrganization(@RequestParam UUID organizationId) {
        return ResponseEntity.ok(service.findByOrganization(organizationId));
    }

    @PutMapping
    public ResponseEntity<OrganizationSettingsResponseDTO> createOrUpdate(@Valid @RequestBody OrganizationSettingsRequestDTO request) {
        return ResponseEntity.ok(service.createOrUpdate(request));
    }
}
