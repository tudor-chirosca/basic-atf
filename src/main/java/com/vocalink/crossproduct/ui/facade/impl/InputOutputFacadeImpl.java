package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.domain.io.IODetailsRepository;
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.facade.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.Presenter;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputOutputFacadeImpl implements InputOutputFacade {

  private final ParticipantIODataRepository participantIODataRepository;
  private final IODetailsRepository ioDetailsRepository;
  private final ParticipantRepository participantRepository;
  private final PresenterFactory presenterFactory;

  @Override
  public IODashboardDto getInputOutputDashboard(String context, ClientType clientType,
      LocalDate date) {
    List<Participant> participants = participantRepository.findAll(context);
    List<ParticipantIOData> ioData = participantIODataRepository.findByTimestamp(context, date);

    Presenter presenter = presenterFactory.getPresenter(clientType);
    return presenter.presentInputOutput(participants, ioData, date);
  }

  @Override
  public IODetailsDto getInputOutputDetails(String context, ClientType clientType, LocalDate date,
      String participantId) {
    Participant participant = participantRepository.findByParticipantId(context, participantId);

    IODetails ioDetails = ioDetailsRepository.findIODetailsFor(context, participantId, date)
        .stream().findFirst().orElseThrow(
            () -> new EntityNotFoundException(
                "There aro no IO Details for participant with id: " + participantId
                    + " for date:" + date.toString()));

    Presenter presenter = presenterFactory.getPresenter(clientType);
    return presenter.presentIoDetails(participant, ioDetails, date);
  }
}
