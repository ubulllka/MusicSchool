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
@Table(name = "office")
@NoArgsConstructor
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "office", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Lesson> lessons;
}
