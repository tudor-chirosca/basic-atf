package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface AuditFacade {

  List<UserDetailsDto> getUserDetails(String context, ClientType clientType, String participantId);
}
