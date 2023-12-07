package ru.ubulllka.school_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ubulllka.school_service.models.Lesson;
import ru.ubulllka.school_service.models.Person;

import java.util.Date;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByDateAndStudent(Date date, Person student);
    List<Lesson> findAllByDateAndTeacher(Date date, Person person);

}

