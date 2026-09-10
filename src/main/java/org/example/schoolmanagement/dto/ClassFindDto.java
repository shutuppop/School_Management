package org.example.schoolmanagement.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassFindDto {
    String nameClass;
    Integer lvlClass;
    List<UserFindDto> students;
    List<UserFindDto> teachers;
}
