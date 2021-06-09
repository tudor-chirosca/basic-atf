package com.vocalink.crossproduct.infrastructure.jpa.info;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseVersionRepositoryJpa extends JpaRepository<DatabaseInfo, String> {

  Optional<DatabaseInfo> findFirstByOrderByReleaseDateDesc();
}
