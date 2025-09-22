package ru.netology.cloudservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudservice.model.entity.UserEntity;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findAllByName(String name);
}
