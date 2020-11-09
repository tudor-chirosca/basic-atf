package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.files.FileReference;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReferencesServiceFacadeImpl implements ReferencesServiceFacade {

  private final ParticipantRepository participantRepository;
  private final ReferencesRepository referencesRepository;
  private final PresenterFactory presenterFactory;
  private final FileRepository fileRepository;

  @Override
  public List<ParticipantReferenceDto> getParticipants(String context) {
    log.info("Fetching all reference participants {} ... ", context);
    return participantRepository.findAll(context).stream()
        .map(p -> new ParticipantReferenceDto(p.getBic(), p.getName()))
        .sorted(Comparator.comparing(ParticipantReferenceDto::getName))
        .collect(Collectors.toList());
  }

  @Override
  public List<MessageDirectionReferenceDto> getMessageDirectionReferences(String context,
      ClientType clientType) {

    List<MessageDirectionReference> messageDirectionReferences = referencesRepository
        .getMessageDirectionReferences(context);
    
    return presenterFactory.getPresenter(clientType)
        .getMessageDirectionReferences(messageDirectionReferences);
  }

  @Override
  public List<FileStatusesDto> getFileReferences(String context, ClientType clientType) {
    log.info("Fetching all file references from: {}", context);

    final List<FileReference> fileReferences = fileRepository.findFileReferences(context);

    return presenterFactory.getPresenter(clientType).presentFileReferences(fileReferences);
  }
}
