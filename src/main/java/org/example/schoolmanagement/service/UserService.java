package org.example.schoolmanagement.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.dto.UserCreateDto;
import org.example.schoolmanagement.dto.UserFindDto;
import org.example.schoolmanagement.entity.Role;
import org.example.schoolmanagement.entity.User;
import org.example.schoolmanagement.repository.RoleRepository;
import org.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private User createBuilderUser(UserCreateDto userDto) {
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setSecondName(userDto.getSecondName());
        user.setMiddleName(userDto.getMiddleName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createTeacher(UserCreateDto userCreateDto) {
        Role roleTeacher = roleRepository
                .findRoleByNameRole("TEACHER")
                .orElse(null);
        User teacher = createBuilderUser(userCreateDto);

        assert roleTeacher != null;

        teacher.getRoles().add(roleTeacher);
        roleTeacher.getUsers().add(teacher);

        userRepository.save(teacher);
        roleRepository.save(roleTeacher);
    }

    @Transactional
    public void createStudent(UserCreateDto userDto) {
        Role roleStudent = roleRepository
                .findRoleByNameRole("STUDENT")
                .orElse(null);
        User student = createBuilderUser(userDto);

        assert roleStudent != null;

        student.getRoles().add(roleStudent);
        roleStudent.getUsers().add(student);

        userRepository.save(student);
        roleRepository.save(roleStudent);
    }

    private UserFindDto createUserFindDtoBuilder(User user) {
        UserFindDto userDto = new UserFindDto();
        userDto.setFirstName(user.getFirstname());
        userDto.setSecondName(user.getSecondName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public List<UserFindDto> getAlTeacher() {
        return userRepository.getAllTeacher()
                .stream()
                .map(this::createUserFindDtoBuilder)
                .toList();
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public List<UserFindDto> getAllStudents() {
        return userRepository.getAllStudent()
                .stream()
                .map(this::createUserFindDtoBuilder)
                .toList();
    }
}
