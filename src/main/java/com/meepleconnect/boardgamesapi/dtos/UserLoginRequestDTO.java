package com.meepleconnect.boardgamesapi.dtos;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String username;
    private String password;
}