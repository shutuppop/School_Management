package org.example.schoolmanagement.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.dto.ClassCreateDto;
import org.example.schoolmanagement.dto.ClassFindDto;
import org.example.schoolmanagement.dto.UserFindDto;
import org.example.schoolmanagement.entity.Class;
import org.example.schoolmanagement.entity.Role;
import org.example.schoolmanagement.entity.User;
import org.example.schoolmanagement.repository.ClassRepository;
import org.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassService {
    @Autowired
    ClassRepository classRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createClass(ClassCreateDto classDto) {
        Set<User> findTeachers = classDto.getEmailTeacherList()
                .stream()
                .map(email -> userRepository.findUserByEmail(email).orElse(null))
                .collect(Collectors.toSet());
        Set<User> findStudents = classDto.getEmailStudentList()
                .stream()
                .map(email -> userRepository.findUserByEmail(email).orElse(null))
                .collect(Collectors.toSet());

        findStudents.forEach(element -> {
            if (!element.getClasses().isEmpty()) {
                Class existingClass = element.getClasses().iterator().next();
                throw new RuntimeException("Ученик " + element.getEmail() + " уже состоит в классе " +
                        existingClass.getLvlClass() + existingClass.getNameClass());
            }
        });

        Class newClass = new Class();

        newClass.setNameClass(classDto.getNameClass());
        newClass.setLvlClass(classDto.getLvlClass());

        Set<User> allClass = new HashSet<>();
        allClass.addAll(findTeachers);
        allClass.addAll(findStudents);
        newClass.setUsers(allClass);
        findTeachers.forEach(element -> element.getClasses().add(newClass));
        findStudents.forEach(element -> element.getClasses().add(newClass));

        classRepository.save(newClass);
        userRepository.saveAll(allClass);
    }

    private UserFindDto createUserFindDtoBuilder(User user) {
        UserFindDto userFindDto = new UserFindDto();
        userFindDto.setFirstName(user.getFirstname());
        userFindDto.setSecondName(user.getSecondName());
        userFindDto.setMiddleName(user.getMiddleName());
        userFindDto.setEmail(user.getEmail());
        userFindDto.setPhoneNumber(user.getPhoneNumber());
        return userFindDto;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ClassFindDto> getAllClass() {
        return classRepository.findAll()
                .stream()
                .map(classElement -> {
                    ClassFindDto classDto = new ClassFindDto();
                    classDto.setNameClass(classElement.getNameClass());
                    classDto.setLvlClass(classElement.getLvlClass());

                    List<UserFindDto> students = userRepository.getAllStudent()
                            .stream()
                            .map(this::createUserFindDtoBuilder)
                            .toList();

                    List<UserFindDto> teachers = userRepository.getAllTeacher()
                            .stream()
                            .map(this::createUserFindDtoBuilder)
                            .toList();

                    classDto.setStudents(students);
                    classDto.setTeachers(teachers);
                    return classDto;
                })
                .toList();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ClassFindDto getClassByName(Long idClass) {
        return classRepository.findClassById(idClass)
                .map(aClass -> {
                    ClassFindDto classFindDto = new ClassFindDto();
                    classFindDto.setNameClass(aClass.getNameClass());
                    classFindDto.setLvlClass(aClass.getLvlClass());
                    List<UserFindDto> students = userRepository.getAllStudent()
                            .stream()
                            .map(this::createUserFindDtoBuilder)
                            .toList();
                    List<UserFindDto> teachers = userRepository.getAllTeacher()
                            .stream()
                            .map(this::createUserFindDtoBuilder)
                            .toList();
                    classFindDto.setStudents(students);
                    classFindDto.setTeachers(teachers);
                    return classFindDto;
                })
                .orElseThrow(() -> new RuntimeException("Класс с таким id не найден."));
    }
}
