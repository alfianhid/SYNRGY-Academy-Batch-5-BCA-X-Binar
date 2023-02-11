package com.binarxbca.chapter6.repository;

import com.binarxbca.chapter6.model.enums.ERoles;
import com.binarxbca.chapter6.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERoles name);
}
