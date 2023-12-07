package ru.ubulllka.school_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ubulllka.school_service.models.DTO.CreateLesson;
import ru.ubulllka.school_service.models.Lesson;
import ru.ubulllka.school_service.models.request.ValidateRequest;
import ru.ubulllka.school_service.services.LessonService;

import java.text.ParseException;

@RestController
@RequestMapping("/lessons/")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonSer;
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
    public ResponseEntity<Iterable<Lesson>> getAll(){
        return ResponseEntity.status(200).body(lessonSer.getAll());
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable int id){
        Lesson lesson = lessonSer.getOne(id);
        return (lesson == null) ? ResponseEntity.status(404).body("Lesson not found") :
                ResponseEntity.status(200).body(lesson);
    }



    @PostMapping("")
    @ResponseBody
    public ResponseEntity save(@RequestHeader("username") String username, @RequestHeader("token") String token,
                               @RequestBody CreateLesson createLesson) {
        String role = checkRights(new ValidateRequest(username, token));
        ..        if (!"ADMIN".equals(role) && !lessonSer.checkTeacher(username, role, createLesson.getTeacher_id()))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Lesson lesson;
        try {
            lesson = lessonSer.save(createLesson);
        } catch (ParseException exception) {
            return ResponseEntity.status(500).body("Wrong date and/or time");
        }
        return (lesson == null) ? ResponseEntity.status(404).body("Wrong parameters") :
                ResponseEntity.status(201).body(lesson);
    }

    @PatchMapping("{id}")
    @ResponseBody
    public ResponseEntity update(@RequestHeader("username") String username, @RequestHeader("token") String token,
                         @PathVariable int id, @RequestBody CreateLesson createLesson) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role) && !lessonSer.checkTeacher(username, role, createLesson.getTeacher_id()))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Lesson lesson;
        try {
            lesson = lessonSer.update(id, createLesson);
        } catch (ParseException exception) {
            return ResponseEntity.status(500).body("Wrong date and/or time");
        }
        return (lesson == null) ? ResponseEntity.status(404).body("Wrong parameters") :
                ResponseEntity.status(200).body(lesson);
    }

    @DeleteMapping ("{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("username") String username, @RequestHeader("token") String token,
                                         @PathVariable int id) {
        String role = checkRights(new ValidateRequest(username, token));
        if (!"ADMIN".equals(role) && !lessonSer.checkTeacherLessonId(username, role, id))
            return ResponseEntity.status(404).body("You have no rights to perform this operation");
        Boolean isDeleted = lessonSer.delete(id);
        return (isDeleted) ? ResponseEntity.status(200).body("Lesson delete") :
                ResponseEntity.status(404).body("Lesson not found");
    }
}
