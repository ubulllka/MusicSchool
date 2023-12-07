package ru.ubulllka.school_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@Table(name = "lesson")
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;

    private Time time;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false, referencedColumnName = "id")
    private Person teacher;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "id")
    private Person student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "office_id", nullable = false, referencedColumnName = "id")
    private Office office;
}
