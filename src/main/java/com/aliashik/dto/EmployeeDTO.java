package com.aliashik.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    private Long id;
    private String name;
    private String role;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String name, String role) {
        this.name = name;
        this.role = role;
    }
}