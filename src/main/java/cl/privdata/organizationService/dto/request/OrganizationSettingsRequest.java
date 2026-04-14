package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrganizationSettingsRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        UUID organizationId,

        String defaultLanguage,

        @NotNull(message = "Los días de retención son obligatorios")
        Integer retentionPolicyDays,

        @Email(message = "Correo de privacidad inválido")
        String privacyEmail,

        boolean allowDataExports,

        Integer consentExpiryAlertDays
) {}
