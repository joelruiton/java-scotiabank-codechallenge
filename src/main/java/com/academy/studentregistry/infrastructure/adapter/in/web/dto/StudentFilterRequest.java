package com.academy.studentregistry.infrastructure.adapter.in.web.dto;

import lombok.Data;

@Data
public class StudentFilterRequest {
    private String estado;
    private Integer edad;
}