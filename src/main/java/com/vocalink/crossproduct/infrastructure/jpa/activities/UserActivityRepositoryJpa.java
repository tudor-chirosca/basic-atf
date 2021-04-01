package com.vocalink.crossproduct.infrastructure.jpa.activities;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserActivityRepositoryJpa extends JpaRepository<UserActivityJpa, UUID> {

  @Query(value = "select name from UserActivityJpa")
  List<String> findAllEventTypes();

  Optional<UserActivityJpa> findByName(String name);
}
