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
@Table(name = "classes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Class {
    @Id
    @Column(name = "id_class")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name_class")
    String nameClass;

    @Column(name = "level_class")
    Integer lvlClass;

    @ManyToMany(mappedBy = "classes")
    Set<User> users = new HashSet<>();
}
