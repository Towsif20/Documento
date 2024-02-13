package com.towsif.Documento.repository;

import com.towsif.Documento.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>
{
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findByName(String name);

    UserEntity findFirstByName(String name);
}
