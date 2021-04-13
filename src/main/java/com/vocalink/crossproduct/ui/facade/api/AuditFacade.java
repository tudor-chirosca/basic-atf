package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.aspects.OccurringEvent;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface AuditFacade {

  List<UserDetailsDto> getUserDetails(String context, ClientType clientType, String participantId);

  Page<AuditDto> getAuditLogs(String context, ClientType clientType, AuditRequestParams parameters);

  List<String> getEventTypes(String context, ClientType clientType);

  void handleEvent(OccurringEvent event);

  AuditDetailsDto getAuditDetails(String context, ClientType clientType, String serviceId);
}
