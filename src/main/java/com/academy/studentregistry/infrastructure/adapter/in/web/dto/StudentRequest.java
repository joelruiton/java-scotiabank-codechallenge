package com.academy.studentregistry.infrastructure.adapter.in.web.dto;

import com.academy.studentregistry.domain.model.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotNull(message = "El campo 'id' es obligatorio")
    @Positive(message = "El campo 'id' debe ser un numero positivo")
    private Long id;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Size(min = 2, max = 100, message = "El campo 'nombre' debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El campo 'apellido' es obligatorio")
    @Size(min = 2, max = 100, message = "El campo 'apellido' debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El campo 'estado' es obligatorio")
    @Pattern(regexp = Status.ACTIVO + "|" + Status.INACTIVO, message = "El campo 'estado' solo acepta: ACTIVO o INACTIVO")
    private String estado;

    @NotNull(message = "El campo 'edad' es obligatorio")
    @Min(value = 1, message = "El campo 'edad' debe ser minimo 1")
    @Max(value = 120, message = "El campo 'edad' debe ser maximo 120")
    private Integer edad;
}
