package com.academy.studentregistry.domain.exception;

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(Long id) {
        super("No se pudo realizar la grabacion. Ya existe un alumno registrado con el id: " + id);
    }
}
