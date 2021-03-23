package com.vocalink.crossproduct.infrastructure.jpa.activities;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepositoryJpa extends JpaRepository<UserActivity, UUID> {

}
