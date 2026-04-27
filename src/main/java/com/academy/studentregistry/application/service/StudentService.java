package com.academy.studentregistry.application.service;

import com.academy.studentregistry.domain.exception.StudentAlreadyExistsException;
import com.academy.studentregistry.domain.model.Status;
import com.academy.studentregistry.domain.model.Student;
import com.academy.studentregistry.domain.port.in.StudentUseCase;
import com.academy.studentregistry.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentService implements StudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;

    @Override
    public Mono<Void> saveStudent(Student student) {
        return studentRepositoryPort.existsById(student.getId())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new StudentAlreadyExistsException(student.getId()));
                    }
                    return studentRepositoryPort.save(student);
                });
    }

    @Override
    public Flux<Student> findAllByFilters(String estado, Integer edad) {
        Flux<Student> students;

        if (estado != null && !estado.isBlank()) {
            students = studentRepositoryPort.findAllByEstado(estado);
        } else {
            students = studentRepositoryPort.findAll();
        }

        if (edad != null) {
            students = students.filter(s -> s.getEdad().equals(edad));
        }

        return students;
    }

    public Flux<Student> findAllActiveByFilters(Integer edad){
        return findAllByFilters(Status.ACTIVO, edad);
    }
}
