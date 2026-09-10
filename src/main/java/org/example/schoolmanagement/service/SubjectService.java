package org.example.schoolmanagement.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.dto.SubjectCreateDto;
import org.example.schoolmanagement.dto.SubjectFindDto;
import org.example.schoolmanagement.dto.UserFindDto;
import org.example.schoolmanagement.entity.Subject;
import org.example.schoolmanagement.entity.User;
import org.example.schoolmanagement.repository.SubjectRepository;
import org.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public void createSubject(SubjectCreateDto subjectDto) {
        List<User> allTeachers = userRepository.getAllTeacher();
        User findTeacher = allTeachers
                .stream()
                .filter(user -> user.getEmail().equals(subjectDto.getEmailTeacher()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден."));

        Subject subject = new Subject();
        subject.setNameSubject(subjectDto.getNameSubject());

        subject.getUsers().add(findTeacher);
        findTeacher.getSubjects().add(subject);

        subjectRepository.save(subject);
        userRepository.save(findTeacher);
    }

    @Transactional
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    public List<SubjectFindDto> getAllSubject() {
        return subjectRepository.findAll()
                .stream()
                .map(subject -> {
                    SubjectFindDto subjectFindDto = new SubjectFindDto();
                    subjectFindDto.setNameSubject(subject.getNameSubject());
                    List<UserFindDto> allTeachers = subject.getUsers().stream().map(user -> {
                        UserFindDto teachers = new UserFindDto();
                        teachers.setFirstName(user.getFirstname());
                        teachers.setSecondName(user.getSecondName());
                        teachers.setMiddleName(user.getMiddleName());
                        teachers.setEmail(user.getEmail());
                        teachers.setPhoneNumber(user.getPhoneNumber());
                        return teachers;
                    }).toList();
                    subjectFindDto.setTeachers(allTeachers);
                    return subjectFindDto;
                })
                .toList();
    }
}
