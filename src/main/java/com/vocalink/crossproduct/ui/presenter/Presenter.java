package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import java.time.LocalDate;
import java.util.List;

public interface Presenter {
  SettlementDashboardDto presentSettlement(String context,
      List<Cycle> cycles,
      List<Participant> participants);

  ClientType getClientType();

  IODashboardDto presentInputOutput(List<Participant> participants, List<ParticipantIOData> ioData,
      LocalDate date);
}
