package org.example.schoolmanagement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subjects")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subject {
    @Id
    @Column(name = "id_subject")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name_subject")
    String nameSubject;

    @ManyToMany(mappedBy = "subjects")
    Set<User> users = new HashSet<>();
}
