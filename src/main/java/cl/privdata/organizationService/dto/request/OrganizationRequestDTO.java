package cl.privdata.organizationService.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizationRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La razón social es obligatoria")
    private String legalName;

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;
    
    @Column(name = "business_type")
    private String businessType;

    @Email(message = "Formato de correo inválido")
    private String email;

    private String phone;

    private String address;

    private boolean isActive;
}
