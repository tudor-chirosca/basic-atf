package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalFacadeImpl implements ApprovalFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public ApprovalDetailsDto getApprovalDetailsById(String product, ClientType clientType,
      String id) {
    log.info("Fetching approval details from: {}", product);

    final Approval approval = repositoryFactory.getApprovalRepository(product).findByJobId(id);

    return presenterFactory.getPresenter(clientType).presentApprovalDetails(approval);
  }

  @Override
  public PageDto<ApprovalDetailsDto> getApprovals(String product, ClientType clientType,
      ApprovalSearchRequest requestDto) {
    log.info("Fetching approvals from: {}", product);

    final ApprovalSearchCriteria request = MAPPER.toEntity(requestDto);

    final Page<Approval> approvals = repositoryFactory.getApprovalRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentApproval(approvals);
  }
}
