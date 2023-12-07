package ru.ubulllka.school_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ubulllka.school_service.models.Office;
import ru.ubulllka.school_service.models.request.ValidateRequest;
import ru.ubulllka.school_service.services.OfficeService;

@RestController
@RequestMapping("/offices/")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeSer;
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
    public ResponseEntity<Iterable<Office>> getAll(){
        return ResponseEntity.status(200).body(officeSer.getAll());
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable int id){
        Office office = officeSer.getOne(id);
        return (office == null) ? ResponseEntity.status(404).body("Office not found") :
                ResponseEntity.status(200).body(office);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity save(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                       @RequestBody Office office) {
        String role = checkRights(new ValidateRequest(username, token));
        return ("ADMIN".equals(role)) ? ResponseEntity.status(201).body(officeSer.save(office)) :
                ResponseEntity.status(404).body("You have no rights to perform this operation");
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity update(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                 @PathVariable int id, @RequestBody Office office) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Office updateOffice = officeSer.update(id, office);
        return (updateOffice == null) ? ResponseEntity.status(404).body("Office not found") :
                ResponseEntity.status(200).body(updateOffice);
    }

    @DeleteMapping ("{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                         @PathVariable int id) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Boolean isDeleted = officeSer.delete(id);
        return (isDeleted) ? ResponseEntity.status(200).body("Office delete") :
                ResponseEntity.status(404).body("Office not found");
    }

}
