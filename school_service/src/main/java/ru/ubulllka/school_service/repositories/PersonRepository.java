package ru.ubulllka.school_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ubulllka.school_service.models.Person;
import ru.ubulllka.school_service.models.Role;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Iterable<Person> findAllByRole(Role role);
    Optional<Person> findPersonByUsername(String username);
}
