package ru.ubulllka.school_service.models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
}
