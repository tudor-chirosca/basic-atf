package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.ParticipantIODataRepository;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
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
}
