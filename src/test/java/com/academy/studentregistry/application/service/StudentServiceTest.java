package com.academy.studentregistry.application.service;

import com.academy.studentregistry.domain.exception.StudentAlreadyExistsException;
import com.academy.studentregistry.domain.model.Status;
import com.academy.studentregistry.domain.model.Student;
import com.academy.studentregistry.domain.port.out.StudentRepositoryPort;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepositoryPort studentRepositoryPort;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .estado(Status.ACTIVO)
                .edad(20)
                .build();
    }

    @Test
    @DisplayName("Debe guardar alumno exitosamente cuando el id no existe")
    void saveStudent_whenIdDoesNotExist_shouldSaveSuccessfully() {
        when(studentRepositoryPort.existsById(1L)).thenReturn(Mono.just(false));
        when(studentRepositoryPort.save(student)).thenReturn(Mono.empty());

        StepVerifier.create(studentService.saveStudent(student))
                .verifyComplete();

        verify(studentRepositoryPort).save(student);
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el id ya existe")
    void saveStudent_whenIdAlreadyExists_shouldThrowStudentAlreadyExistsException() {
        when(studentRepositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(studentService.saveStudent(student))
                .expectError(StudentAlreadyExistsException.class)
                .verify();

        verify(studentRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar todos los alumnos con estado ACTIVO")
    void findAllActiveStudents_shouldReturnOnlyActiveStudents() {
        Student active1 = student;
        Student active2 = Student.builder()
                .id(2L).nombre("Maria").apellido("Lopez")
                .estado(Status.ACTIVO).edad(22).build();

        when(studentRepositoryPort.findAllByEstado(Status.ACTIVO))
                .thenReturn(Flux.just(active1, active2));

        StepVerifier.create(studentService.findAllActiveByFilters(null))
                .expectNext(active1)
                .expectNext(active2)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByFilters debe retornar todos los alumnos (active null)")
    void findByFilters_shouldReturnAllStudentsActiveNull() {
        Student student20 = Student.builder()
                .id(1L).nombre("Juan").apellido("Perez")
                .estado(Status.ACTIVO).edad(20).build();
        Student student25 = Student.builder()
                .id(2L).nombre("Maria").apellido("Lopez")
                .estado(Status.ACTIVO).edad(25).build();

        when(studentRepositoryPort.findAll())
                .thenReturn(Flux.just(student20, student25));

        StepVerifier.create(studentService.findAllByFilters(null, null))
                .expectNext(student20)
                .expectNext(student25)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByFilters debe retornar todos los alumnos (active blank)")
    void findByFilters_shouldReturnAllStudentsActiveBlank() {
        Student student20 = Student.builder()
                .id(1L).nombre("Juan").apellido("Perez")
                .estado(Status.ACTIVO).edad(20).build();

        when(studentRepositoryPort.findAll())
                .thenReturn(Flux.just(student20));

        StepVerifier.create(studentService.findAllByFilters("", null))
                .expectNext(student20)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByFilters con edad debe retornar solo alumnos con esa edad")
    void findByFilters_withEdad_shouldFilterByEdad() {
        Student student20 = Student.builder()
                .id(1L).nombre("Juan").apellido("Perez")
                .estado(Status.ACTIVO).edad(20).build();
        Student student25 = Student.builder()
                .id(2L).nombre("Maria").apellido("Lopez")
                .estado(Status.ACTIVO).edad(25).build();

        when(studentRepositoryPort.findAllByEstado(Status.ACTIVO))
                .thenReturn(Flux.just(student20, student25));

        StepVerifier.create(studentService.findAllByFilters(Status.ACTIVO, 20))
                .expectNextMatches(s -> s.getEdad().equals(20))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Flux vacio cuando no hay alumnos activos")
    void findAllActiveStudents_whenNoActiveStudents_shouldReturnEmpty() {
        when(studentRepositoryPort.findAllByEstado(Status.ACTIVO)).thenReturn(Flux.empty());

        StepVerifier.create(studentService.findAllActiveByFilters(null))
                .verifyComplete();
    }
}
