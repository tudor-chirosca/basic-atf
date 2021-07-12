package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.ZonedDateTime;
import java.util.List;

public interface ReferencesServiceFacade {

  List<ParticipantReferenceDto> getParticipantReferences(String product, ClientType clientType,
      String destination);

  List<MessageDirectionReferenceDto> getMessageDirectionReferences(String product,
      ClientType clientType);

  List<ReasonCodeReferenceDto> getReasonCodeReferences(String product, ClientType clientType,
      String enquiryType);

  List<DayCycleDto> getDayCyclesByDate(String product, ClientType clientType, ZonedDateTime date,
      boolean settled);
}
