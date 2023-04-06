package com.example.vs.Repository;

import com.example.vs.Entity.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByUsernameContaining(String name);

    @Query(value = "UPDATE users SET role = replace(role, 'ROLE_USER', 'ROLE_USER,ROLE_EDITOR') WHERE id = :id returning role", nativeQuery = true)
    String updateRole(Long id);
}
