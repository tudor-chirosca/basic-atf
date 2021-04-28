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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

  public static final String ZONE_ID_UTC = "UTC";
  public static final String QUERY_BY_NAME = "select activity from UserActivityJpa as activity where activity.name = :eventName";
  public static final String EVENT_NAME_USER_ACTIVITY = "eventName";
  public static final String CUSTOMER = "P27-SEK";

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
  public com.vocalink.crossproduct.domain.Page<AuditDetails> getAuditDetailsByParameters(
      AuditSearchRequest params) {
    final Sort sortBy = AuditDetailsJpa.getSortBy(params.getSort());
    final Page<AuditDetailsView> allByParameters = auditDetailsRepository
        .getAllByParameters(
            params.getDateFrom(),
            params.getDateTo(),
            params.getParticipant(),
            params.getUser(),
            params.getEvents(),
            new OffsetBasedPageRequest(params.getLimit(), params.getOffset(), sortBy));

    final List<AuditDetails> auditDetails = allByParameters
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());

    return new com.vocalink.crossproduct.domain.Page<>(
        (int) allByParameters.getTotalElements(), auditDetails);
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
        .ipsSuiteApplicationName(event.getProduct())
        .customer(CUSTOMER)
        .build();

    auditDetailsRepository.save(detailsJpa);
  }

  @Override
  public AuditDetails getAuditDetailsByUsername(String name) {
    final AuditDetails auditDetails = MAPPER.toEntity(auditDetailsRepository.findByUsername(name));
    if (auditDetails == null) {
      throw new EntityNotFoundException("No auditDetails with user name: " + name);
    }
    return auditDetails;
  }

  @Override
  public List<AuditDetails> getAuditDetailsByParticipantIdAndGroupByUser(String id) {
    return auditDetailsRepository.findByParticipantId(id).stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public List<AuditDetails> getAuditDetailsByCorrelationId(String id) {
    return auditDetailsRepository.findByCorrelationId(id).stream()
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
