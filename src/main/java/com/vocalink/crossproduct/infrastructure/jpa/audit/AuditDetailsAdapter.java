package com.vocalink.crossproduct.infrastructure.jpa.audit;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

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
  public List<AuditDetails> getAuditDetailsByParameters(AuditRequestParams parameters) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<AuditDetailsJpa> query = cb.createQuery(AuditDetailsJpa.class);
    Root<AuditDetailsJpa> details = query.from(AuditDetailsJpa.class);

    List<Predicate> predicates = new ArrayList<>();

    if (parameters.getParticipant() != null) {
      predicates.add(cb.equal(details.get("participantId"), parameters.getParticipant()));
    }

    if (parameters.getUser() != null) {
      predicates.add(cb.equal(details.get("username"), parameters.getUser()));
    }

    if (parameters.getDateFrom() != null) {
      final ZonedDateTime fromTime = ZonedDateTime
          .of(parameters.getDateFrom(), LocalTime.MIN, ZoneId.of("UTC"));
      predicates.add(cb.greaterThan(details.get("timestamp"), fromTime));
    }

    if (parameters.getDateTo() != null) {
      final ZonedDateTime toTime = ZonedDateTime
          .of(parameters.getDateTo(), LocalTime.MAX, ZoneId.of("UTC"));
      predicates.add(cb.greaterThan(details.get("timestamp"), toTime));
    }

    if (parameters.getEvent() != null) {
      predicates.add(cb.equal(details.get("activityId").get("name"), parameters.getEvent()));
    }

    query.select(details)
        .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));

    return entityManager.createQuery(query)
        .getResultList()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
