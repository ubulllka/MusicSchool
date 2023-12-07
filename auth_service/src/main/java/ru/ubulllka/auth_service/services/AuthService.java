package ru.ubulllka.auth_service.services;

import ru.ubulllka.auth_service.models.*;
import ru.ubulllka.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        UserDTO userDTO = userRepository.findUserByUsername(request.getUsername());
        if (userDTO == null) {
            UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                    password(passwordEncoder.encode(request.getPassword())).role(Role.STUDENT).build();
            userRepository.save(userImpl);
            var jwtToken = jwtService.generateToken(userImpl);
            return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                    username(userImpl.getUsername()).build();
        } else {
            return null;
        }
    }
    public AuthenticationResponse registerTeacher(RegisterRequest request) {
        UserDTO userDTO = userRepository.findUserByUsername(request.getUsername());
        if (userDTO == null) {
            UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                    password(passwordEncoder.encode(request.getPassword())).role(Role.TEACHER).build();
            userRepository.save(userImpl);
            var jwtToken = jwtService.generateToken(userImpl);
            return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                    username(userImpl.getUsername()).build();
        } else {
            return null;
        }
    }
    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        UserDTO userDTO = userRepository.findUserByUsername(request.getUsername());
        if (userDTO == null) {
            UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                    password(passwordEncoder.encode(request.getPassword())).role(Role.ADMIN).build();
            userRepository.save(userImpl);
            var jwtToken = jwtService.generateToken(userImpl);
            return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                    username(userImpl.getUsername()).build();
        } else {
            return null;
        }
    }
    public AuthenticationResponse login(RegisterRequest request) {
        UserDTO userDto = userRepository.findUserByUsername(request.getUsername());
        if (userDto != null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserImpl user = new UserImpl(userDto.getName(), userDto.getPassword(), userDto.getRole());
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).role(user.getRole().toString()).
                    username(user.getUsername()).build();
        } else {
            return null;
        }
    }
    public String checkToken(CheckRequest request){
        UserDTO userDto = userRepository.findUserByUsername(request.getUsername());
        UserImpl user = new UserImpl();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        if (jwtService.isTokenValid(request.getToken(), user))
            return user.getRole().name();
        else
            return Role.ANON.name();
    }

    public String deleteUser(String username){
        return userRepository.deleteUser(username);
    }
}

