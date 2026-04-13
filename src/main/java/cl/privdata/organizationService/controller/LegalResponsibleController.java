package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.LegalResponsibleRequest;
import cl.privdata.organizationService.dto.response.LegalResponsibleResponse;
import cl.privdata.organizationService.service.LegalResponsibleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legal-responsibles")
public class LegalResponsibleController {

    private final LegalResponsibleService service;

    public LegalResponsibleController(LegalResponsibleService service) {
        this.service = service;
    }

    // Lista a los encargados actuales
    @GetMapping
    public ResponseEntity<List<LegalResponsibleResponse>> findAllByOrganization(@RequestParam Long organizationId) {
        return ResponseEntity.ok(service.findAllByOrganization(organizationId));
    }

    // Registra a un nuevo responsable o delegado de protección de datos
    @PostMapping
    public ResponseEntity<LegalResponsibleResponse> create(@Valid @RequestBody LegalResponsibleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    // Actualiza los datos de contacto o el tipo de rol legal
    @PutMapping("/{id}")
    public ResponseEntity<LegalResponsibleResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody LegalResponsibleRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
