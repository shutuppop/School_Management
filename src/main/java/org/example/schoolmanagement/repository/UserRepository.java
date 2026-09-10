package org.example.schoolmanagement.repository;

import org.example.schoolmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.nameRole = 'TEACHER'")
    List<User> getAllTeacher();

    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.nameRole = 'STUDENT'")
    List<User> getAllStudent();
}
