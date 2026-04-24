## Repositorios de PrivData:



## authService: [Link al repo](https://github.com/WVinet/authservice-PrivData.git)
## frontend-PrivDataReact: [Link al repo](https://github.com/miloQ1/frontend-Privdata.git)


# Documentación de la API - Organization Service


## Descripción general

**Organization Service** es una API RESTful que permite gestionar organizaciones, departamentos, personas y configuraciones asociadas. Facilita la administración de la estructura organizacional, usuarios y preferencias de privacidad/idioma de cada organización.

### Funcionalidades principales
- Crear, listar, obtener y actualizar organizaciones.
- Gestionar departamentos dentro de cada organización.
- Administrar personas asociadas a organizaciones y departamentos.
- Configurar opciones de privacidad, idioma y exportación de datos para cada organización.

---

## Modelo de datos principal

**Organization**
- id (UUID)
- name, legalName, rut, businessType, email, phone, address
- isActive (boolean), timestamps

**Department**
- id (UUID)
- organization (relación), name, description
- isActive (boolean), createdAt

**Person**
- id (UUID)
- organization (relación), department (opcional)
- firstName, lastName, fullName, rut, email

**OrganizationSettings**
- id (UUID)
- organization (relación uno a uno)
- defaultLanguage, privacyEmail, allowDataExports, updatedAt

---

## Endpoints

Los principales endpoints disponibles son:

---

## Organización

### Crear organización
- **POST** `/api/organizations`
- **Body:** OrganizationCreateRequestDTO
- **Respuesta:** ApiResponseDTO<OrganizationResponseDTO>

### Listar organizaciones
- **GET** `/api/organizations`
- **Respuesta:** ApiResponseDTO<List<OrganizationResponseDTO>>

### Obtener organización por ID
- **GET** `/api/organizations/{organizationId}`
- **Respuesta:** ApiResponseDTO<OrganizationResponseDTO>

### Actualizar organización
- **PUT** `/api/organizations/{organizationId}`
- **Body:** OrganizationUpdateRequestDTO
- **Respuesta:** ApiResponseDTO<OrganizationResponseDTO>

---

## Departamento

### Crear departamento
- **POST** `/api/organizations/{organizationId}/departments`
- **Body:** DepartmentCreateRequestDTO
- **Respuesta:** ApiResponseDTO<DepartmentResponseDTO>

### Listar departamentos
- **GET** `/api/organizations/{organizationId}/departments`
- **Query Params:** `active` (opcional)
- **Respuesta:** ApiResponseDTO<List<DepartmentResponseDTO>>

### Obtener departamento por ID
- **GET** `/api/organizations/{organizationId}/departments/{departmentId}`
- **Respuesta:** ApiResponseDTO<DepartmentResponseDTO>

### Actualizar departamento
- **PUT** `/api/organizations/{organizationId}/departments/{departmentId}`
- **Body:** DepartmentUpdateRequestDTO
- **Respuesta:** ApiResponseDTO<DepartmentResponseDTO>

---

## Persona

### Crear persona
- **POST** `/api/organizations/{organizationId}/persons`
- **Body:** PersonCreateRequestDTO
- **Respuesta:** ApiResponseDTO<PersonResponseDTO>

### Listar personas
- **GET** `/api/organizations/{organizationId}/persons`
- **Query Params:** `departmentId`, `active` (opcionales)
- **Respuesta:** ApiResponseDTO<List<PersonResponseDTO>>

### Obtener persona por ID
- **GET** `/api/organizations/{organizationId}/persons/{personId}`
- **Respuesta:** ApiResponseDTO<PersonResponseDTO>

### Actualizar persona
- **PUT** `/api/organizations/{organizationId}/persons/{personId}`
- **Body:** PersonUpdateRequestDTO
- **Respuesta:** ApiResponseDTO<PersonResponseDTO>

---

## Configuración de Organización

### Crear configuración
- **POST** `/api/organizations/{organizationId}/settings`
- **Body:** OrganizationSettingsCreateRequestDTO
- **Respuesta:** ApiResponseDTO<OrganizationSettingsResponseDTO>

### Obtener configuración
- **GET** `/api/organizations/{organizationId}/settings`
- **Respuesta:** ApiResponseDTO<OrganizationSettingsResponseDTO>

### Actualizar configuración
- **PUT** `/api/organizations/{organizationId}/settings`
- **Body:** OrganizationSettingsUpdateRequestDTO
- **Respuesta:** ApiResponseDTO<OrganizationSettingsResponseDTO>

---

## Notas
- Todos los endpoints retornan un objeto `ApiResponseDTO` con los datos y un mensaje.
- Los DTOs de request y response se encuentran en el paquete `cl.privdata.organizationService.dto`.
- Los IDs son de tipo UUID.
- Se recomienda validar los datos antes de enviar las peticiones.



