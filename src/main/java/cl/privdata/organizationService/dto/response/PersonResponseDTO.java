package cl.privdata.organizationService.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PersonResponseDTO {
    private UUID id;
    private UUID organizationId;
    private UUID departmentId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String rut;
    private String email;
    private String phone;
    private String position;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
