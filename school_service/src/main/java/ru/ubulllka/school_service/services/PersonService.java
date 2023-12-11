package ru.ubulllka.school_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ubulllka.school_service.models.DTO.SetSpecialization;
import ru.ubulllka.school_service.models.DTO.UpdatePerson;
import ru.ubulllka.school_service.models.Lesson;
import ru.ubulllka.school_service.models.Person;
import ru.ubulllka.school_service.models.Role;
import ru.ubulllka.school_service.models.Specialization;
import ru.ubulllka.school_service.repositories.LessonRepository;
import ru.ubulllka.school_service.repositories.PersonRepository;
import ru.ubulllka.school_service.repositories.SpecializationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRep;
    private final SpecializationRepository specializationRep;
    private final LessonRepository lessonRep;

    public Iterable<Person> getAll(){
        return personRep.findAll();
    }

    public Iterable<Person> getTeachers() {
        return personRep.findAllByRole(Role.TEACHER);
    }

    public Iterable<Person> getStudents() {
        return personRep.findAllByRole(Role.STUDENT);
    }

    public boolean checkPersonForLessons(String username, String role, int id){
        Optional<Person> optionalPerson = personRep.findById(id);
        if (optionalPerson.isEmpty()) return false;
        Person person = optionalPerson.get();
        if (!person.getRole().toString().equals(role)) return false;
        return person.getUsername().equals(username);
    }

    public Iterable<Lesson> getLessons(int id) {
        Optional<Person> optionalPerson = personRep.findById(id);
        if (optionalPerson.isEmpty()) return null;
        Person person = optionalPerson.get();
        if (person.getRole().equals(Role.STUDENT)){
            return person.getStudentLesson();
        } else {
            return person.getTeacherLessons();
        }
    }
    public Person getOne(int id) {
        Optional<Person> optional = personRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    public Person save(Person person){
        return personRep.save(person);
    }

    public Person update(int id, UpdatePerson updatePerson) {
        Optional<Person> optional = personRep.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        Person person = optional.get();
        person.setLastName((updatePerson.getFirstName().isEmpty()) ? person.getLastName() : updatePerson.getLastName());
        person.setFirstName((updatePerson.getFirstName().isEmpty()) ? person.getFirstName() : updatePerson.getFirstName());
        person.setMiddleName((updatePerson.getMiddleName().isEmpty() ? person.getMiddleName() : updatePerson.getMiddleName()));
        return personRep.save(person);
    }

    public boolean delete(String username) {
        Optional<Person> optional = personRep.findPersonByUsername(username);
        if (optional.isEmpty()){
            return false;
        }
        Person person = optional.get();
        if (person.getRole().equals(Role.TEACHER)) {
            Specialization specialization = person.getSpecialization();
            specialization.getTeachers().remove(person);
            specializationRep.save(specialization);
            lessonRep.deleteAll(person.getTeacherLessons());
        }
        if (person.getRole().equals(Role.STUDENT)) {
            lessonRep.deleteAll(person.getStudentLesson());
        }

        personRep.delete(person);
        return true;
    }

    public String setSpecialization(int id, SetSpecialization setSpecialization){
        Optional<Person> optionalPerson = personRep.findById(id);
        if (optionalPerson.isEmpty()){
            return "Person not found";
        }
        Optional<Specialization> optionalSpecialization = specializationRep.findById(setSpecialization.getSpecialization_id());
        if (optionalSpecialization.isEmpty()){
            return "Specialization not found";
        }
        Person person = optionalPerson.get();
        if (person.getRole() != Role.TEACHER){
            return "Person not teacher";
        }
        Specialization specialization = optionalSpecialization.get();
        specialization.getTeachers().add(person);
        person.setSpecialization(specialization);
        specializationRep.save(specialization);
        personRep.save(person);
        return "Specialization set";
    }

    public boolean checkStudent(String role, String username,  int id) {
        if (!"STUDENT".equals(role)) return false;
        Optional<Person> optionalStudent = personRep.findById(id);
        if (optionalStudent.isEmpty()) return false;
        Person student = optionalStudent.get();
        return student.getUsername().equals(username);
    }
}
