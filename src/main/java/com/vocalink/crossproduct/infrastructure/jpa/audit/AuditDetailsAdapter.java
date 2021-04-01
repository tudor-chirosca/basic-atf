package com.vocalink.crossproduct.infrastructure.jpa.audit;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository;
import com.vocalink.crossproduct.domain.audit.AuditRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

  public static final String ZONE_ID_UTC = "UTC";
  public static final String PARTICIPANT_ID_AUDIT_DETAILS = "participantId";
  public static final String TIMESTAMP_AUDIT_DETAILS = "timestamp";
  public static final String ACTIVITY_ID_AUDIT_DETAILS = "activityId";
  public static final String USERNAME_AUDIT_DETAILS = "username";
  public static final String NAME_USER_ACTIVITY = "name";
  public static final String QUERY_BY_NAME = "select activity from UserActivityJpa as activity where activity.name = :eventName";
  public static final String EVENT_NAME_USER_ACTIVITY = "eventName";
  public static final String NOT_AVAILABLE = "n/a";

  @PersistenceContext
  private final EntityManager entityManager;
  private final AuditDetailsRepositoryJpa auditDetailsRepository;

  @Override
  public List<AuditDetails> getAuditDetailsById(String participantId) {
    return auditDetailsRepository.findAllByParticipantId(participantId)
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public List<AuditDetails> getAuditDetailsByParameters(AuditRequest parameters) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<AuditDetailsJpa> query = cb.createQuery(AuditDetailsJpa.class);
    Root<AuditDetailsJpa> details = query.from(AuditDetailsJpa.class);

    List<Predicate> predicates = new ArrayList<>();

    if (parameters.getParticipant() != null) {
      predicates
          .add(cb.equal(details.get(PARTICIPANT_ID_AUDIT_DETAILS), parameters.getParticipant()));
    }

    if (parameters.getUser() != null) {
      predicates.add(cb.equal(details.get(USERNAME_AUDIT_DETAILS), parameters.getUser()));
    }

    if (parameters.getDateFrom() != null) {
      final ZonedDateTime fromTime = ZonedDateTime
          .of(parameters.getDateFrom(), LocalTime.MIN, ZoneId.of(ZONE_ID_UTC));
      predicates.add(cb.greaterThan(details.get(TIMESTAMP_AUDIT_DETAILS), fromTime));
    }

    if (parameters.getDateTo() != null) {
      final ZonedDateTime toTime = ZonedDateTime
          .of(parameters.getDateTo(), LocalTime.MAX, ZoneId.of(ZONE_ID_UTC));
      predicates.add(cb.greaterThan(details.get(TIMESTAMP_AUDIT_DETAILS), toTime));
    }

    if (parameters.getEvent() != null) {
      predicates.add(cb.equal(details.get(ACTIVITY_ID_AUDIT_DETAILS).get(NAME_USER_ACTIVITY),
          parameters.getEvent()));
    }

    query.select(details)
        .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

    return entityManager.createQuery(query)
        .getResultList()
        .stream()
        .map(MAPPER::toEntity)
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
        .correlationId(NOT_AVAILABLE)
        .channel(event.getClient() + "<>" + event.getProduct())
        .userActivityString(eventName)
        .approvalRequestId("00000")
        .requestOrResponseEnum(NOT_AVAILABLE)
        .requestUrl(event.getRequestUrl())
        .contents(event.getContent())
        .participantId(event.getParticipantId())
        .username(userDetails.getUserId())
        .firstName(userDetails.getFirstName())
        .lastName(userDetails.getLastName())
        .build();

    auditDetailsRepository.save(detailsJpa);
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
