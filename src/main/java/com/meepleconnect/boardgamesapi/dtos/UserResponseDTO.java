package com.meepleconnect.boardgamesapi.dtos;

import lombok.Data;
import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String userName;
    private List<RoleResponseDTO> roles;
    private boolean isExpired;
    private boolean isLocked;
    private boolean areCredentialsExpired;
    private boolean isEnabled;
}