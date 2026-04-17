package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentRequestDTO {

    @NotNull(message = "El ID de la organización es obligatorio")
    private UUID organizationId;

    @NotBlank(message = "El nombre del departamento es obligatorio")
    private String name;

    private String description;

    private boolean isActive;
}
