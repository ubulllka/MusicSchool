package ru.ubulllka.school_service.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ubulllka.school_service.models.DTO.SetSpecialization;
import ru.ubulllka.school_service.models.DTO.UpdatePerson;
import ru.ubulllka.school_service.models.Lesson;
import ru.ubulllka.school_service.models.Person;
import ru.ubulllka.school_service.models.request.ValidateRequest;
import ru.ubulllka.school_service.services.PersonService;


@RestController
@RequestMapping("/person/")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personSer;
    private final WebClient authClient = WebClient.create("http://auth:8081/auth/");

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
    public ResponseEntity<Iterable<Person>> getAll(){
        return ResponseEntity.status(200).body(personSer.getAll());
    }

    @GetMapping("teachers")
    @ResponseBody
    public ResponseEntity<Iterable<Person>> getTeachers(){
        return ResponseEntity.status(200).body(personSer.getTeachers());
    }

    @GetMapping("students")
    @ResponseBody
    public ResponseEntity<Iterable<Person>> getStudents(){
        return ResponseEntity.status(200).body(personSer.getStudents());
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable int id){
        Person person = personSer.getOne(id);
        return (person == null) ? ResponseEntity.status(404).body("Person not found") :
                ResponseEntity.status(200).body(person);
    }

    @GetMapping("{id}/lessons")
    @ResponseBody
    public ResponseEntity getLessons(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                     @PathVariable int id){
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role) && !personSer.checkPersonForLessons(username, role, id))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Iterable<Lesson> lessons = personSer.getLessons(id);
        return (lessons == null) ? ResponseEntity.status(404).body("Person not found") :
                ResponseEntity.status(200).body(lessons);
    }

    @PostMapping("{id}/set")
    public ResponseEntity<String> setSpecialization(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                    @PathVariable int id, @RequestBody SetSpecialization specialization){
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        String result = personSer.setSpecialization(id, specialization);

        return (result.equals("Specialization set")) ? ResponseEntity.status(200).body(result) :
                ResponseEntity.status(500).body(result);
    }

    @PatchMapping("{id}")
    @ResponseBody
    public ResponseEntity update(@RequestHeader("username") String username, @RequestHeader("token") String token,
                         @PathVariable int id, @RequestBody UpdatePerson updatePerson) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role) && !"TEACHER".equals(role) && !personSer.checkStudent(role, username, id))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Person person = personSer.update(id, updatePerson);
        return (person == null) ? ResponseEntity.status(404).body("Person not found") :
                ResponseEntity.status(200).body(person);
    }

    @DeleteMapping("{userNamePath}")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("username") String username, @RequestHeader("token") String token,
                         @PathVariable String userNamePath) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        String url = "user/" + userNamePath;
        String authResponse = authClient.delete()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        System.out.println(authResponse);
        boolean isDeleted = personSer.delete(userNamePath);
        return (isDeleted) ? ResponseEntity.status(200).body("Person delete") :
                ResponseEntity.status(404).body("Person not found");
    }

}
