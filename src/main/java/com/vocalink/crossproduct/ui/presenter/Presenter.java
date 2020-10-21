package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;

import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

  IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date);
}
