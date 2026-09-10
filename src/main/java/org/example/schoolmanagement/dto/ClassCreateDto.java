package org.example.schoolmanagement.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassCreateDto {
    String nameClass;
    Integer lvlClass;
    List<String> emailTeacherList;
    List<String> emailStudentList;
}
