package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalFacadeImpl implements ApprovalFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public ApprovalDetailsDto getApprovalDetailsById(String product, ClientType clientType,
      String id) {
    log.info("Fetching approval details by id: {} for: {} from: {}", id, clientType, product);

    final Approval approval = repositoryFactory.getApprovalRepository(product).findByJobId(id);

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll().getItems();

    return presenterFactory.getPresenter(clientType).presentApprovalDetails(approval, participants);
  }

  @Override
  public PageDto<ApprovalDetailsDto> getApprovals(String product, ClientType clientType,
      ApprovalSearchRequest requestDto) {
    log.info("Fetching approvals for: {} from: {}", clientType, product);

    final ApprovalSearchCriteria request = MAPPER.toEntity(requestDto);

    final Page<Approval> approvals = repositoryFactory.getApprovalRepository(product)
        .findPaginated(request);

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll().getItems();

    return presenterFactory.getPresenter(clientType).presentApproval(approvals, participants);
  }

  @Override
  public ApprovalDetailsDto requestApproval(String product, ClientType clientType,
      ApprovalChangeRequest requestDto) {

    log.info("Creating approval by type: {} for: {} from: {}", requestDto.getRequestType(), clientType, product);

    final ApprovalChangeCriteria request = MAPPER.toEntity(requestDto);

    final Approval approval = repositoryFactory.getApprovalRepository(product)
        .requestApproval(request);

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll().getItems();

    return presenterFactory.getPresenter(clientType).presentApprovalDetails(approval, participants);
  }

  @Override
  public ApprovalConfirmationResponseDto submitApprovalConfirmation(String product, ClientType clientType,
      ApprovalConfirmationRequest requestDto, String id) {
    log.info("Sending confirmation on approval id: {} for: {} from: {}", id, clientType, product);

    final ApprovalConfirmation request = MAPPER.toEntity(requestDto, id);

    final ApprovalConfirmationResponse response = serviceFactory.getApprovalService(product)
        .submitApprovalConfirmation(request);

    return presenterFactory.getPresenter(clientType).presentApprovalResponse(response);
  }

  @Override
  public List<ApprovalUserDto> findRequestedDetails(String product, ClientType clientType) {
    log.info("Fetching user details of requested approvals for: {} from: {}", clientType, product);
    final List<UserDetails> userDetails = repositoryFactory.getApprovalRepository(product)
        .findRequestedDetails();
    return presenterFactory.getPresenter(clientType).presentRequestedDetails(userDetails);
  }

  @Override
  public List<ApprovalRequestType> findApprovalRequestTypes(String product, ClientType clientType) {
    log.info("Fetching approvals request types for: {} from: {}", clientType, product);
    return repositoryFactory.getApprovalRepository(product)
        .findApprovalRequestTypes();
  }
}
