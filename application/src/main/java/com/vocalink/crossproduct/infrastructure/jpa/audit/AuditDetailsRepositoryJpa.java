package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.domain.audit.AuditDetails;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuditDetailsRepositoryJpa extends JpaRepository<AuditDetailsJpa, UUID> {

  List<AuditDetailsJpa> findAllByParticipantId(String id);

  @Query("SELECT "
      + "distinct "
      + "a.username AS username, "
      + "a.firstName AS firstName, "
      + "a.lastName AS lastName, "
      + "a.participantId AS participantId "
      + "FROM AuditDetailsJpa AS a "
      + "WHERE a.username = :name ")
  UserDetailsView findByUsername(String name);

  @Query("SELECT"
      + "    a.id AS id,"
      + "    a.activityId.id AS activityId,"
      + "    a.serviceId AS serviceId,"
      + "    a.timestamp AS timestamp,"
      + "    a.correlationId AS correlationId,"
      + "    a.requestOrResponseEnum AS requestOrResponseEnum,"
      + "    a.contents AS requestContent,"
      + "    a.participantId AS participantId,"
      + "    a.username AS username,"
      + "    a.firstName AS firstName,"
      + "    a.lastName AS lastName,"
      + "    b.contents AS responseContent"
      + "    FROM AuditDetailsJpa a"
      + "    LEFT JOIN AuditDetailsJpa b ON a.correlationId = b.correlationId"
      + "    WHERE (:dateFrom IS NULL OR a.timestamp > :dateFrom)"
      + "    AND a.requestOrResponseEnum = 'REQUEST'"
      + "    AND b.requestOrResponseEnum = 'RESPONSE'"
      + "    AND (:dateTo IS NULL OR a.timestamp < :dateTo)"
      + "    AND (:participantId IS NULL OR a.participantId = :participantId)"
      + "    AND (:userId IS NULL OR a.username = :userId)"
      + "    AND (COALESCE(:events) IS NULL OR a.userActivityString IN :events)")
  Page<AuditDetailsView> getAllByParameters(ZonedDateTime dateFrom, ZonedDateTime dateTo,
      String participantId, String userId, List<String> events, Pageable pageable);

  @Query("SELECT "
      + "a.username AS username, "
      + "a.firstName AS firstName, "
      + "a.lastName AS lastName, "
      + "a.participantId AS participantId "
      + "FROM AuditDetailsJpa AS a "
      + "WHERE a.participantId = :id "
      + "GROUP BY a.username, a.firstName, a.lastName, a.participantId")
  List<UserDetailsView> findByParticipantId(String id);

  List<AuditDetailsJpa> findByCorrelationId(String id);
}
