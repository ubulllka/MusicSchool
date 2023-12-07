package ru.ubulllka.school_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ubulllka.school_service.models.Office;

public interface OfficeRepository extends JpaRepository<Office, Integer> {
}
