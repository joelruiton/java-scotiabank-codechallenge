package com.academy.studentregistry.infrastructure.adapter.out.persistence;

import com.academy.studentregistry.domain.model.Student;
import com.academy.studentregistry.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final StudentR2dbcRepository r2dbcRepository;

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Void> save(Student student) {
        return r2dbcRepository.save(toEntity(student)).then();
    }

    @Override
    public Flux<Student> findAll() {
        return r2dbcRepository.findAll().map(this::toDomain);
    }

    @Override
    public Flux<Student> findAllByEstado(String estado) {
        return r2dbcRepository.findByEstado(estado).map(this::toDomain);
    }

    private StudentEntity toEntity(Student s) {
        return StudentEntity.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .apellido(s.getApellido())
                .estado(s.getEstado())
                .edad(s.getEdad())
                .build();
    }

    private Student toDomain(StudentEntity e) {
        return Student.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .apellido(e.getApellido())
                .estado(e.getEstado())
                .edad(e.getEdad())
                .build();
    }
}
