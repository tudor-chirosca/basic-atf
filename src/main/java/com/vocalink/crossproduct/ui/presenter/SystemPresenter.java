package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import java.time.LocalDate;
import java.util.List;

import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemPresenter implements Presenter {

  @Override
  public SettlementDashboardDto presentSettlement(List<Cycle> cycles,
      List<Participant> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ParticipantSettlementDetailsDto presentParticipantSettlementDetails(List<Cycle> cycles,
      List<PositionDetails> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross) {

    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ClientType getClientType() {
    return ClientType.SYSTEM;
  }

  @Override
  public IODashboardDto presentInputOutput(List<Participant> participants,
      List<ParticipantIOData> ioData, LocalDate date) {
    throw new RuntimeException("System API not implemented yet");
  }
}
