package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface ReferenceServiceFacade {

  List<ParticipantReferenceDto> getParticipants(String context);

  List<FileStatusesDto> getFileReferences(String context, ClientType clientType);
}
