package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.CycleRepository;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import com.vocalink.crossproduct.ui.dto.SettlementDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.Presenter;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettlementServiceFacadeImpl implements SettlementServiceFacade {
  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;
  private final PresenterFactory presenterFactory;

  @Override
  public SettlementDto getSettlement(String context, ClientType clientType) {
    List<Participant> participants = participantRepository.findAll(context);
    List<Cycle> cycles = cycleRepository.findAll(context);

    Presenter presenter = presenterFactory.getPresenterForClient(clientType);

    return presenter.presentSettlement(context, cycles, participants);
  }

}
