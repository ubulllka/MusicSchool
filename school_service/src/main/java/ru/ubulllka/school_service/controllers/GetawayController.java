package ru.ubulllka.school_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ubulllka.school_service.models.Person;
import ru.ubulllka.school_service.models.Role;
import ru.ubulllka.school_service.models.request.AuthRequest;
import ru.ubulllka.school_service.models.request.AuthResponse;
import ru.ubulllka.school_service.repositories.PersonRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class GetawayController {

    private final WebClient authClient = WebClient.create("http://auth:8081/auth/");
    private final PersonRepository personRep;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authClient.post()
                .uri("login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .block()
                .bodyToMono(AuthResponse.class)
                .block();
        Optional<Person> optionalPerson = personRep.findPersonByUsername(authResponse.getUsername());
        return (optionalPerson.isPresent()) ? ResponseEntity.status(200).body(authResponse) :
                ResponseEntity.status(404).body("Person not found");
    }

    @PostMapping("register/admin")
    public ResponseEntity registerAdmin(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authClient.post()
                .uri("register/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .block()
                .bodyToMono(AuthResponse.class)
                .block();
        Person person = new Person();
        if (authResponse != null) {
            person.setUsername(authResponse.getUsername());
            person.setLastName("last_name");
            person.setFirstName("first_name");
            person.setRole(Role.ADMIN);
            personRep.save(person);
        }
        return (authResponse == null) ? ResponseEntity.status(409).body("Account with this username exists") :
                ResponseEntity.status(200).body(person);
    }

    @PostMapping("register/teacher")
    public ResponseEntity registerTeacher(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authClient.post()
                .uri("register/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .block()
                .bodyToMono(AuthResponse.class)
                .block();
        Person person = new Person();
        if (authResponse != null) {
            person.setUsername(authResponse.getUsername());
            person.setLastName("last_name");
            person.setFirstName("first_name");
            person.setRole(Role.TEACHER);
            personRep.save(person);
        }
        return (authResponse == null) ? ResponseEntity.status(409).body("Account with this username exists") :
                ResponseEntity.status(200).body(person);
    }

    @PostMapping("register")
    public ResponseEntity registerStudent(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authClient.post()
                .uri("register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .block()
                .bodyToMono(AuthResponse.class)
                .block();
        Person person = new Person();
        if (authResponse != null) {
            person.setUsername(authResponse.getUsername());
            person.setLastName("last_name");
            person.setFirstName("first_name");
            person.setRole(Role.STUDENT);
            personRep.save(person);
        }
        return (authResponse == null) ? ResponseEntity.status(409).body("Account with this username exists") :
                ResponseEntity.status(200).body(person);
    }
}
