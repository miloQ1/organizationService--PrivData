package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequestDTO {

    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
