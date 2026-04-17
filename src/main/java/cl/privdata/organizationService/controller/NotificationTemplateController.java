package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.TemplateRenderRequest;
import cl.privdata.organizationService.dto.request.TemplateUpdateRequest;
import cl.privdata.organizationService.dto.response.NotificationTemplateResponse;
import cl.privdata.organizationService.dto.response.TemplateRenderResponse;
import cl.privdata.organizationService.service.NotificationTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/templates")
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    public NotificationTemplateController(NotificationTemplateService service) {
        this.service = service;
    }

    // Catálogo de todas las plantillas de la organización
    @GetMapping
    public ResponseEntity<List<NotificationTemplateResponse>> findAll(@RequestParam UUID organizationId) {
        return ResponseEntity.ok(service.findAllByOrganization(organizationId));
    }

    // Consulta el detalle de una plantilla específica por su código único
    @GetMapping("/{code}")
    public ResponseEntity<NotificationTemplateResponse> findByCode(@PathVariable String code,
                                                                    @RequestParam UUID organizationId) {
        return ResponseEntity.ok(service.findActiveByCode(organizationId, code));
    }

    // Edita el asunto y cuerpo de la plantilla activa
    @PutMapping("/{code}")
    public ResponseEntity<NotificationTemplateResponse> updateByCode(@PathVariable String code,
                                                                      @RequestParam UUID organizationId,
                                                                      @Valid @RequestBody TemplateUpdateRequest request) {
        return ResponseEntity.ok(service.updateByCode(organizationId, code, request));
    }

    // Procesa la plantilla sustituyendo variables {{clave}} y devuelve el texto final
    @PostMapping("/{code}/render")
    public ResponseEntity<TemplateRenderResponse> render(@PathVariable String code,
                                                          @RequestParam UUID organizationId,
                                                          @Valid @RequestBody TemplateRenderRequest request) {
        return ResponseEntity.ok(service.render(organizationId, code, request));
    }
}
