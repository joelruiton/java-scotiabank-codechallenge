package com.academy.studentregistry.domain.port.in;

import com.academy.studentregistry.domain.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentUseCase {
    Mono<Void> saveStudent(Student student);
    Flux<Student> findAllByFilters(String estado, Integer edad);
    Flux<Student> findAllActiveByFilters(Integer edad);
}
