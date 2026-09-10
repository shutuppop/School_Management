package org.example.schoolmanagement.repository;

import org.example.schoolmanagement.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
