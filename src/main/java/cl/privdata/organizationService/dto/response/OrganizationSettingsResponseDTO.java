package cl.privdata.organizationService.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrganizationSettingsResponseDTO {
    private UUID id;
    private UUID organizationId;
    private String defaultLanguage;
    private String privacyEmail;
    private boolean allowDataExports;
    private LocalDateTime updatedAt;
}
