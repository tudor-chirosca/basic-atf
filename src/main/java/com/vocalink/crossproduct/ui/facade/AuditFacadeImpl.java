package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserActivity;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.aspects.OccurringEvent;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditFacadeImpl implements AuditFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public List<UserDetailsDto> getUserDetails(String product, ClientType clientType,
      String participantId) {
    log.info("Fetching user details for {}", participantId);

    final List<AuditDetails> auditDetails = repositoryFactory
        .getAuditDetailsRepository(product)
        .getAuditDetailsByParticipantId(participantId);

    return presenterFactory.getPresenter(clientType)
        .presentUserDetails(auditDetails);
  }

  @Override
  public Page<AuditDto> getAuditLogs(String product, ClientType clientType,
      AuditRequestParams parameters) {
    log.info("Fetching audit logs by {}", parameters);

    final AuditRequest auditRequest = MAPPER.toEntity(parameters);

    List<AuditDetails> details = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsByParameters(auditRequest);

    final List<UUID> activityIds = details.stream().map(AuditDetails::getActivityId)
        .collect(toList());

    final List<UserActivity> activities = repositoryFactory.getUserActivityRepository(product)
        .getActivitiesByIds(activityIds);

    return presenterFactory.getPresenter(clientType)
        .presentAuditDetails(details, activities);
  }

  @Override
  public List<String> getEventTypes(String product, ClientType clientType) {
    List<String> events = repositoryFactory.getUserActivityRepository(product)
        .getEventTypes();

    return presenterFactory.getPresenter(clientType)
        .presentEvents(events);
  }

  @Override
  @Transactional
  public void handleEvent(OccurringEvent occurringEvent) {
    Event event = MAPPER.toEntity(occurringEvent);

    final UserDetails userDetails = repositoryFactory
        .getAuditDetailsRepository(occurringEvent.getProduct())
        .getAuditDetailsByParticipantId(occurringEvent.getParticipantId())
        .stream()
        .filter(d -> d.getUsername().equals(occurringEvent.getUserId()))
        .map(MAPPER::toEntity)
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException(
            "No user details found by id: " + occurringEvent.getUserId()));

    repositoryFactory.getAuditDetailsRepository(event.getProduct())
        .logOperation(event, userDetails);
  }

  @Override
  public AuditDetailsDto getAuditDetails(String product, ClientType clientType, String serviceId) {
    final AuditDetails details = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsById(serviceId);

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(details.getParticipantId());

    return presenterFactory.getPresenter(clientType)
        .presentAuditDetails(details, participant);
  }
}
