package com.academy.studentregistry.infrastructure.adapter.out.persistence;

import com.academy.studentregistry.domain.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryAdapterTest {

    @Mock
    private StudentR2dbcRepository r2dbcRepository;

    @InjectMocks
    private StudentRepositoryAdapter adapter;

    private StudentEntity entity;
    private Student student;

    @BeforeEach
    void setUp() {
        entity = StudentEntity.builder()
                .id(1L)
                .nombre("Luis")
                .apellido("Torres")
                .estado("ACTIVO")
                .edad(25)
                .build();

        student = Student.builder()
                .id(1L)
                .nombre("Luis")
                .apellido("Torres")
                .estado("ACTIVO")
                .edad(25)
                .build();
    }

    @Test
    @DisplayName("isNew debe retornar true al crear una entidad nueva")
    void studentEntity_isNew_shouldReturnTrue() {
        StudentEntity entity = StudentEntity.builder()
                .id(1L)
                .nombre("Luis")
                .apellido("Torres")
                .estado("ACTIVO")
                .edad(25)
                .build();

        assertTrue(entity.isNew());
        assertEquals(1L, entity.getId());
    }

    @Test
    @DisplayName("existsById debe retornar true cuando el alumno existe")
    void existsById_whenStudentExists_shouldReturnTrue() {
        when(r2dbcRepository.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsById(1L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("existsById debe retornar false cuando el alumno no existe")
    void existsById_whenStudentDoesNotExist_shouldReturnFalse() {
        when(r2dbcRepository.existsById(99L)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsById(99L))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("save debe persistir el alumno y retornar Mono vacio")
    void save_shouldPersistStudentAndReturnEmpty() {
        when(r2dbcRepository.save(any(StudentEntity.class))).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.save(student))
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll debe retornar alumnos correctamente mapeados")
    void findAll_shouldReturnMappedStudents() {
        when(r2dbcRepository.findAll()).thenReturn(Flux.just(entity));

        StepVerifier.create(adapter.findAll())
                .expectNextMatches(s ->
                        s.getId().equals(1L) &&
                                s.getNombre().equals("Luis") &&
                                s.getApellido().equals("Torres") &&
                                s.getEstado().equals("ACTIVO") &&
                                s.getEdad().equals(25)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll debe retornar Flux vacio cuando no hay coincidencias")
    void findAll_whenNoMatch_shouldReturnEmpty() {
        when(r2dbcRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(adapter.findAll())
                .verifyComplete();
    }

    @Test
    @DisplayName("findAllByEstado debe retornar alumnos con estado ACTIVO correctamente mapeados")
    void findAllByEstado_shouldReturnMappedStudents() {
        when(r2dbcRepository.findByEstado("ACTIVO")).thenReturn(Flux.just(entity));

        StepVerifier.create(adapter.findAllByEstado("ACTIVO"))
                .expectNextMatches(s ->
                        s.getId().equals(1L) &&
                        s.getNombre().equals("Luis") &&
                        s.getApellido().equals("Torres") &&
                        s.getEstado().equals("ACTIVO") &&
                        s.getEdad().equals(25)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("findAllByEstado debe retornar Flux vacio cuando no hay coincidencias")
    void findAllByEstado_whenNoMatch_shouldReturnEmpty() {
        when(r2dbcRepository.findByEstado("INACTIVO")).thenReturn(Flux.empty());

        StepVerifier.create(adapter.findAllByEstado("INACTIVO"))
                .verifyComplete();
    }
}
