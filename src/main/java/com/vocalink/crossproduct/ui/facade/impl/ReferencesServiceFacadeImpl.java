package com.vocalink.crossproduct.ui.facade.impl;

import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferencesServiceFacadeImpl implements ReferencesServiceFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public List<ParticipantReferenceDto> getParticipantReferences(String product,
      ClientType clientType) {
    final List<ParticipantReference> participantReferences = repositoryFactory
        .getParticipantRepository(product)
        .findAll().stream()
        .map(MAPPER::toReference).collect(toList());

    return presenterFactory.getPresenter(clientType)
        .presentParticipantReferences(participantReferences);
  }

  @Override
  public List<MessageDirectionReferenceDto> getMessageDirectionReferences(String product,
      ClientType clientType) {

    final List<MessageDirectionReference> messageDirectionReferences = repositoryFactory
        .getReferencesRepository(product).findAll();
    return presenterFactory.getPresenter(clientType)
        .presentMessageDirectionReferences(messageDirectionReferences);
  }

  @Override
  public List<FileStatusesTypeDto> getFileReferences(String product, ClientType clientType,
      String enquiryType) {
    final List<FileReference> fileReferences = repositoryFactory.getFileRepository(product)
        .findFileReferences(enquiryType);

    return presenterFactory.getPresenter(clientType).presentFileReferencesFor(fileReferences);
  }

  @Override
  public List<CycleDto> getCyclesByDate(String product, ClientType clientType,
      LocalDate date) {
    final List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findByDate(date);

    return presenterFactory.getPresenter(clientType).presentCycleDateReferences(cycles);
  }
}
