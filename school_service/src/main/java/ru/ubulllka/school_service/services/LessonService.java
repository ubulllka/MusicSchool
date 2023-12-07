package ru.ubulllka.school_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ubulllka.school_service.models.DTO.CreateLesson;
import ru.ubulllka.school_service.models.Lesson;
import ru.ubulllka.school_service.models.Office;
import ru.ubulllka.school_service.models.Person;
import ru.ubulllka.school_service.models.Role;
import ru.ubulllka.school_service.repositories.LessonRepository;
import ru.ubulllka.school_service.repositories.OfficeRepository;
import ru.ubulllka.school_service.repositories.PersonRepository;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRep;
    private final OfficeRepository officeRep;
    private final PersonRepository personRep;
    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    public Iterable<Lesson> getAll(){
        return lessonRep.findAll();
    }

    public Lesson getOne(int id) {
        Optional<Lesson> optional = lessonRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }



    public boolean checkTeacherLessonId(String username, String role, int lesson_id){
        Lesson lesson = getOne(lesson_id);
        if (lesson == null) return false;
        return checkTeacher(username, role, lesson.getTeacher().getId());
    }
    public boolean checkTeacher(String username, String role, int id) {
        if (!"TEACHER".equals(role)) return false;
        Optional<Person> optionalTeacher = personRep.findById(id);
        if (optionalTeacher.isEmpty()) return false;
        Person teacher = optionalTeacher.get();
        return teacher.getUsername().equals(username);
    }


    private boolean checkTimeLesson(List<Lesson> lessons, Time time) {
        LocalTime localTime1 = time.toLocalTime();
        LocalTime localTime2 = localTime1.plusHours(1);
        for (Lesson item : lessons){
            LocalTime start = item.getTime().toLocalTime();
            LocalTime end = start.plusHours(1);
            System.out.println(start + " " + end + " " + localTime1);
            if (localTime1.isAfter(start) && localTime1.isBefore(end) || localTime1.equals(start)) {
                return true;
            }
            if (localTime2.isAfter(start) && localTime2.isBefore(end)) {
                return true;
            }
        }
        return false;
    }
    public Lesson save(CreateLesson createLesson) throws ParseException {
        Date date = new java.sql.Date(dateFormat.parse(createLesson.getDate()).getTime());
        Time time = new java.sql.Time(timeFormat.parse(createLesson.getTime()).getTime());
        Optional<Office> optionalOffice = officeRep.findById(createLesson.getOffice_id());
        if (optionalOffice.isEmpty()) return null;
        Optional<Person> optionalTeacher = personRep.findById(createLesson.getTeacher_id());
        if (optionalTeacher.isEmpty()) return null;
        Optional<Person> optionalStudent = personRep.findById(createLesson.getStudent_id());
        if (optionalStudent.isEmpty()) return null;
        Office office = optionalOffice.get();
        Person teacher = optionalTeacher.get();
        Person student = optionalStudent.get();
        if (student.getRole() != Role.STUDENT || teacher.getRole() != Role.TEACHER ) return null;

        List<Lesson> lessonsStudent = lessonRep.findAllByDateAndStudent(date, student);
        if (checkTimeLesson(lessonsStudent, time)) return null;
        List<Lesson> lessonsTeacher = lessonRep.findAllByDateAndTeacher(date, teacher);
        if (checkTimeLesson(lessonsTeacher, time)) return null;

        Lesson lesson = new Lesson();
        lesson.setDate(date);
        lesson.setTime(time);
        lesson.setOffice(office);
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        office.getLessons().add(lesson);
        officeRep.save(office);
        teacher.getTeacherLessons().add(lesson);
        personRep.save(teacher);
        student.getStudentLesson().add(lesson);
        personRep.save(student);
        return lessonRep.save(lesson);
    }

    public Lesson update(int id, CreateLesson createLesson) throws ParseException {
        Optional<Lesson> optional = lessonRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        Date date = (Date) dateFormat.parse(createLesson.getDate());
        Time time = (Time) timeFormat.parse(createLesson.getTime());
        Lesson lesson = optional.get();
        Person teacher = null;
        Person student = null;
        Office office = null;
        if (lesson.getTeacher().getId() != createLesson.getTeacher_id()) {
            Optional<Person> optionalTeacher = personRep.findById(createLesson.getTeacher_id());
            if (optionalTeacher.isEmpty()) return null;
            teacher = optionalTeacher.get();
            if (teacher.getRole() != Role.TEACHER) return null;
            List<Lesson> lessonsTeacher = lessonRep.findAllByDateAndTeacher(date, teacher);
            if (checkTimeLesson(lessonsTeacher, time)) return null;
            lesson.setTeacher(teacher);
        }
        if (lesson.getStudent().getId() != createLesson.getStudent_id()) {
            Optional<Person> optionalStudent = personRep.findById(createLesson.getStudent_id());
            if (optionalStudent.isEmpty()) return null;
            student = optionalStudent.get();
            if (student.getRole() != Role.STUDENT) return null;
            List<Lesson> lessonsStudent = lessonRep.findAllByDateAndStudent(date, student);
            if (checkTimeLesson(lessonsStudent, time)) return null;
            lesson.setStudent(student);
        }
        if (lesson.getOffice().getId() != createLesson.getOffice_id()){
            Optional<Office> optionalOffice = officeRep.findById(createLesson.getOffice_id());
            if (optionalOffice.isEmpty()) return null;
            office = optionalOffice.get();
            lesson.setOffice(office);
        }
        if (student != null) {
            student.getStudentLesson().add(lesson);
            personRep.save(student);
        }
        if (teacher != null) {
            teacher.getTeacherLessons().add(lesson);
            personRep.save(teacher);
        }
        if (office != null) {
            office.getLessons().add(lesson);
            officeRep.save(office);
        }
        lessonRep.save(lesson);
        return lesson;
    }

    public boolean delete(int id) {
        Optional<Lesson> optional = lessonRep.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        Lesson lesson = optional.get();
        Person teacher = lesson.getTeacher();
        teacher.getTeacherLessons().remove(lesson);
        personRep.save(teacher);
        Person student = lesson.getStudent();
        student.getStudentLesson().remove(lesson);
        personRep.save(student);
        Office office = lesson.getOffice();
        office.getLessons().remove(lesson);
        officeRep.save(office);
        lessonRep.delete(lesson);
        return true;
    }
}
