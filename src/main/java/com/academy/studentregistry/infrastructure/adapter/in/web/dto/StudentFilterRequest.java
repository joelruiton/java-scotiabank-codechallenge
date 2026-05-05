package com.academy.studentregistry.infrastructure.adapter.in.web.dto;

import com.academy.studentregistry.domain.model.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentFilterRequest {
    @NotBlank(message = "El campo 'estado' es obligatorio")
    @Pattern(regexp = Status.ACTIVO + "|" + Status.INACTIVO, message = "El campo 'estado' solo acepta: ACTIVO o INACTIVO")
    private String estado;

    @NotNull(message = "El campo 'edad' es obligatorio")
    @Min(value = 1, message = "El campo 'edad' debe ser minimo 1")
    @Max(value = 120, message = "El campo 'edad' debe ser maximo 120")
    private Integer edad;
}