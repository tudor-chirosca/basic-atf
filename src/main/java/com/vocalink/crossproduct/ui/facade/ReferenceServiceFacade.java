package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.util.List;

public interface ReferenceServiceFacade {

  List<ParticipantReferenceDto> getParticipants(String context);
}
