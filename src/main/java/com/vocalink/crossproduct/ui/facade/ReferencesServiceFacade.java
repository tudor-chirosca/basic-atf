package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface ReferencesServiceFacade {

  List<ParticipantReferenceDto> getParticipantReferences(String context, ClientType clientType);

  List<MessageDirectionReferenceDto> getMessageDirectionReferences(String context,
      ClientType clientType);

  List<FileStatusesDto> getFileReferences(String context, ClientType clientType);

}
