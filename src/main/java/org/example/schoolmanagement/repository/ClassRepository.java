package org.example.schoolmanagement.repository;

import org.example.schoolmanagement.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findClassById(Long id);
}
