package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        .getAuditDetailsById(participantId);

    return presenterFactory.getPresenter(clientType)
        .presentUserDetails(auditDetails);
  }
}
