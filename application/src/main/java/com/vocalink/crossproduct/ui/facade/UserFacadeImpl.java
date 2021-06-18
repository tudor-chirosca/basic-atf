package com.vocalink.crossproduct.ui.facade;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.permission.UIPermission;
import com.vocalink.crossproduct.domain.role.Role.Function;
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.facade.api.UserFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public CurrentUserInfoDto getCurrentUserInfo(String product, ClientType clientType,
      String userId, String participantId, List<String> roles) {
    log.info("Fetching user info by id: {} participantId: {} for: {} from: {}", userId, participantId, clientType, product);

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    final List<UIPermission> uiPermissions = repositoryFactory.getUIPermissionRepository()
        .findByRolesAndParticipantType(roles.stream().map(Function::valueOf).collect(toList()),
            participant.getParticipantType());

    final AuditDetails auditDetails = repositoryFactory.getAuditDetailsRepository(product)
        .getAuditDetailsByUsername(userId);

    return presenterFactory.getPresenter(clientType)
        .presentCurrentUserInfo(participant, uiPermissions, auditDetails);
  }
}
