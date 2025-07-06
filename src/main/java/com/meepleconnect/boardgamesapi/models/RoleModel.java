package com.meepleconnect.boardgamesapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {
    private Long id;
    private String roleName;
    private boolean active;
    private String description;

    public RoleModel(Long id) {
        this.id = id;
    }

    public RoleModel(String roleName) {
        this.id = -1L;
        this.roleName = roleName;
    }
}