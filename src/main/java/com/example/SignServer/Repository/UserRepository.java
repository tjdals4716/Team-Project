package com.example.SignServer.Repository;

import com.example.SignServer.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> getByEmail(String username);

}
