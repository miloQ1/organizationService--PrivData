package cl.privdata.organizationService.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class OrganizationSettingsRequestDTO {

    @NotNull(message = "El ID de la organización es obligatorio")
    private UUID organizationId;

    @Column(name ="default_language")
    private String defaultLanguage;

    @Email(message = "Correo de privacidad inválido")
    private String privacyEmail;

    @Column(name= "allow_data_exports")
    private boolean allowDataExports;
}
