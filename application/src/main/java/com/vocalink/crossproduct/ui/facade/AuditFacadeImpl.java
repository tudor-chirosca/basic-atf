package com.vocalink.crossproduct.ui.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditSearchRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.ui.aspects.EventType;
import com.vocalink.crossproduct.ui.aspects.OccurringEvent;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static com.vocalink.crossproduct.infrastructure.logging.EventMarker.AUDIT_EVENT_MARKER;
import static com.vocalink.crossproduct.ui.aspects.OperationType.RESPONSE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditFacadeImpl implements AuditFacade {

  private static final String AUDIT_EVENT_LOG_MESSAGE = "Log audit event: %s type: %s";

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public List<UserDetailsDto> getUserDetails(String product, ClientType clientType,
      String participantId) {
    log.info("Fetching user details for {}", participantId);

    final List<AuditDetails> userDetails = repositoryFactory
        .getAuditDetailsRepository(product)
        .getAuditDetailsByParticipantIdAndGroupByUser(participantId);

    return presenterFactory.getPresenter(clientType)
        .presentUserDetails(userDetails);
  }

  @Override
  public PageDto<AuditDto> getAuditLogs(String product, ClientType clientType,
      AuditRequestParams parameters) {
    log.info("Fetching audit logs by {} for: {} from: {}", parameters, clientType, product);

    final AuditSearchRequest auditSearchRequest = MAPPER.toEntity(parameters);

    Page<AuditDetails> details = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsByParameters(auditSearchRequest);

    return presenterFactory.getPresenter(clientType)
        .presentAuditDetails(details);
  }

  @Override
  public List<String> getEventTypes(String product, ClientType clientType) {
    log.info("Get event types for: {} from: {}", clientType, product);
    return presenterFactory.getPresenter(clientType)
        .presentEvents(EventType.getEventsList());
  }

  @Override
  public void handleEvent(OccurringEvent occurringEvent) {
    Event event = MAPPER.toEntity(occurringEvent);
    try {
      UserDetails userDetails = getUserDetails(occurringEvent);
      repositoryFactory.getAuditDetailsRepository(event.getProduct()).logOperation(event, userDetails);
      log.info(AUDIT_EVENT_MARKER, String.format(AUDIT_EVENT_LOG_MESSAGE, occurringEvent.getEventType(),
        occurringEvent.getOperationType()), occurringEvent, userDetails);
    } catch (Exception e) {
      log.info(AUDIT_EVENT_MARKER, String.format(AUDIT_EVENT_LOG_MESSAGE,
        occurringEvent.getEventType(),
        occurringEvent.getOperationType()), occurringEvent);
    }
  }

  private UserDetails getUserDetails(OccurringEvent occurringEvent) {
    return repositoryFactory
        .getAuditDetailsRepository(occurringEvent.getProduct())
        .getAuditDetailsByParticipantId(occurringEvent.getParticipantId())
        .stream()
        .filter(d -> d.getUsername().equals(occurringEvent.getUserId()))
        .map(MAPPER::toEntity)
        .findFirst().orElseThrow(() -> new EntityNotFoundException(
                String.format("No user details found matching for participantId: %s and userId: %s",
                        occurringEvent.getParticipantId(), occurringEvent.getUserId())
        ));
  }

  @Override
  public AuditDetailsDto getAuditDetails(String product, ClientType clientType, String id) {
    log.info("Fetching audit log details for id: {} for: {} from: {}", id, clientType, product);
    final AuditDetails request = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsById(id);

    final AuditDetails response = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsByCorrelationId(request.getCorrelationId()).stream()
        .filter(r -> RESPONSE.name().equals(r.getRequestOrResponseEnum()))
        .findFirst()
        .orElseThrow(() -> new InfrastructureException("Missing response for request id: " + id));

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(request.getParticipantId());

    return presenterFactory.getPresenter(clientType)
        .presentAuditDetails(request, response, participant);
  }
}
