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
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

  private final AuditDetailsRepositoryJpa auditDetailsRepository;
  private final Clock clock;

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
            params.getResponseContent(),
            params.getEvents(),
            new OffsetBasedPageRequest(params.getLimit(), params.getOffset(), sortBy));

    final List<AuditDetails> auditDetails = allByParameters
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());

    return new com.vocalink.crossproduct.domain.Page<>(
        (int) allByParameters.getTotalElements(), auditDetails);
  }

  @Transactional
  public void logOperation(Event event, UserDetails userDetails) {

    final AuditDetailsJpa detailsJpa = AuditDetailsJpa.builder()
        .id(UUID.randomUUID())
        .activityName(event.getEventType())
        .timestamp(ZonedDateTime.now(clock))
        .correlationId(event.getCorrelationId())
        .requestOrResponseEnum(event.getOperationType())
        .channel(event.getClient())
        .requestUrl(event.getRequestUrl())
        .contents(event.getContent())
        .participantId(event.getParticipantId())
        .username(userDetails.getUserId())
        .firstName(userDetails.getFirstName())
        .lastName(userDetails.getLastName())
        .ipsSuiteApplicationName(event.getProduct())
        .customer(event.getCustomer())
        .approvalRequestId(event.getApprovalRequestId())
        .ipAddress(event.getIpAddress())
        .userRoleList(event.getUserRoleList())
        .scheme(event.getScheme())
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

  @Override
  public Optional<UserDetails> getUserDetailsById(String participantId, String userId) {
    return auditDetailsRepository.findAllByParticipantId(participantId)
        .stream()
        .filter(d -> d.getUsername().equals(userId))
        .map(MAPPER::toUserEntity)
        .findFirst();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
