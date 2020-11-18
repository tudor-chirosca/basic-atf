package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.repository.FileRepository;
import com.vocalink.crossproduct.repository.ParticipantRepository;
import com.vocalink.crossproduct.repository.ReferencesRepository;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReferencesServiceFacadeImpl implements ReferencesServiceFacade {

  private final ParticipantRepository participantRepository;
  private final ReferencesRepository referencesRepository;
  private final PresenterFactory presenterFactory;
  private final FileRepository fileRepository;

  @Override
  public List<ParticipantReferenceDto> getParticipantReferences(String context,
      ClientType clientType) {
    List<Participant> participants = participantRepository.findAll(context);

    return presenterFactory.getPresenter(clientType).presentParticipantReferences(participants);
  }

  @Override
  public List<MessageDirectionReferenceDto> getMessageDirectionReferences(String context,
      ClientType clientType) {

    List<MessageDirectionReference> messageDirectionReferences = referencesRepository
        .findMessageDirectionReferences(context);
    return presenterFactory.getPresenter(clientType)
        .presentMessageDirectionReferences(messageDirectionReferences);
  }

  @Override
  public List<FileStatusesDto> getFileReferences(String context, ClientType clientType) {

    final List<FileReference> fileReferences = fileRepository.findFileReferences(context);

    return presenterFactory.getPresenter(clientType).presentFileReferences(fileReferences);
  }
}
