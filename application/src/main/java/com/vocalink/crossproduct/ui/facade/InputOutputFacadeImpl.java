package com.vocalink.crossproduct.ui.facade;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.io.IODashboard;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.facade.api.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
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
    log.info("Fetching IO Dashboard date: {} for: {} from: {}", date, clientType, product);

    Predicate<Participant> scheme = p -> p.getParticipantType() != ParticipantType.SCHEME_OPERATOR;
    Predicate<Participant> tpsp = p -> p.getParticipantType() != ParticipantType.TPSP;

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll()
        .getItems()
        .stream()
        .filter(scheme.and(tpsp))
        .collect(toList());

    final IODashboard ioDashboard = repositoryFactory.getParticipantsIODataRepository(product)
        .findAll();

    return presenterFactory.getPresenter(clientType).presentInputOutput(participants, ioDashboard, date);
  }

  @Override
  public IODetailsDto getInputOutputDetails(String product, ClientType clientType,
      String participantId, LocalDate date) {
    log.info("Fetching IO Details for participantId: {} date: {} for: {} from: {}", participantId, date, clientType, product);

    Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    IODetails ioDetails = repositoryFactory.getParticipantsIODataRepository(product)
        .findByParticipantId(participantId);

    return presenterFactory.getPresenter(clientType).presentIoDetails(participant, ioDetails, date);
  }
}
