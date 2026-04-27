package com.academy.studentregistry.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface StudentR2dbcRepository extends ReactiveCrudRepository<StudentEntity, Long> {
    Flux<StudentEntity> findByEstado(String estado);
}
