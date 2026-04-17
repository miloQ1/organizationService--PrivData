package cl.privdata.organizationService.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DepartmentResponseDTO {
    private UUID id;
    private UUID organizationId;
    private String name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
}
