package ru.ubulllka.school_service.models.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLesson {
    private String time;
    private String date;
    private int teacher_id;
    private int student_id;
    private int office_id;
}
