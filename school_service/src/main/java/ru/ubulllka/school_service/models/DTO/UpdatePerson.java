package ru.ubulllka.school_service.models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePerson {
    private String firstName;
    private String middleName;
    private String lastName;
    private int specialization_id;
}
