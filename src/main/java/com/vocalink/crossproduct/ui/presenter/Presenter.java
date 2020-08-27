package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.ui.dto.InputOutputDto;
import com.vocalink.crossproduct.ui.dto.SettlementDto;
import java.util.List;

public interface Presenter {
  SettlementDto presentSettlement(String context,
      List<Cycle> cycles,
      List<Participant> participants);

  InputOutputDto presentInputOutput();

  ClientType getClientType();
}
