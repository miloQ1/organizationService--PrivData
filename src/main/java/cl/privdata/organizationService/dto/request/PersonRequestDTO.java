package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class PersonRequestDTO {

    @NotNull(message = "El ID de la organización es obligatorio")
    private UUID organizationId;

    private UUID departmentId;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private String rut;

    @Email(message = "Formato de correo inválido")
    private String email;

    private String phone;

    private String position;

    private boolean isActive;
}
