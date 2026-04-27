package com.academy.studentregistry.infrastructure.adapter.in.web.dto;

import com.academy.studentregistry.domain.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String estado;
    private Integer edad;

    public static StudentResponse from(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .apellido(s.getApellido())
                .estado(s.getEstado())
                .edad(s.getEdad())
                .build();
    }
}
