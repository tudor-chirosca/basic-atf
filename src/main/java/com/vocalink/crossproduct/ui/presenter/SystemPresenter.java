package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import java.time.LocalDate;
import java.util.List;

import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemPresenter implements Presenter {

  @Override
  public SettlementDashboardDto presentAllParticipantsSettlement(List<Cycle> cycles,
      List<Participant> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public SettlementDashboardDto presentFundingParticipantSettlement(List<Cycle> cycles,
      List<Participant> participants, Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays) {
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

  @Override
  public IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date) {
    throw new RuntimeException("System API not implemented yet");
  }
}
