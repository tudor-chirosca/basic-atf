package com.vocalink.crossproduct.infrastructure.jpa.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditDetailsRepositoryJpa extends JpaRepository<AuditDetails, UUID> {

}
