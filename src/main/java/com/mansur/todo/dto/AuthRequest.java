package com.mansur.todo.dto;

import lombok.Data;

// Used for register and login requests
@Data
public class AuthRequest {
    private String username;
    private String password;
}
