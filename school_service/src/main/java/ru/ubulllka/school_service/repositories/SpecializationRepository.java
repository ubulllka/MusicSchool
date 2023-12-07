package ru.ubulllka.school_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ubulllka.school_service.models.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
}



