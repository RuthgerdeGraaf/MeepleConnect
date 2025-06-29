package com.meepleconnect.boardgamesapi.repositories;

import com.meepleconnect.boardgamesapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRoleNameIn(List<String> names);
}