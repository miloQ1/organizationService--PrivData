package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.OrganizationUserRequest;
import cl.privdata.organizationService.dto.request.StatusUpdateRequest;
import cl.privdata.organizationService.dto.response.OrganizationUserResponse;
import cl.privdata.organizationService.service.OrganizationUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organization-users")
public class OrganizationUserController {

    private final OrganizationUserService service;

    public OrganizationUserController(OrganizationUserService service) {
        this.service = service;
    }

    // Lista a todo el personal interno y sus posiciones
    @GetMapping
    public ResponseEntity<List<OrganizationUserResponse>> findAllByOrganization(@RequestParam Long organizationId) {
        return ResponseEntity.ok(service.findAllByOrganization(organizationId));
    }

    // Asocia a un usuario con un departamento y cargo específico
    @PostMapping
    public ResponseEntity<OrganizationUserResponse> create(@Valid @RequestBody OrganizationUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    // Activa o suspende el acceso de un usuario dentro de la organización
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrganizationUserResponse> updateStatus(@PathVariable Long id,
                                                                  @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request.isActive()));
    }
}
