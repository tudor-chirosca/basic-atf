package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantConfigurationDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface ParticipantFacade {

  PageDto<ManagedParticipantDto> getPaginated(String product, ClientType clientType,
      ManagedParticipantsSearchRequest requestDto, String requestedParticipantId);

  ParticipantConfigurationDto getById(String product, ClientType clientType, String participantId,
      String requestedParticipantId);
}
