package ru.ubulllka.auth_service.controllers;


import org.springframework.web.bind.annotation.*;
import ru.ubulllka.auth_service.models.AuthenticationResponse;
import ru.ubulllka.auth_service.models.CheckRequest;
import ru.ubulllka.auth_service.models.RegisterRequest;
import ru.ubulllka.auth_service.models.UserImpl;
import ru.ubulllka.auth_service.repositories.UserRepository;
import ru.ubulllka.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<AuthenticationResponse> registerTeacher(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerTeacher(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/check")
    public ResponseEntity<String> check(@RequestBody CheckRequest request) {
        return ResponseEntity.ok(authService.checkToken(request));
    }

    @GetMapping("/get")
    public List<UserImpl> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(authService.deleteUser(username));
    }
}


