package com.academy.studentregistry.infrastructure.adapter.in.web;

import com.academy.studentregistry.domain.model.Student;
import com.academy.studentregistry.domain.port.in.StudentUseCase;
import com.academy.studentregistry.infrastructure.adapter.in.web.dto.StudentFilterRequest;
import com.academy.studentregistry.infrastructure.adapter.in.web.dto.StudentRequest;
import com.academy.studentregistry.infrastructure.adapter.in.web.dto.StudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentUseCase studentUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> saveStudent(@Valid @RequestBody StudentRequest request) {
        return studentUseCase.saveStudent(toDomain(request));
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<StudentResponse> getActiveStudents(StudentFilterRequest filter) {
        return studentUseCase.findAllActiveByFilters(filter.getEdad())
                .map(StudentResponse::from);
    }

    private Student toDomain(StudentRequest r) {
        return Student.builder()
                .id(r.getId())
                .nombre(r.getNombre())
                .apellido(r.getApellido())
                .estado(r.getEstado())
                .edad(r.getEdad())
                .build();
    }
}
