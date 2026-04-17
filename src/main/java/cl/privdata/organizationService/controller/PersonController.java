package cl.privdata.organizationService.controller;

import cl.privdata.organizationService.dto.request.PersonRequestDTO;
import cl.privdata.organizationService.dto.request.StatusUpdateRequestDTO;
import cl.privdata.organizationService.dto.response.PersonResponseDTO;
import cl.privdata.organizationService.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> findAllByOrganization(@RequestParam UUID organizationId) {
        return ResponseEntity.ok(service.findAllByOrganization(organizationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> create(@Valid @RequestBody PersonRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> update(@PathVariable UUID id,
                                                  @Valid @RequestBody PersonRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PersonResponseDTO> updateStatus(@PathVariable UUID id,
                                                        @Valid @RequestBody StatusUpdateRequestDTO request) {
        return ResponseEntity.ok(service.updateStatus(id, request.getIsActive()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
