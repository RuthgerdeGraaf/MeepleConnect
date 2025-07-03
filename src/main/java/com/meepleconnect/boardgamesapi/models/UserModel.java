package com.meepleconnect.boardgamesapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id = -1L;
    private String userName;
    private String password;
    private List<RoleModel> roles = new ArrayList<>();
    private boolean isExpired;
    private boolean isLocked;
    private boolean areCredentialsExpired;
    private boolean isEnabled;

    // Custom constructor voor backward compatibility
    public UserModel(Long id) {
        this.id = id;
    }

    public List<String> getRoleNames() {
        return roles.stream()
                .map(RoleModel::getRoleName) // Converts each Role object to its name
                .collect(Collectors.toList());
    }
}