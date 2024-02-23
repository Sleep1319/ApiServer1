package com.api_board.restapiboard.repository;

import com.api_board.restapiboard.domain.member.Role;
import com.api_board.restapiboard.domain.member.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
}
