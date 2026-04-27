package com.academy.studentregistry.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Student {
    Long id;
    String nombre;
    String apellido;
    String estado;
    Integer edad;
}
