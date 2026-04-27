package com.academy.studentregistry.domain.port.out;

import com.academy.studentregistry.domain.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepositoryPort {
    Mono<Boolean> existsById(Long id);
    Mono<Void> save(Student student);
    Flux<Student> findAllByEstado(String estado);
    Flux<Student> findAll();
}
