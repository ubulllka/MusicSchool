package ru.ubulllka.auth_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String name;
    private String password;
    private Role role;

    public UserDTO(String name, String password, String role) {
        this.name = name;
        this.password = password;
        switch (role){
            case "STUDENT":
                this.role = Role.STUDENT;
                break;
            case "TEACHER":
                this.role = Role.TEACHER;
                break;
            case "ADMIN":
                this.role = Role.ADMIN;
                break;
        }
    }
}

