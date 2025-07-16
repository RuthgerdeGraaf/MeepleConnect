package com.meepleconnect.boardgamesapi.dtos;

import lombok.Data;

@Data
public class RoleResponseDTO {
    private Long id;
    private String roleName;
    private boolean active;
    private String description;
} 