package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.facade.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InputOutputFacadeImpl implements InputOutputFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public IODashboardDto getInputOutputDashboard(String product, ClientType clientType,
      LocalDate date) {
    log.info("Fetching IO Dashboard from: {}", product);

    List<Participant> participants = repositoryFactory.getParticipantRepository(product).findAll();
    List<ParticipantIOData> ioData = repositoryFactory.getParticipantsIODataRepository(product)
        .findByTimestamp(date);

    return presenterFactory.getPresenter(clientType).presentInputOutput(participants, ioData, date);
  }

  @Override
  public IODetailsDto getInputOutputDetails(String product, ClientType clientType, LocalDate date,
      String participantId) {

    log.info("Fetching IO Details for participantId: {} from: {}", participantId, product);

    Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    IODetails ioDetails = repositoryFactory.getParticipantsIODataRepository(product)
        .findIODetailsFor(participantId, date)
        .stream().findFirst().orElseThrow(
            () -> new EntityNotFoundException(
                "There are no IO Details for participant with id: " + participantId
                    + " for date:" + date.toString()));

    return presenterFactory.getPresenter(clientType).presentIoDetails(participant, ioDetails, date);
  }
}
