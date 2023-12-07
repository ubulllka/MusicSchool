package ru.ubulllka.school_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ubulllka.school_service.models.Specialization;
import ru.ubulllka.school_service.models.request.ValidateRequest;
import ru.ubulllka.school_service.services.SpecializationService;


@RestController
@RequestMapping("/specializations/")
@RequiredArgsConstructor
public class SpecializationController {
    private final SpecializationService specializationSer;

    private final WebClient authClient = WebClient.create("http://auth:8081/auth");

    private String checkRights(ValidateRequest request) {
        return authClient.post()
                .uri("/check")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
    }


    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Iterable<Specialization>> getAll(){
        return ResponseEntity.status(200).body(specializationSer.getAll());
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable int id){
        Specialization specialization = specializationSer.getOne(id);
        return (specialization == null) ? ResponseEntity.status(404).body("Specialization not found") :
                ResponseEntity.status(200).body(specialization);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity save(@RequestHeader("username") String username, @RequestHeader("token") String token,
                               @RequestBody Specialization specialization) {
        String role = checkRights(new ValidateRequest(username, token));
        return ("ADMIN".equals(role)) ? ResponseEntity.status(201).body(specializationSer.save(specialization)) :
                ResponseEntity.status(404).body("You have no rights to perform this operation");
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity update(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                 @PathVariable int id, @RequestBody Specialization specialization) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Specialization updateSpecialization = specializationSer.update(id, specialization);
        return (updateSpecialization == null) ? ResponseEntity.status(404).body("Office not found") :
                ResponseEntity.status(200).body(updateSpecialization);
    }

    @DeleteMapping ("{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("username") String username, @RequestHeader("token") String token,
                         @PathVariable int id) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Boolean isDeleted = specializationSer.delete(id);
        return (isDeleted) ? ResponseEntity.status(200).body("Specialization delete") :
                ResponseEntity.status(404).body("Specialization not found");
    }
}


