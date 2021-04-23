package com.vocalink.crossproduct.infrastructure.jpa.audit;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository;
import com.vocalink.crossproduct.domain.audit.AuditSearchRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

  public static final String ZONE_ID_UTC = "UTC";
  public static final String QUERY_BY_NAME = "select activity from UserActivityJpa as activity where activity.name = :eventName";
  public static final String EVENT_NAME_USER_ACTIVITY = "eventName";
  public static final String OPERATION_TYPE_REQUEST = "REQUEST";

  @PersistenceContext
  private final EntityManager entityManager;
  private final AuditDetailsRepositoryJpa auditDetailsRepository;

  @Override
  public List<AuditDetails> getAuditDetailsByParticipantId(String participantId) {
    return auditDetailsRepository.findAllByParticipantId(participantId)
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public AuditDetails getAuditDetailsById(String id) {
    try {
      final UUID uuid = UUID.fromString(id);
      final AuditDetailsJpa detailsJpa = auditDetailsRepository.findById(uuid)
          .orElseThrow(() -> new EntityNotFoundException("No audit details found by id: " + id));

      return MAPPER.toEntity(detailsJpa);
    } catch (IllegalArgumentException e) {
      throw new InfrastructureException("Invalid audit id: " + id, e, null);
    }
  }

  @Override
  public List<AuditDetails> getAuditDetailsByParameters(AuditSearchRequest params) {
    final Sort sortBy = AuditDetailsJpa.getSortBy(params.getSort());
    return auditDetailsRepository
        .getAllByParameters(
            params.getDateFrom(),
            params.getDateTo(),
            params.getParticipant(),
            params.getUser(),
            params.getEvents(),
            new OffsetBasedPageRequest(params.getLimit(), params.getOffset(), sortBy))
        .stream()
        .map(MAPPER::toEntity)
        .filter(e -> OPERATION_TYPE_REQUEST.equals(e.getRequestOrResponseEnum()))
        .collect(toList());
  }

  public void logOperation(Event event, UserDetails userDetails) {
    final String eventName = event.getEventType();

    final UserActivityJpa activity = findActivityByName(eventName)
        .orElseThrow(() -> new EntityNotFoundException(
            "User activity not found for event name: " + eventName));

    final AuditDetailsJpa detailsJpa = AuditDetailsJpa.builder()
        .id(UUID.randomUUID())
        .activityId(activity)
        .timestamp(ZonedDateTime.now(ZoneId.of(ZONE_ID_UTC)))
        .correlationId(event.getCorrelationId())
        .requestOrResponseEnum(event.getOperationType())
        .channel(event.getClient())
        .userActivityString(eventName)
        .requestUrl(event.getRequestUrl())
        .contents(event.getContent())
        .participantId(event.getParticipantId())
        .username(userDetails.getUserId())
        .firstName(userDetails.getFirstName())
        .lastName(userDetails.getLastName())
        .build();

    auditDetailsRepository.save(detailsJpa);
  }

  @Override
  public AuditDetails getAuditDetailsByUsername(String name) {
    return MAPPER.toEntity(auditDetailsRepository.findByUsername(name)
        .orElseThrow(() -> new EntityNotFoundException(
            "No audit details found by user name: " + name)));
  }

  @Override
  public List<AuditDetails> getGetUserReferencesByParticipantId(String id) {
    return auditDetailsRepository.findUserDetailsByParticipantId(id).stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  private Optional<UserActivityJpa> findActivityByName(String eventName) {
    final TypedQuery<UserActivityJpa> query = entityManager
        .createQuery(QUERY_BY_NAME, UserActivityJpa.class);
    query.setParameter(EVENT_NAME_USER_ACTIVITY, eventName);

    try {
      return Optional.ofNullable(query.getSingleResult());
    } catch (PersistenceException e) {
      return Optional.empty();
    }
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
