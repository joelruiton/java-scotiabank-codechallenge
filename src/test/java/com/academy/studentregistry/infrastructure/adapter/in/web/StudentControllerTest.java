package com.academy.studentregistry.infrastructure.adapter.in.web;

import com.academy.studentregistry.domain.exception.StudentAlreadyExistsException;
import com.academy.studentregistry.domain.model.Student;
import com.academy.studentregistry.domain.port.in.StudentUseCase;
import com.academy.studentregistry.infrastructure.adapter.in.web.dto.StudentRequest;
import com.academy.studentregistry.infrastructure.config.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private StudentUseCase studentUseCase;

    private StudentRequest buildValidRequest() {
        return new StudentRequest(1L, "Ana", "Garcia", "ACTIVO", 22);
    }

    // ─────────────────────────────────────────────────
    // POST /api/v1/students
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 204 cuando el alumno se guarda correctamente")
    void saveStudent_validRequest_shouldReturn204() {
        when(studentUseCase.saveStudent(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildValidRequest())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 409 cuando el id ya existe")
    void saveStudent_duplicateId_shouldReturn409() {
        when(studentUseCase.saveStudent(any()))
                .thenReturn(Mono.error(new StudentAlreadyExistsException(1L)));

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildValidRequest())
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.error").exists();
    }

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 400 cuando falta el nombre")
    void saveStudent_missingNombre_shouldReturn400() {
        StudentRequest req = new StudentRequest(1L, null, "Garcia", "ACTIVO", 22);

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.nombre").exists();
    }

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 400 cuando el estado es invalido")
    void saveStudent_invalidEstado_shouldReturn400() {
        StudentRequest req = new StudentRequest(1L, "Ana", "Garcia", "SUSPENDIDO", 22);

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.estado").exists();
    }

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 400 cuando el id es negativo")
    void saveStudent_negativeId_shouldReturn400() {
        StudentRequest req = new StudentRequest(-1L, "Ana", "Garcia", "ACTIVO", 22);

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.id").exists();
    }

    @Test
    @DisplayName("POST /api/v1/students - debe retornar 400 cuando la edad supera el maximo")
    void saveStudent_ageTooHigh_shouldReturn400() {
        StudentRequest req = new StudentRequest(1L, "Ana", "Garcia", "ACTIVO", 200);

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.edad").exists();
    }

    // ─────────────────────────────────────────────────
    // GET /api/v1/students/active
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/students/active - debe retornar lista de alumnos activos")
    void getActiveStudents_shouldReturnActiveStudentList() {
        Student s1 = Student.builder().id(1L).nombre("Ana").apellido("Garcia")
                .estado("ACTIVO").edad(22).build();
        Student s2 = Student.builder().id(2L).nombre("Luis").apellido("Torres")
                .estado("ACTIVO").edad(25).build();

        when(studentUseCase.findAllActiveByFilters(null)).thenReturn(Flux.just(s1, s2));

        webTestClient.get()
                .uri("/api/v1/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].nombre").isEqualTo("Ana")
                .jsonPath("$[0].estado").isEqualTo("ACTIVO")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].nombre").isEqualTo("Luis");
    }

    @Test
    @DisplayName("GET /api/v1/students/active - debe retornar lista vacia cuando no hay alumnos activos")
    void getActiveStudents_noActiveStudents_shouldReturnEmptyList() {
        when(studentUseCase.findAllActiveByFilters(null)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class).hasSize(0);
    }

    @Test
    @DisplayName("POST - debe retornar 400 cuando se envia un campo no permitido")
    void saveStudent_unknownField_shouldReturn400() {
        String bodyConCampoExtra = """
            {
                "id": 1,
                "nombre": "Juan",
                "apellido": "Perez",
                "estado": "ACTIVO",
                "edad": 22,
                "campoExtra": "valor"
            }
            """;

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyConCampoExtra)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").value(v ->
                        assertThat(v.toString()).contains("Campo no permitido")
                );
    }

    @Test
    @DisplayName("POST - debe retornar 400 cuando el cuerpo tiene un error de decodificacion generico")
    void saveStudent_decodingError_shouldReturn400() {
        String bodyMalformado = """
            {
                "id": "no-es-un-numero",
                "nombre": "Juan",
                "apellido": "Perez",
                "estado": "ACTIVO",
                "edad": 22
            }
            """;

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyMalformado)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").value(v ->
                        assertThat(v.toString()).contains("Error al procesar")
                );
    }
}
