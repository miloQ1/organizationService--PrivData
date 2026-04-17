package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationStatusUpdateRequestDTO {

    @NotNull(message = "El estado isActive es obligatorio")
    private Boolean isActive;
}