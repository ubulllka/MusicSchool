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
@Table(name = "specialization")
@NoArgsConstructor
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "specialization", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Person> teachers;
}




