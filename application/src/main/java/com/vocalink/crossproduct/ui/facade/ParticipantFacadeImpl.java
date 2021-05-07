package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_SUSPEND;
import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_UNSUSPEND;
import static com.vocalink.crossproduct.domain.approval.ApprovalStatus.PENDING;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ParticipantFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParticipantFacadeImpl implements ParticipantFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public PageDto<ManagedParticipantDto> getPaginated(String product, ClientType clientType,
      ManagedParticipantsSearchRequest requestDto) {

    log.info("Fetching managed participant from: {}", product);

    final ManagedParticipantsSearchCriteria request = MAPPER.toEntity(requestDto);

    final Page<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findPaginated(request);

    final Page<Participant> managedParticipants = new Page<>(participants.getTotalResults(),
        participants.getItems().stream()
            .peek(p -> {
              setFundedParticipants(p, product);
              setRoutingRecords(p, product, requestDto.getQ());
            })
            .collect(toList()));

    return presenterFactory.getPresenter(clientType).presentManagedParticipants(managedParticipants,
        getApprovals(managedParticipants.getItems(), request, product));
  }

  private Map<String, Approval> getApprovals(final List<Participant> managedParticipants,
      final ManagedParticipantsSearchCriteria request, final String product) {

    final Map<String, Approval> approvalParticipants = new HashMap<>();

    final List<String> managedParticipantIds = managedParticipants.stream()
        .map(Participant::getBic)
        .collect(toList());

    final ApprovalSearchCriteria approvalRequest = ApprovalSearchCriteria.builder()
        .offset(request.getOffset())
        .limit(request.getLimit())
        .participantIds(managedParticipantIds)
        .requestTypes(asList(PARTICIPANT_UNSUSPEND, PARTICIPANT_SUSPEND))
        .statuses(asList(PENDING))
        .build();

    final List<Approval> approvals = repositoryFactory.getApprovalRepository(product)
        .findPaginated(approvalRequest).getItems();

    approvals.forEach(approval -> approval.getParticipantIds()
        .forEach(id -> approvalParticipants.put(id, approval)));

    return approvalParticipants;
  }

  @Override
  public ManagedParticipantDetailsDto getById(String product, ClientType clientType,
      String bic) {
    log.info("Fetching managed participant details for: {}, from: {}", bic, product);

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(bic);
    setFundedParticipants(participant, product);

    final ApprovalRequestType requestType = participant.getStatus().equals(ParticipantStatus.ACTIVE)
        ? PARTICIPANT_SUSPEND
        : PARTICIPANT_UNSUSPEND;

    final ApprovalSearchCriteria criteria = ApprovalSearchCriteria.builder()
        .limit(1)
        .statuses(singletonList(PENDING))
        .requestTypes(asList(requestType))
        .participantIds(singletonList(bic))
        .build();

    final List<Approval> approvals = repositoryFactory.getApprovalRepository(product)
        .findPaginated(criteria).getItems();

    final Account account = repositoryFactory.getAccountRepository(product)
        .findByPartyCode(bic);

    final ParticipantConfiguration configuration = repositoryFactory
        .getParticipantRepository(product)
        .findConfigurationById(bic);

    if (participant.getParticipantType().equals(ParticipantType.FUNDED)) {
      Participant fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(participant.getFundingBic());

      return presenterFactory.getPresenter(clientType)
          .presentManagedParticipantDetails(participant, configuration, fundingParticipant,
              account, approvals);
    }

    return presenterFactory.getPresenter(clientType)
        .presentManagedParticipantDetails(participant, configuration, account, approvals);
  }

  private void setFundedParticipants(Participant participant, String product) {
    if (participant.getParticipantType().equals(ParticipantType.DIRECT_FUNDING) ||
        participant.getParticipantType().equals(ParticipantType.FUNDING)) {
      List<Participant> fundedParticipants = repositoryFactory.getParticipantRepository(product)
          .findByConnectingPartyAndType(participant.getId(),
              ParticipantType.FUNDED.getDescription()).getItems();
      fundedParticipants.sort(comparing(Participant::getName));
      participant.setFundedParticipants(fundedParticipants);
      participant.setFundedParticipantsCount(participant.getFundedParticipants().size());
    }
  }

  private void setRoutingRecords(Participant participant, String product, String searchString) {
    if (nonNull(searchString)) {
      List<RoutingRecord> routingRecords = repositoryFactory.getRoutingRepository(product)
          .findAllByBic(participant.getBic())
          .stream().filter(f -> containsIgnoreCase(f.getReachableBic(), searchString))
          .collect(toList());
      participant.setReachableBics(routingRecords);
    }
  }
}
