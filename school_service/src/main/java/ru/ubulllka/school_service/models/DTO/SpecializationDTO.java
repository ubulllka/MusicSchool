package ru.ubulllka.school_service.models.DTO;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationDTO {
    private int id;
    private String name;
    private String description;
    private Set<PersonDTO> teachers;
}
