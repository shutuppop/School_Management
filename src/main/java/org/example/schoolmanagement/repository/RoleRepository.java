package org.example.schoolmanagement.repository;

import org.example.schoolmanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByNameRole(String nameRole);
}
