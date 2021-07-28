package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_SUSPEND;
import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_UNSUSPEND;
import static com.vocalink.crossproduct.domain.approval.ApprovalStatus.PENDING;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantConfigurationDto;
import com.vocalink.crossproduct.ui.facade.api.ParticipantFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.Presenter;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParticipantFacadeImpl implements ParticipantFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public PageDto<ManagedParticipantDto> getPaginated(String product, ClientType clientType,
      ManagedParticipantsSearchRequest requestDto, String requestedParticipantId) {
    log.info("Fetching managed participant id: {} for: {} from: {}", requestedParticipantId,
        clientType, product);

    final ManagedParticipantsSearchCriteria request = MAPPER.toEntity(requestDto);

    final Page<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findPaginated(request);

    participants.getItems()
        .forEach(p -> setFundedParticipants(p, product));

    if (requestDto.getQ() != null) {
      participants.getItems()
          .forEach(p -> setRoutingRecords(p, product, requestDto.getQ()));
    }

    final List<String> managedParticipantIds = participants.getItems().stream()
        .map(Participant::getBic)
        .collect(toList());

    final List<ApprovalRequestType> requestTypes = asList(PARTICIPANT_UNSUSPEND,
        PARTICIPANT_SUSPEND);

    final Map<String, Approval> approvals = getApprovals(request.getOffset(), request.getLimit(),
        product, managedParticipantIds, requestedParticipantId, requestTypes);

    return presenterFactory.getPresenter(clientType)
        .presentManagedParticipants(participants, approvals);
  }

  @Override
  public ParticipantConfigurationDto getById(String product, ClientType clientType,
      String participantId, String requestedParticipantId) {
    log.info("Fetching details of managed participant id: {} participantId: {} for: {}, from: {}",
        requestedParticipantId, participantId, clientType, product);

    final ParticipantRepository participantRepository = repositoryFactory
        .getParticipantRepository(product);

    final Participant participant = participantRepository.findById(participantId);

    final ApprovalRequestType requestType = participant.getStatus().equals(ParticipantStatus.ACTIVE)
        ? PARTICIPANT_SUSPEND
        : PARTICIPANT_UNSUSPEND;

    final Map<String, Approval> approvals = getApprovals(0, 1, product,
        singletonList(participantId), requestedParticipantId, singletonList(requestType));

    final ParticipantConfiguration configuration = participantRepository.findConfigurationById(
        participantId);

    final UserDetails userDetails = configuration.getUpdatedBy() == null ? null : repositoryFactory
        .getAuditDetailsRepository(product)
        .getUserDetailsById(participantId, configuration.getUpdatedBy())
        .orElse(UserDetails.builder()
            .participantId(participantId)
            .userId(configuration.getUpdatedBy())
            .build());

    final Presenter presenter = presenterFactory.getPresenter(clientType);
    if (participant.getParticipantType().equals(ParticipantType.FUNDED)) {

      final Participant fundingParticipant = participantRepository
          .findByBic(participant.getFundingBic())
          .orElseThrow(() -> new EntityNotFoundException(
              String.format("No participant with bic: %s found", participant.getFundingBic())));

      return presenter.presentManagedParticipantDetails(configuration, fundingParticipant,
          approvals, userDetails);
    }

    return presenter.presentManagedParticipantDetails(configuration, approvals, userDetails);
  }

  private Map<String, Approval> getApprovals(final int offset, final int limit,
      final String product,
      final List<String> participantIds, final String requestedParticipantId,
      final List<ApprovalRequestType> requestTypes) {
    log.info("Fetching approvals for participant id: {}, from: {}", requestedParticipantId,
        product);

    final Map<String, Approval> approvalParticipants = new HashMap<>();

    final ApprovalSearchCriteria approvalRequest = ApprovalSearchCriteria.builder()
        .offset(offset)
        .limit(limit)
        .participantIds(participantIds)
        .requestTypes(requestTypes)
        .statuses(singletonList(PENDING))
        .build();

    final List<Approval> approvals = repositoryFactory.getApprovalRepository(product)
        .findPaginated(approvalRequest).getItems().stream()
        .filter(
            approval -> approval.getRequestedBy().getParticipantId().equals(requestedParticipantId))
        .collect(toList());

    approvals.forEach(approval -> approvalParticipants
        .put(approval.getOriginalData().getOrDefault("id", StringUtils.EMPTY).toString(),
            approval));

    return approvalParticipants;
  }

  private void setFundedParticipants(Participant participant, String product) {
    if (ParticipantType.DIRECT_FUNDING.equals(participant.getParticipantType()) ||
        ParticipantType.FUNDING.equals(participant.getParticipantType())) {
      final List<Participant> fundedParticipants = repositoryFactory
          .getParticipantRepository(product)
          .findByConnectingPartyAndType(participant.getId(),
              ParticipantType.FUNDED.getDescription()).getItems();
      fundedParticipants.sort(comparing(Participant::getName));
      participant.setFundedParticipants(fundedParticipants);
      participant.setFundedParticipantsCount(participant.getFundedParticipants().size());
    }
  }

  private void setRoutingRecords(Participant participant, String product, String searchString) {
    if (participant.getBic() != null && participant.getBic().contains(searchString)) {
      return;
    }
    final List<RoutingRecord> routingRecords = repositoryFactory.getRoutingRepository(product)
        .findAllByBic(participant.getBic())
        .stream()
        .filter(f -> containsIgnoreCase(f.getReachableBic(), searchString))
        .collect(toList());

    participant.setReachableBics(routingRecords);
  }
}
