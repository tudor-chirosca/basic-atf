package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;

import java.time.LocalDate;
import java.util.List;

public interface Presenter {
  SettlementDashboardDto presentSettlement(
      List<Cycle> cycles,
      List<Participant> participants);

  ParticipantSettlementDetailsDto presentParticipantSettlementDetails(List<Cycle> cycles,
      List<PositionDetails> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross);

  ClientType getClientType();

  IODashboardDto presentInputOutput(List<Participant> participants, List<ParticipantIOData> ioData,
      LocalDate date);
}
