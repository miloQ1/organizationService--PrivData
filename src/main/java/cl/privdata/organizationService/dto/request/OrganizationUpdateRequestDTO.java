package cl.privdata.organizationService.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizationUpdateRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La razón social es obligatoria")
    private String legalName;

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @Column(name = "business_type")
    private String businessType;

    @Email(message = "El correo no tiene un formato válido")
    private String email;

    private String phone;

    private String address;
}