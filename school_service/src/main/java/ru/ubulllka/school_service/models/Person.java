package ru.ubulllka.school_service.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "person")
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private Role role;

    @NotEmpty
    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", referencedColumnName = "id", nullable = true)
    private Specialization specialization;

    @JsonIgnore
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Lesson> studentLesson;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Lesson> teacherLessons;
}
