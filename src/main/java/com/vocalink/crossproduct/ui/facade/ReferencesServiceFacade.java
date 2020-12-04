package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;
import java.util.List;

public interface ReferencesServiceFacade {

  List<ParticipantReferenceDto> getParticipantReferences(String context, ClientType clientType);

  List<MessageDirectionReferenceDto> getMessageDirectionReferences(String context,
      ClientType clientType);

  List<FileStatusesTypeDto> getFileReferences(String context, ClientType clientType, String enquiryType);

  List<CycleDto> getCyclesByDate(String context, ClientType clientType, LocalDate date);
}
